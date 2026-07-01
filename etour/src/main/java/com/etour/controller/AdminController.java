package com.etour.controller;

import com.etour.common.Result;
import com.etour.dto.AdminLoginRequest;
import com.etour.dto.UserQueryRequest;
import com.etour.entity.User;
import com.etour.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理员控制器
 */
@Tag(name = "管理员接口", description = "后台管理系统专用接口")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理员登录
     */
    @Operation(summary = "管理员登录", description = "后台管理系统登录接口")
    @PostMapping("/login")
    public Result<String> adminLogin(@RequestBody @Valid AdminLoginRequest request) {
        try {
            String token = adminService.adminLogin(request.getUsername(), request.getPassword());
            return Result.success("登录成功", token);
        } catch (Exception e) {
            return Result.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户列表（支持分页和查询）
     */
    @Operation(summary = "获取用户列表", description = "后台管理系统用户列表接口")
    @PostMapping("/users")
    public Result<List<User>> getUserList(@RequestBody UserQueryRequest request) {
        try {
            List<User> users = adminService.getUserList(request);
            return Result.success(users);
        } catch (Exception e) {
            return Result.error("获取用户列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户总数（用于分页）
     */
    @Operation(summary = "获取用户总数", description = "获取用户总数用于分页")
    @PostMapping("/users/count")
    public Result<Long> getUserCount(@RequestBody UserQueryRequest request) {
        try {
            long count = adminService.getUserCount(request);
            return Result.success(count);
        } catch (Exception e) {
            return Result.error("获取用户总数失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户状态（启用/禁用）
     */
    @Operation(summary = "更新用户状态", description = "启用或禁用用户账号")
    @PostMapping("/users/{userId}/status")
    public Result<String> updateUserStatus(@PathVariable Long userId, @RequestParam Integer status) {
        try {
            boolean success = adminService.updateUserStatus(userId, status);
            if (success) {
                return Result.success(status == 1 ? "用户已启用" : "用户已禁用");
            }
            return Result.error("更新用户状态失败");
        } catch (Exception e) {
            return Result.error("更新用户状态失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新用户状态
     */
    @Operation(summary = "批量更新用户状态", description = "批量启用或禁用用户账号")
    @PostMapping("/users/batch-status")
    public Result<String> batchUpdateUserStatus(@RequestParam List<Long> userIds, @RequestParam Integer status) {
        try {
            boolean success = adminService.batchUpdateUserStatus(userIds, status);
            if (success) {
                return Result.success("批量更新用户状态成功");
            }
            return Result.error("批量更新用户状态失败");
        } catch (Exception e) {
            return Result.error("批量更新用户状态失败：" + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "删除用户账号")
    @DeleteMapping("/users/{userId}")
    public Result<String> deleteUser(@PathVariable Long userId) {
        try {
            boolean success = adminService.deleteUser(userId);
            if (success) {
                return Result.success("用户删除成功");
            }
            return Result.error("用户删除失败");
        } catch (Exception e) {
            return Result.error("用户删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除用户
     */
    @Operation(summary = "批量删除用户", description = "批量删除用户账号")
    @DeleteMapping("/users/batch")
    public Result<String> batchDeleteUsers(@RequestParam List<Long> userIds) {
        try {
            boolean success = adminService.batchDeleteUsers(userIds);
            if (success) {
                return Result.success("批量删除用户成功");
            }
            return Result.error("批量删除用户失败");
        } catch (Exception e) {
            return Result.error("批量删除用户失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "获取用户详情", description = "获取用户详细信息")
    @GetMapping("/users/{userId}")
    public Result<User> getUserDetail(@PathVariable Long userId) {
        try {
            User user = adminService.getUserDetail(userId);
            if (user != null) {
                user.setPassword(null); // 不返回密码
                return Result.success(user);
            }
            return Result.error("用户不存在");
        } catch (Exception e) {
            return Result.error("获取用户详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取系统统计数据
     */
    @Operation(summary = "获取系统统计数据", description = "获取系统运行统计数据")
    @GetMapping("/statistics")
    public Result<Object> getSystemStatistics() {
        try {
            Object statistics = adminService.getSystemStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            return Result.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取系统监控数据
     */
    @Operation(summary = "获取系统监控数据", description = "获取系统运行监控数据")
    @GetMapping("/monitor")
    public Result<Object> getSystemMonitor() {
        try {
            Object monitorData = adminService.getSystemMonitor();
            return Result.success(monitorData);
        } catch (Exception e) {
            return Result.error("获取监控数据失败：" + e.getMessage());
        }
    }
}