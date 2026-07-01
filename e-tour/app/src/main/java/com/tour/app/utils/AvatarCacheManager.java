package com.tour.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 头像缓存管理器
 * 用于管理用户头像的本地缓存
 */
public class AvatarCacheManager {
    
    private static final String TAG = "AvatarCacheManager";
    private static final String CACHE_DIR_NAME = "avatar_cache";
    private static final long MAX_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
    
    private Context context;
    private File cacheDir;
    
    public AvatarCacheManager(Context context) {
        this.context = context.getApplicationContext();
        this.cacheDir = getCacheDirectory();
        ensureCacheDirectoryExists();
    }
    
    /**
     * 获取缓存目录
     */
    private File getCacheDirectory() {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            return new File(externalCacheDir, CACHE_DIR_NAME);
        } else {
            return new File(context.getCacheDir(), CACHE_DIR_NAME);
        }
    }
    
    /**
     * 确保缓存目录存在
     */
    private void ensureCacheDirectoryExists() {
        if (!cacheDir.exists()) {
            if (!cacheDir.mkdirs()) {
                Log.e(TAG, "Failed to create cache directory: " + cacheDir.getAbsolutePath());
            }
        }
    }
    
    /**
     * 根据用户ID生成缓存文件名
     */
    private String getCacheFileName(Long userId) {
        if (userId == null) {
            return "default_avatar.png";
        }
        return "avatar_" + userId + ".png";
    }
    
    /**
     * 获取缓存文件
     */
    private File getCacheFile(Long userId) {
        return new File(cacheDir, getCacheFileName(userId));
    }
    
    /**
     * 保存头像到缓存
     */
    public boolean saveAvatarToCache(Long userId, Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }
        
        File cacheFile = getCacheFile(userId);
        FileOutputStream fos = null;
        
        try {
            fos = new FileOutputStream(cacheFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            Log.d(TAG, "Avatar saved to cache: " + cacheFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to save avatar to cache", e);
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close file output stream", e);
                }
            }
        }
    }
    
    /**
     * 从缓存加载头像
     */
    public Bitmap loadAvatarFromCache(Long userId) {
        File cacheFile = getCacheFile(userId);
        
        if (!cacheFile.exists()) {
            return null;
        }
        
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
            if (bitmap != null) {
                Log.d(TAG, "Avatar loaded from cache: " + cacheFile.getAbsolutePath());
            }
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "Failed to load avatar from cache", e);
            return null;
        }
    }
    
    /**
     * 检查缓存中是否有头像
     */
    public boolean hasAvatarInCache(Long userId) {
        File cacheFile = getCacheFile(userId);
        return cacheFile.exists();
    }
    
    /**
     * 获取缓存头像文件路径
     */
    public String getAvatarCachePath(Long userId) {
        File cacheFile = getCacheFile(userId);
        if (cacheFile.exists()) {
            return cacheFile.getAbsolutePath();
        }
        return null;
    }
    
    /**
     * 删除缓存的头像
     */
    public boolean deleteAvatarFromCache(Long userId) {
        File cacheFile = getCacheFile(userId);
        if (cacheFile.exists()) {
            boolean deleted = cacheFile.delete();
            if (deleted) {
                Log.d(TAG, "Avatar deleted from cache: " + cacheFile.getAbsolutePath());
            }
            return deleted;
        }
        return true; // 文件不存在，视为删除成功
    }
    
    /**
     * 清理所有缓存
     */
    public void clearAllCache() {
        if (cacheDir.exists() && cacheDir.isDirectory()) {
            File[] files = cacheDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        Log.e(TAG, "Failed to delete cache file: " + file.getAbsolutePath());
                    }
                }
            }
        }
        Log.d(TAG, "All avatar cache cleared");
    }
    
    /**
     * 获取缓存大小
     */
    public long getCacheSize() {
        if (!cacheDir.exists() || !cacheDir.isDirectory()) {
            return 0;
        }
        
        long totalSize = 0;
        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                totalSize += file.length();
            }
        }
        return totalSize;
    }
    
    /**
     * 检查是否需要清理缓存
     */
    public boolean needsCacheCleanup() {
        return getCacheSize() > MAX_CACHE_SIZE;
    }
    
    /**
     * 清理过期的缓存文件（按时间排序，删除最旧的文件）
     */
    public void cleanupOldCache() {
        if (!cacheDir.exists() || !cacheDir.isDirectory()) {
            return;
        }
        
        File[] files = cacheDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        
        // 按最后修改时间排序
        java.util.Arrays.sort(files, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
        
        long currentSize = getCacheSize();
        int filesDeleted = 0;
        
        for (File file : files) {
            if (currentSize <= MAX_CACHE_SIZE) {
                break;
            }
            
            long fileSize = file.length();
            if (file.delete()) {
                currentSize -= fileSize;
                filesDeleted++;
                Log.d(TAG, "Deleted old cache file: " + file.getAbsolutePath());
            }
        }
        
        if (filesDeleted > 0) {
            Log.d(TAG, "Cleaned up " + filesDeleted + " old cache files");
        }
    }
}