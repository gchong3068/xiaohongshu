package com.gchong3068.xiaohongshu.data.align.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 收藏、取消收藏笔记
 * @Author: gchong3068
 * @Date: 2024/11/10 20:51
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollectUnCollectNoteMqDTO {

    private Long userId;

    private Long noteId;

    /**
     * 0: 取消收藏， 1：收藏
     */
    private Integer type;

    private LocalDateTime createTime;

    /**
     * 笔记发布者 ID
     */
    private Long noteCreatorId;


}
