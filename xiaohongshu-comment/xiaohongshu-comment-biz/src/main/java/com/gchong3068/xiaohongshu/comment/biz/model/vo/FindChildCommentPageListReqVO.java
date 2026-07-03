package com.gchong3068.xiaohongshu.comment.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询二级评论分页数据
 * @Author: gchong3068
 * @Date: 2025/4/14 18:12
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindChildCommentPageListReqVO {

    @NotNull(message = "父评论 ID 不能为空" )
    private Long parentCommentId;

    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1;

}
