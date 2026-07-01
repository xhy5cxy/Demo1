package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 收藏实体类
 * 支持攻略和景点收藏
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("collect")
public class Collect extends BaseEntity {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 收藏类型：1-攻略，2-景点
     */
    @NotNull(message = "收藏类型不能为空")
    private Integer type;
    
    /**
     * 目标ID（根据类型关联不同表的ID）
     */
    @NotNull(message = "目标ID不能为空")
    private Long targetId;
    
    /**
     * 兼容旧的攻略ID字段
     */
    @TableField(exist = false)
    private Long strategyId;
    
    private LocalDateTime createTime;
}