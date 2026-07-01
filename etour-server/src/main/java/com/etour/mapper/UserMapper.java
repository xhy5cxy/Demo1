package com.etour.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.etour.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 根据用户名查找用户
     */
    User findByUsername(@Param("username") String username);
    
    /**
     * 查询用户列表（支持条件查询和分页）
     */
    List<User> selectUserList(@Param("params") Map<String, Object> params);
    
    /**
     * 查询用户总数（支持条件查询）
     */
    long selectUserCount(@Param("params") Map<String, Object> params);
    
    /**
     * 根据状态统计用户数量
     */
    long selectCountByStatus(@Param("status") Integer status);
    
    /**
     * 根据用户类型统计用户数量
     */
    long selectCountByUserType(@Param("userType") Integer userType);
    
    /**
     * 批量更新用户状态
     */
    int batchUpdateStatus(@Param("userIds") List<Long> userIds, 
                         @Param("status") Integer status, 
                         @Param("updateTime") LocalDateTime updateTime);
    
    /**
     * 批量删除用户
     */
    int batchDeleteByIds(@Param("userIds") List<Long> userIds);
}