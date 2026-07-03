package com.gchong3068.xiaohongshu.comment.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 取消评论点赞
 * @Author: gchong3068
 * @Date: 2025/5/9 17:49
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnLikeCommentReqVO {

    @NotNull(message = "评论 ID 不能为空")
    private Long commentId;

}
