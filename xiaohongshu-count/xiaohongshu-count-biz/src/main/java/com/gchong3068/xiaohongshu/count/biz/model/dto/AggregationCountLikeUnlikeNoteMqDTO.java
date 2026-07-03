package com.gchong3068.xiaohongshu.count.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 聚合后计数：点赞、取消点赞笔记
 * @Author: gchong3068
 * @Date: 2024/11/2 16:57
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AggregationCountLikeUnlikeNoteMqDTO {

    private Long creatorId;

    private Long noteId;

    private Integer count;
}
