package com.gchong3068.xiaohongshu.kv.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

/**
 * @Description: 评论内容
 * @Author: gchong3068
 * @Date: 2025/2/15 10:03
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCommentContentReqDTO {

    @NotBlank(message = "发布日期不能为空")
    private String yearMonth;

    @NotBlank(message = "评论正文 ID 不能为空")
    private String contentId;



}
