package com.tour.app.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 攻略实体类
 */
public class Strategy {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String destination;
    private String coverImage;
    private Integer viewCount;
    private Integer likeCount;
    private Integer collectCount;
    private Integer status; // 0-待审核，1-已通过，2-已驳回
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 关联数据
    private User author;
    private List<StrategyTag> tags;

    public Strategy() {}

    public Strategy(Long id, Long userId, String title, String content, String destination, 
                   String coverImage, Integer viewCount, Integer likeCount, Integer collectCount, 
                   Integer status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.destination = destination;
        this.coverImage = coverImage;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.collectCount = collectCount;
        this.status = status;
    }
    
    public Strategy(String title, String content, User author, String destination, Integer likeCount, Integer viewCount) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.destination = destination;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getLikeCount() { return likeCount; }
    public void setLikeCount(Integer likeCount) { this.likeCount = likeCount; }

    public Integer getCollectCount() { return collectCount; }
    public void setCollectCount(Integer collectCount) { this.collectCount = collectCount; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public List<StrategyTag> getTags() { return tags; }
    public void setTags(List<StrategyTag> tags) { this.tags = tags; }
}
