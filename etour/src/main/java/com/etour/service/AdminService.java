package com.etour.service;

import com.etour.dto.UserQueryRequest;
import com.etour.entity.User;

import java.util.List;

/**
 * 管理员服务接口
 */
public interface AdminService {
    
    /**
     * 管理员登录
     */
    String adminLogin(String username, String password);
    
    /**
     * 获取用户列表
     */
    List<User> getUserList(UserQueryRequest request);
    
    /**
     * 获取用户总数
     */
    long getUserCount(UserQueryRequest request);
    
    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Long userId, Integer status);
    
    /**
     * 批量更新用户状态
     */
    boolean batchUpdateUserStatus(List<Long> userIds, Integer status);
    
    /**
     * 删除用户
     */
    boolean deleteUser(Long userId);
    
    /**
     * 批量删除用户
     */
    boolean batchDeleteUsers(List<Long> userIds);
    
    /**
     * 获取用户详情
     */
    User getUserDetail(Long userId);
    
    /**
     * 获取系统统计数据
     */
    Object getSystemStatistics();
    
    /**
     * 获取系统监控数据
     */
    Object getSystemMonitor();
}