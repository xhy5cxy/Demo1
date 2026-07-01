package com.etour.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotNull;

/**
 * 攻略-标签关联实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("strategy_tag_relation")
public class StrategyTagRelation extends BaseEntity {
    
    @NotNull(message = "攻略ID不能为空")
    private Long strategyId;
    
    @NotNull(message = "标签ID不能为空")
    private Long tagId;
}