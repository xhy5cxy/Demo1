package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

/**
 * 攻略实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy")
public class Strategy extends BaseEntity {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "攻略标题不能为空")
    @Size(max = 100, message = "攻略标题长度不能超过100位")
    private String title;
    
    @NotBlank(message = "攻略内容不能为空")
    private String content;
    
    @NotBlank(message = "目的地不能为空")
    @Size(max = 50, message = "目的地长度不能超过50位")
    private String destination;
    
    @Size(max = 255, message = "封面图片URL长度不能超过255位")
    private String coverImage;

    private Integer viewCount;

    private Integer likeCount;

    private Integer collectCount;

    private Integer days;

    private Double budget;

    /**
     * 状态：0-待审核，1-已通过，2-已驳回
     */
    private Integer status;
    
    /**
     * 临时字段：收藏ID，用于前端取消收藏
     */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private Long collectId;
}