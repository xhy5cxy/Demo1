package com.etour.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.etour.common.ServiceException;
import com.etour.entity.User;
import com.etour.mapper.UserMapper;
import com.etour.service.UserService;
import com.etour.utils.JwtUtils;
import com.etour.utils.Md5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return getOne(wrapper);
    }

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (getByUsername(user.getUsername()) != null) {
            throw new ServiceException("用户名已存在");
        }

        // 使用MD5加密密码
        user.setPassword(Md5Utils.encode(user.getPassword()));
        user.setStatus(1);
        user.setUserType(0);
        
        // 设置创建时间和更新时间
        user.setCreateTime(java.time.LocalDateTime.now());
        user.setUpdateTime(java.time.LocalDateTime.now());

        return save(user);
    }

    @Override
    public String login(String username, String password) {
        User user = getByUsername(username);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (!Md5Utils.matches(password, user.getPassword())) {
            throw new ServiceException("密码错误");
        }

        if (user.getStatus() == 0) {
            throw new ServiceException("账号已被禁用");
        }

        return jwtUtils.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getById(userId);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new ServiceException("旧密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }
}