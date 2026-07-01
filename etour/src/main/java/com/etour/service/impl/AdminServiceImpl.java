package com.etour.service.impl;

import com.etour.dto.UserQueryRequest;
import com.etour.entity.User;
import com.etour.mapper.UserMapper;
import com.etour.service.AdminService;
import com.etour.utils.JwtUtils;
import com.etour.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员服务实现类
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public String adminLogin(String username, String password) {
        // Validate admin account
        User adminUser = userMapper.findByUsername(username);
        if (adminUser == null) {
            throw new RuntimeException("Admin account does not exist");
        }
        
        // Validate user type (must be admin)
        if (adminUser.getUserType() != 1) {
            throw new RuntimeException("No admin permission");
        }
        
        // Validate password using MD5 with salt
        if (!Md5Utils.matches(password, adminUser.getPassword())) {
            throw new RuntimeException("Password error");
        }
        
        // Validate user status
        if (adminUser.getStatus() != 1) {
            throw new RuntimeException("Account has been disabled");
        }
        
        // Generate JWT token
        return jwtUtils.generateToken(adminUser.getId(), adminUser.getUsername());
    }

    @Override
    public List<User> getUserList(UserQueryRequest request) {
        // 构建查询条件
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(request.getUsername())) {
            params.put("username", "%" + request.getUsername() + "%");
        }
        if (StringUtils.hasText(request.getNickname())) {
            params.put("nickname", "%" + request.getNickname() + "%");
        }
        if (StringUtils.hasText(request.getEmail())) {
            params.put("email", "%" + request.getEmail() + "%");
        }
        if (StringUtils.hasText(request.getPhone())) {
            params.put("phone", "%" + request.getPhone() + "%");
        }
        if (request.getStatus() != null) {
            params.put("status", request.getStatus());
        }
        if (request.getUserType() != null) {
            params.put("userType", request.getUserType());
        }
        
        // 分页参数
        int offset = (request.getPage() - 1) * request.getSize();
        params.put("offset", offset);
        params.put("size", request.getSize());
        
        // 排序
        params.put("sortField", request.getSortField());
        params.put("sortOrder", request.getSortOrder());
        
        return userMapper.selectUserList(params);
    }

    @Override
    public long getUserCount(UserQueryRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(request.getUsername())) {
            params.put("username", "%" + request.getUsername() + "%");
        }
        if (StringUtils.hasText(request.getNickname())) {
            params.put("nickname", "%" + request.getNickname() + "%");
        }
        if (StringUtils.hasText(request.getEmail())) {
            params.put("email", "%" + request.getEmail() + "%");
        }
        if (StringUtils.hasText(request.getPhone())) {
            params.put("phone", "%" + request.getPhone() + "%");
        }
        if (request.getStatus() != null) {
            params.put("status", request.getStatus());
        }
        if (request.getUserType() != null) {
            params.put("userType", request.getUserType());
        }
        
        return userMapper.selectUserCount(params);
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // Cannot disable admin account (simplified for demo)
        if (user.getUserType() == 1) {
            throw new RuntimeException("Cannot disable admin account");
        }
        
        user.setStatus(status);
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean batchUpdateUserStatus(List<Long> userIds, Integer status) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        
        // 过滤掉管理员账号
        List<Long> validUserIds = userIds.stream()
                .filter(userId -> {
                    User user = userMapper.selectById(userId);
                    return user != null && user.getUserType() != 1;
                })
                .collect(Collectors.toList());
        
        if (validUserIds.isEmpty()) {
            return false;
        }
        
        return userMapper.batchUpdateStatus(validUserIds, status, LocalDateTime.now()) > 0;
    }

    @Override
    public boolean deleteUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        
        // Cannot delete admin account
        if (user.getUserType() == 1) {
            throw new RuntimeException("Cannot delete admin account");
        }
        
        return userMapper.deleteById(userId) > 0;
    }

    @Override
    public boolean batchDeleteUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return false;
        }
        
        // 过滤掉管理员账号
        List<Long> validUserIds = userIds.stream()
                .filter(userId -> {
                    User user = userMapper.selectById(userId);
                    return user != null && user.getUserType() != 1;
                })
                .collect(Collectors.toList());
        
        if (validUserIds.isEmpty()) {
            return false;
        }
        
        return userMapper.batchDeleteByIds(validUserIds) > 0;
    }

    @Override
    public User getUserDetail(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public Object getSystemStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 获取用户统计
        statistics.put("totalUsers", userMapper.selectCount(null));
        statistics.put("activeUsers", userMapper.selectCountByStatus(1));
        statistics.put("inactiveUsers", userMapper.selectCountByStatus(0));
        statistics.put("adminUsers", userMapper.selectCountByUserType(1));
        
        // 获取其他统计数据（这里简化处理）
        statistics.put("todayNewUsers", 5);
        statistics.put("weekNewUsers", 35);
        statistics.put("monthNewUsers", 120);
        
        return statistics;
    }

    @Override
    public Object getSystemMonitor() {
        Map<String, Object> monitorData = new HashMap<>();
        
        // 系统状态
        monitorData.put("apiService", "正常");
        monitorData.put("database", "正常");
        monitorData.put("cache", "正常");
        
        // 资源使用率（模拟数据）
        monitorData.put("cpuUsage", 45.2);
        monitorData.put("memoryUsage", 68.7);
        monitorData.put("diskUsage", 32.1);
        
        // API调用统计（模拟数据）
        Map<String, Object> apiStats = new HashMap<>();
        apiStats.put("totalCalls", 1250);
        apiStats.put("successCalls", 1180);
        apiStats.put("errorCalls", 70);
        apiStats.put("successRate", 94.4);
        monitorData.put("apiStats", apiStats);
        
        // 错误日志（模拟数据）
        List<Map<String, Object>> errorLogs = new ArrayList<>();
        
        Map<String, Object> error1 = new HashMap<>();
        error1.put("time", "2024-01-15 10:23:45");
        error1.put("type", "Database connection timeout");
        error1.put("level", "ERROR");
        
        Map<String, Object> error2 = new HashMap<>();
        error2.put("time", "2024-01-15 09:15:32");
        error2.put("type", "File upload failed");
        error2.put("level", "WARN");
        
        Map<String, Object> error3 = new HashMap<>();
        error3.put("time", "2024-01-15 08:42:11");
        error3.put("type", "User authentication failed");
        error3.put("level", "INFO");
        
        errorLogs.add(error1);
        errorLogs.add(error2);
        errorLogs.add(error3);
        monitorData.put("errorLogs", errorLogs);
        
        return monitorData;
    }
}