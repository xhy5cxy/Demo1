package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 攻略驳回原因实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy_reject_reason")
public class StrategyRejectReason extends BaseEntity {
    
    @NotNull(message = "攻略ID不能为空")
    private Long strategyId;
    
    @NotBlank(message = "驳回原因不能为空")
    private String reason;
}