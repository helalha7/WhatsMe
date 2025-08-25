package com.whatsme.messages_service.webhooks.dtos;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * <H1>Webhook Payload DTO</H1>
 * <P>Jackson Maps the incoming payload to this objects which follows the payload generic format :
 * {
 *   "object": "whatsapp_business_account",
 *   "entry": [{
 *       "id": "WHATSAPP_BUSINESS_ACCOUNT_ID",
 *       "changes": [{
 *           "value": {
 *               "messaging_product": "whatsapp",
 *               "metadata": {
 *                   "display_phone_number": "PHONE_NUMBER",
 *                   "phone_number_id": "PHONE_NUMBER_ID"
 *               },
 *               # specific Webhooks payload
 *           },
 *           "field": "messages"
 *         }]
 *     }]
 * }
 * </P>
 */
public class GenericWhatsAppPayLoadDTO {
    public String object;
    public List<Entry> entry;

    public static class Entry {
        public String id;
        public List<Change> changes;

        @Override
        public String toString() {
            return "Entry{" +
                    "id='" + id + '\'' +
                    ", changes=" + changes +
                    '}';
        }
    }

    public static class Change {
        public Value value;
        public String field;

        @Override
        public String toString() {
            return "Change{" +
                    "value=" + value +
                    ", field='" + field + '\'' +
                    '}';
        }
    }

    public static class Value {
        public String messaging_product;  // "whatsapp" (use this to detect)
        public MetaData metadata;         // phone_number_id, etc.

        // Catch-all for the rest (messages, statuses, errors, etc.)
        @JsonIgnore
        public Map<String, JsonNode> extras = new HashMap<>();

        @JsonAnySetter
        public void putExtra(String key, JsonNode node) {
            // 'messaging_product' and 'metadata' are already fields; others go to extras
            if (!"messaging_product".equals(key) && !"metadata".equals(key)) {
                extras.put(key, node);
            }
        }

        @Override public String toString() {
            return "Value{messaging_product='%s', metadata=%s, extrasKeys=%s}"
                    .formatted(messaging_product, metadata, extras.keySet());
        }
    }

    public static class MetaData {
        public String display_phone_number;
        public String phone_number_id;

        @Override
        public String toString() {
            return "MetaData{" +
                    "display_phone_number='" + display_phone_number + '\'' +
                    ", phone_number_id='" + phone_number_id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WhatsAppPayLoadDTO{" +
                "object='" + object + '\'' +
                ", entry=" + entry +
                '}';
    }
}
