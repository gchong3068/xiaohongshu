package com.gchong3068.xiaohongshu.kv.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 新增笔记内容
 * @Author: gchong3068
 * @Date: 2024/7/28 22:08
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddNoteContentReqDTO {

    @NotNull(message = "笔记内容 UUID不能为空")
    private String uuid;
    @NotBlank(message = "笔记内容不能为空")
    private String content;
}
