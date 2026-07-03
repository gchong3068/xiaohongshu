package com.gchong3068.xiaohongshu.kv.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 批量查询评论内容
 * @Author: gchong3068
 * @Date: 2025/2/15 10:03
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchFindCommentContentReqDTO {

    @NotNull(message = "评论 ID 不能为空")
    private Long noteId;

    @NotEmpty(message = "评论内容 Key 集合")
    @Valid  //参数中 FindCommentContentReqDTO 也需要校验
    private List<FindCommentContentReqDTO> commentContentKeys;

}
