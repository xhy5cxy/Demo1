package com.etour.dto;

import java.util.List;

/**
 * AI推荐请求DTO
 */
public class AIRecommendRequest {
    private List<String> preferences;
    private Double budget;
    private Integer days;

    public AIRecommendRequest() {}

    public AIRecommendRequest(List<String> preferences, Double budget, Integer days) {
        this.preferences = preferences;
        this.budget = budget;
        this.days = days;
    }

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }
}