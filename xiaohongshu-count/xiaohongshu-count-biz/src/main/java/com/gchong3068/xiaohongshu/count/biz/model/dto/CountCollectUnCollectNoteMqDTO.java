package com.gchong3068.xiaohongshu.count.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 收藏、取消收藏笔记
 * @Author: gchong3068
 * @Date: 2024/10/31 15:20
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountCollectUnCollectNoteMqDTO {

    private Long userId;

    private Long noteId;

    private Integer type;

    private LocalDateTime createTime;

}
