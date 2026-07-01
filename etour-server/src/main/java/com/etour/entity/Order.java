package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("order")
public class Order extends BaseEntity {
    
    @NotBlank(message = "订单编号不能为空")
    @Size(max = 50, message = "订单编号长度不能超过50位")
    private String orderNo;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.0", message = "订单金额不能为负数")
    private BigDecimal amount;
    
    /**
     * 支付方式：1-微信，2-支付宝
     */
    @NotNull(message = "支付方式不能为空")
    private Integer payType;
    
    /**
     * 订单状态：0-待支付，1-已支付，2-已取消，3-已退款
     */
    private Integer status;
    
    private LocalDateTime payTime;
    
    private LocalDateTime expireTime;
}