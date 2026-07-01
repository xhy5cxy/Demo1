package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

/**
 * 景点实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("spot")
public class Spot extends BaseEntity {
    
    @NotBlank(message = "景点名称不能为空")
    @Size(max = 100, message = "景点名称长度不能超过100位")
    private String name;
    
    @NotBlank(message = "城市不能为空")
    @Size(max = 50, message = "城市长度不能超过50位")
    private String city;
    
    @Size(max = 255, message = "详细地址长度不能超过255位")
    private String address;
    
    @DecimalMin(value = "-90.0", message = "纬度必须在-90到90之间")
    @DecimalMax(value = "90.0", message = "纬度必须在-90到90之间")
    private BigDecimal latitude;
    
    @DecimalMin(value = "-180.0", message = "经度必须在-180到180之间")
    @DecimalMax(value = "180.0", message = "经度必须在-180到180之间")
    private BigDecimal longitude;
    
    private String intro;
    
    @Size(max = 100, message = "开放时间长度不能超过100位")
    private String openTime;
    
    @DecimalMin(value = "0.0", message = "门票价格不能为负数")
    private BigDecimal ticketPrice;
    
    @DecimalMin(value = "0.0", message = "评分不能小于0")
    @DecimalMax(value = "5.0", message = "评分不能大于5")
    private BigDecimal rating;
}