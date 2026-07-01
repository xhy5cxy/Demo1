package com.tour.app.ai.model;

import java.util.List;

/**
 * AI推荐请求模型
 * 匹配后端AIRecommendRequest格式
 */
public class AIRecommendRequest {
    private String location;
    private Double budget;
    private Integer days;
    private List<String> preferences;
    private String season;

    public AIRecommendRequest() {
    }

    public AIRecommendRequest(String location, Double budget, Integer days, List<String> preferences, String season) {
        this.location = location;
        this.budget = budget;
        this.days = days;
        this.preferences = preferences;
        this.season = season;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<String> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<String> preferences) {
        this.preferences = preferences;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}