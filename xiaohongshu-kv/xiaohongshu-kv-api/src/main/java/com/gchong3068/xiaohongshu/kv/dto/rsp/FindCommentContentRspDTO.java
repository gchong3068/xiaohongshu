package com.gchong3068.xiaohongshu.kv.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 评论内容
 * @Author: gchong3068
 * @Date: 2025/2/15 10:11
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindCommentContentRspDTO {

    /**
     * 评论内容 UUID
     */
    private String contentId;

    /**
     * 评论内容
     */
    private String content;


}
