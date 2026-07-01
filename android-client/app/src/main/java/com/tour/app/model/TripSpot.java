package com.tour.app.model;

/**
 * 行程-景点关联实体类
 */
public class TripSpot {
    private Long id;
    private Long tripId;
    private Long spotId;
    private Integer day; // 行程天数（第N天）
    private Integer sort; // 排序序号
    private String visitTime; // 参观时间
    private String notes; // 备注
    
    // 关联数据
    private Spot spot;

    public TripSpot() {}

    public TripSpot(Long id, Long tripId, Long spotId, Integer day, Integer sort, 
                   String visitTime, String notes) {
        this.id = id;
        this.tripId = tripId;
        this.spotId = spotId;
        this.day = day;
        this.sort = sort;
        this.visitTime = visitTime;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public Long getSpotId() { return spotId; }
    public void setSpotId(Long spotId) { this.spotId = spotId; }

    public Integer getDay() { return day; }
    public void setDay(Integer day) { this.day = day; }

    public Integer getSort() { return sort; }
    public void setSort(Integer sort) { this.sort = sort; }

    public String getVisitTime() { return visitTime; }
    public void setVisitTime(String visitTime) { this.visitTime = visitTime; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Spot getSpot() { return spot; }
    public void setSpot(Spot spot) { this.spot = spot; }
}
