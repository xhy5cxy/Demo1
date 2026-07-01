package com.etour.dto;

import lombok.Data;

/**
 * 用户查询请求DTO
 */
@Data
public class UserQueryRequest {
    
    /**
     * 页码
     */
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    private Integer size = 10;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 用户状态：0-禁用，1-正常
     */
    private Integer status;
    
    /**
     * 用户类型：0-普通用户，1-管理员
     */
    private Integer userType;
    
    /**
     * 排序字段
     */
    private String sortField = "create_time";
    
    /**
     * 排序方式：asc-升序，desc-降序
     */
    private String sortOrder = "desc";
}