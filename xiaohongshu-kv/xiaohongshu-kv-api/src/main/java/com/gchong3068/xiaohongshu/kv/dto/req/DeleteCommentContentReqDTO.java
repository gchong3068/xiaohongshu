package com.gchong3068.xiaohongshu.kv.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 评论内容删除
 * @Author: gchong3068
 * @Date: 2025/6/2 21:00
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteCommentContentReqDTO {

    @NotNull(message = "笔记 ID 不能为空")
    private Long noteId;

    @NotBlank(message = "发布年月不能为空")
    private String yearMonth;

    @NotBlank(message = "内容 ID 不能为空")
    private String contentId;
}
