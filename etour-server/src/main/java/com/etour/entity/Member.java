package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;

/**
 * 会员实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("member")
public class Member extends BaseEntity {
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    /**
     * 会员等级：1-普通会员，2-高级会员
     */
    @NotNull(message = "会员等级不能为空")
    private Integer level;
    
    @NotNull(message = "会员生效时间不能为空")
    private LocalDateTime startTime;
    
    @NotNull(message = "会员到期时间不能为空")
    @Future(message = "会员到期时间必须是未来时间")
    private LocalDateTime endTime;
}