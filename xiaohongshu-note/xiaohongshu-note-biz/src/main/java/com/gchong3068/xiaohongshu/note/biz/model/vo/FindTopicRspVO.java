package com.gchong3068.xiaohongshu.note.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/12 17:59
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindTopicRspVO {

    /**
     * 话题 ID
     */
    private Long id;

    /**
     * 话题名称
     */
    private String name;
}
