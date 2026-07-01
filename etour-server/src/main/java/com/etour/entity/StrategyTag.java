package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 攻略标签实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy_tag")
public class StrategyTag extends BaseEntity {
    
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 30, message = "标签名称长度不能超过30位")
    private String name;
}