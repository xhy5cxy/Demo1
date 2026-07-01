package com.etour.controller;

import com.etour.common.Result;
import com.etour.dto.LoginRequest;
import com.etour.dto.RegisterRequest;
import com.etour.dto.ChangePasswordRequest;
import com.etour.dto.UserInfoRequest;
import com.etour.entity.User;
import com.etour.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户Controller
 */
@Tag(name = "用户管理", description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "用户注册接口")
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
            
            boolean success = userService.register(user);
            if (success) {
                return Result.success("注册成功");
            }
            return Result.error("注册失败：用户名已存在");
        } catch (Exception e) {
            return Result.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录接口")
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getUsername(), request.getPassword());
        return Result.success("登录成功", token);
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestParam Long userId) {
        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword(null); // 不返回密码
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }

    /**
     * 修改密码
     */
    @Operation(summary = "修改密码", description = "用户修改密码接口")
    @PostMapping("/change-password")
    public Result<String> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean success = userService.changePassword(request.getUserId(), request.getOldPassword(), request.getNewPassword());
        if (success) {
            return Result.success("密码修改成功");
        }
        return Result.error("密码修改失败");
    }
}