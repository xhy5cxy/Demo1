package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 评论实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("comment")
public class Comment extends BaseEntity {
    
    @NotNull(message = "攻略ID不能为空")
    private Long strategyId;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotBlank(message = "评论内容不能为空")
    private String content;
    
    private Long parentId;
    
    private Integer likeCount;
    
    /**
     * 评论状态：0-已禁用/已删除，1-正常显示，2-待审核
     */
    private Integer status;
}