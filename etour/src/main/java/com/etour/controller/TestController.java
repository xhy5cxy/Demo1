package com.etour.controller;

import com.etour.entity.User;
import com.etour.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试控制器
 * 
 * @author etour
 * @since 2024-01-01
 */
@Tag(name = "测试接口", description = "系统测试相关接口")
@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * 测试数据库连接
     */
    @Operation(summary = "测试数据库连接", description = "测试数据库连接状态")
    @GetMapping("/db")
    public String testDatabase() {
        try {
            List<User> users = userMapper.selectList(null);
            return "数据库连接成功！用户数量：" + users.size();
        } catch (Exception e) {
            return "数据库连接失败：" + e.getMessage();
        }
    }
    
    /**
     * 测试系统运行状态
     */
    @Operation(summary = "测试系统运行状态", description = "测试系统健康状态")
    @GetMapping("/health")
    public String health() {
        return "ETour 后端服务运行正常！";
    }
}