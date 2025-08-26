// src/main/java/com/whatsme/messages_service/config/CourseSettings.java
package com.whatsme.messages_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:course.properties")
@ConfigurationProperties(prefix = "course")
public class CourseSettings {
    private String title;
    private int price;
    private String startDate;      // or LocalDate if you prefer
    private int durationMonths;
    private String hours;

    // getters & setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }
    public String getHours() { return hours; }
    public void setHours(String hours) { this.hours = hours; }
}
