// src/main/java/com/whatsme/messages_service/service/MessageAggregator.java
package com.whatsme.messages_service.service;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class MessageAggregator {

    private final TaskScheduler scheduler;
    private final GPTMessageService gpt;
    private final WhatsAppSender waSender; // tiny helper to send replies

    // How long to wait after the last fragment before flushing
    private final Duration quietPeriod = Duration.ofMillis(5000);

    private final Map<String, Pending> buffers = new ConcurrentHashMap<>();

    public MessageAggregator(TaskScheduler scheduler, GPTMessageService gpt, WhatsAppSender waSender) {
        this.scheduler = scheduler;
        this.gpt = gpt;
        this.waSender = waSender;
    }

    /** Enqueue an incoming user fragment. from = user wa_id/phone, to = your business phone (not PHONE_NUMBER_ID) */
    public void onIncoming(String from, String to, String text) {
        buffers.compute(from, (key, pending) -> {
            if (pending == null) pending = new Pending(from, to);
            pending.append(text);
            pending.rescheduleFlush();
            return pending;
        });
    }

    private class Pending {
        final String from;
        final String to;
        final StringBuilder buf = new StringBuilder();
        volatile ScheduledFuture<?> future;

        Pending(String from, String to) {
            this.from = from; this.to = to;
        }

        synchronized void append(String text) {
            if (buf.length() > 0) buf.append(" ");
            // add simple punctuation if fragment ends without one
            if (text != null) {
                String t = text.trim();
                buf.append(t);
                if (!t.isEmpty() && ".!?".indexOf(t.charAt(t.length()-1)) < 0) buf.append(".");
            }
        }

        synchronized void rescheduleFlush() {
            if (future != null) future.cancel(false);
            future = scheduler.schedule(this::flushSafe, java.util.Date.from(
                    java.time.Instant.now().plus(quietPeriod)));
        }

        void flushSafe() {
            String payload;
            String fromLocal = this.from;
            String toLocal   = this.to;
            synchronized (this) {
                payload = buf.toString().trim();
                buf.setLength(0);
                future = null;
            }
            if (!payload.isEmpty()) {
                try {
                    // Single call to OpenAI for the whole batch
                    String reply = gpt.complete(payload);
                    waSender.sendText(toLocal, fromLocal, reply); // to=user, from=your business (uses PHONE_NUMBER_ID internally)
                } catch (Exception e) {
                    // log and swallow to avoid killing scheduler thread
                    e.printStackTrace();
                } finally {
                    // remove empty buffer instance
                    buffers.remove(fromLocal, this);
                }
            } else {
                buffers.remove(fromLocal, this);
            }
        }
    }
}
