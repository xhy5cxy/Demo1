package com.tour.app.model;

import java.time.LocalDateTime;

/**
 * 攻略标签实体类
 */
public class StrategyTag {
    private Long id;
    private String name;
    private LocalDateTime createTime;

    public StrategyTag() {}

    public StrategyTag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
