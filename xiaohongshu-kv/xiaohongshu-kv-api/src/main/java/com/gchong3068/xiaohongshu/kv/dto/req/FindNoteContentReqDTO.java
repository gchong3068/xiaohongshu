package com.gchong3068.xiaohongshu.kv.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询笔记内容请求
 * @Author: gchong3068
 * @Date: 2024/7/29 21:31
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteContentReqDTO {

    @NotBlank(message = "笔记内容 UUID 不能为空")
    private String uuid;
}
