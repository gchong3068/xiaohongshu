package com.gchong3068.xiaohongshu.count.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 点赞、取消点赞评论
 * @Author: gchong3068
 * @Date: 2025/5/29 17:59
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountLikeUnlikeCommentMqDTO {

    private Long userId;

    private Long commentId;

    /**
     * 0: 取消点赞 ，1: 点赞
     */
    private Integer type;
}
