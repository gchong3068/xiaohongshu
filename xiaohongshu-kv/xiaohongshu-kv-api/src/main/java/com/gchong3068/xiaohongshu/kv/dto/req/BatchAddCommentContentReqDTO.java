package com.gchong3068.xiaohongshu.kv.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 批量新增评论内容
 * @Author: gchong3068
 * @Date: 2025/1/10 15:13
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchAddCommentContentReqDTO {

    @NotEmpty(message = "评论内容集合不能为空")
    @Valid
    private List<CommentContentReqDTO> comments;


}
