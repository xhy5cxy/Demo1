package com.tour.app.admin.model;

public class AdminMenuItem {
    private int iconResId;
    private String title;
    private String description;
    private String targetActivity;
    
    public AdminMenuItem(int iconResId, String title, String description, String targetActivity) {
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
        this.targetActivity = targetActivity;
    }
    
    public int getIconResId() {
        return iconResId;
    }
    
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTargetActivity() {
        return targetActivity;
    }
    
    public void setTargetActivity(String targetActivity) {
        this.targetActivity = targetActivity;
    }
}