package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;

/**
 * 用户偏好实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_preference")
public class UserPreference extends BaseEntity {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 偏好标签，用逗号分隔
     */
    private String preferenceTags;
    
    /**
     * 偏好城市，用逗号分隔
     */
    private String preferenceCities;
    
    /**
     * 预算范围：0-不限，1-500以下，2-500-1000，3-1000-3000，4-3000以上
     */
    private Integer budgetRange;
    
    /**
     * 旅行天数偏好：0-不限，1-1-3天，2-4-7天，3-7天以上
     */
    private Integer travelDays;
}