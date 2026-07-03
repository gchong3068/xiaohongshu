package com.gchong3068.xiaohongshu.count.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 聚合后计数：点赞、取消点赞评论
 * @Author: gchong3068
 * @Date: 2025/5/29 17:57
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggregationCountLikeUnLikeCommentMqDTO {

    /**
     * 评论 ID
     */
    private Long commentId;

    /**
     * 聚合后的计数
     */
    private Integer count;

}
