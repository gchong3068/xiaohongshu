package com.gchong3068.xiaohongshu.kv.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @Description: 查询笔记内容返回
 * @Author: gchong3068
 * @Date: 2024/7/29 21:31
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteContentRspDTO {

    private UUID uuid;

    private String content;
}
