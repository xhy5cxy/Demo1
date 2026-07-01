package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import java.time.LocalDate;

/**
 * 行程实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("trip")
public class Trip extends BaseEntity {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "行程标题不能为空")
    @Size(max = 100, message = "行程标题长度不能超过100位")
    private String title;
    
    @NotBlank(message = "目的地不能为空")
    @Size(max = 50, message = "目的地长度不能超过50位")
    private String destination;
    
    @NotNull(message = "开始日期不能为空")
    @Future(message = "开始日期必须是未来日期")
    private LocalDate startDate;
    
    @NotNull(message = "结束日期不能为空")
    @Future(message = "结束日期必须是未来日期")
    private LocalDate endDate;
    
    @NotNull(message = "行程天数不能为空")
    private Integer days;
    
    private String description;
    
    /**
     * 是否已AI优化：0-否，1-是
     */
    private Integer isOptimized;
    
    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;
}