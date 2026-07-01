package com.tour.app.strategy.model;

public class Comment {
    private int id;
    private int strategyId;
    private int userAvatar;
    private String username;
    private String content;
    private long createTime;
    private int likeCount;
    private boolean isLiked;

    public Comment() {
    }

    public Comment(int id, int strategyId, String username, String content, long createTime, int likeCount) {
        this.id = id;
        this.strategyId = strategyId;
        this.username = username;
        this.content = content;
        this.createTime = createTime;
        this.likeCount = likeCount;
        this.isLiked = false;
    }

    public Comment(int id, int strategyId, int userAvatar, String username, String content, long createTime, int likeCount) {
        this.id = id;
        this.strategyId = strategyId;
        this.userAvatar = userAvatar;
        this.username = username;
        this.content = content;
        this.createTime = createTime;
        this.likeCount = likeCount;
        this.isLiked = false;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }

    public int getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}