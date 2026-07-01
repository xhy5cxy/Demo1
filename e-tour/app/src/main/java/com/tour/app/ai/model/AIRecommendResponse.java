package com.tour.app.ai.model;

import java.util.List;
import com.tour.app.model.Spot;
import com.tour.app.model.Strategy;

/**
 * AI推荐响应模型
 * 匹配后端AI推荐响应格式
 */
public class AIRecommendResponse {
    private List<Spot> spots;
    private List<Strategy> strategies;
    private List<ItineraryDay> itinerary;

    public AIRecommendResponse() {
    }

    public AIRecommendResponse(List<Spot> spots, List<Strategy> strategies, List<ItineraryDay> itinerary) {
        this.spots = spots;
        this.strategies = strategies;
        this.itinerary = itinerary;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }

    public List<Strategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(List<Strategy> strategies) {
        this.strategies = strategies;
    }

    public List<ItineraryDay> getItinerary() {
        return itinerary;
    }

    public void setItinerary(List<ItineraryDay> itinerary) {
        this.itinerary = itinerary;
    }

    /**
     * 行程天模型
     */
    public static class ItineraryDay {
        private int day;
        private List<Activity> activities;

        public ItineraryDay() {
        }

        public ItineraryDay(int day, List<Activity> activities) {
            this.day = day;
            this.activities = activities;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public List<Activity> getActivities() {
            return activities;
        }

        public void setActivities(List<Activity> activities) {
            this.activities = activities;
        }
    }

    /**
     * 活动模型
     */
    public static class Activity {
        private String time;
        private Spot spot;
        private String description;

        public Activity() {
        }

        public Activity(String time, Spot spot, String description) {
            this.time = time;
            this.spot = spot;
            this.description = description;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Spot getSpot() {
            return spot;
        }

        public void setSpot(Spot spot) {
            this.spot = spot;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}