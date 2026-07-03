package com.gchong3068.xiaohongshu.comment.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 点赞取消点赞评论
 * @Author: gchong3068
 * @Date: 2025/5/9 16:29
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeUnlikeCommentMqDTO {

    private Long userId;

    private Long commentId;

    private Integer type;

    private LocalDateTime createTime;

}
