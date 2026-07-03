package com.gchong3068.xiaohongshu.comment.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 删除评论
 * @Author: gchong3068
 * @Date: 2025/6/2 21:18
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCommentReqVO {

    @NotNull(message = "评论 ID 不能为空")
    private Long commentId;

}
