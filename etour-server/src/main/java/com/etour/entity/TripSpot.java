package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 行程-景点关联实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("trip_spot")
public class TripSpot extends BaseEntity {
    
    @NotNull(message = "行程ID不能为空")
    private Long tripId;
    
    @NotNull(message = "景点ID不能为空")
    private Long spotId;
    
    @NotNull(message = "行程天数不能为空")
    private Integer day;
    
    @NotNull(message = "排序序号不能为空")
    private Integer sort;
    
    @Size(max = 50, message = "参观时间长度不能超过50位")
    private String visitTime;
    
    private String notes;
}