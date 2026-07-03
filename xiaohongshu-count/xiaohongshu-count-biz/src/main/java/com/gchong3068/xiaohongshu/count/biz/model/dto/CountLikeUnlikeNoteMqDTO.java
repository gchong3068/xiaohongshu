package com.gchong3068.xiaohongshu.count.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 点赞、取消点赞笔记
 * @Author: gchong3068
 * @Date: 2024/10/27 20:45
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountLikeUnlikeNoteMqDTO {

    private Long userId;

    private Long noteId;

    private Integer type;

    private LocalDateTime createTime;

    private Long noteCreatorId;

}
