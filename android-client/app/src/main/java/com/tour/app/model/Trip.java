package com.tour.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 行程实体类
 */
public class Trip {
    private Long id;
    private Long userId;
    private String title;
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private String description;
    private Boolean isOptimized; // 是否已AI优化
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer status; // 0-草稿，1-已发布
    
    // 关联数据
    private User user;
    private List<TripSpot> tripSpots;

    public Trip() {}

    public Trip(Long id, Long userId, String title, String destination, LocalDate startDate, 
                LocalDate endDate, Integer days, String description, Boolean isOptimized, 
                Integer status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.description = description;
        this.isOptimized = isOptimized;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getDays() { return days; }
    public void setDays(Integer days) { this.days = days; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getIsOptimized() { return isOptimized; }
    public void setIsOptimized(Boolean isOptimized) { this.isOptimized = isOptimized; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<TripSpot> getTripSpots() { return tripSpots; }
    public void setTripSpots(List<TripSpot> tripSpots) { this.tripSpots = tripSpots; }
}
