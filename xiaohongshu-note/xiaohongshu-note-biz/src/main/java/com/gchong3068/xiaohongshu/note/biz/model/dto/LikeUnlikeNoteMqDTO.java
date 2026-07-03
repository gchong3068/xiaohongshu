package com.gchong3068.xiaohongshu.note.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 点赞笔记消息
 * @Author: gchong3068
 * @Date: 2024/10/20 11:58
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeUnlikeNoteMqDTO {

    private Long userId;

    private Long noteId;
    /*
    0：取消点赞，1：点赞
     */
    private Integer type;

    private LocalDateTime createTime;

    private Long noteCreatorId;

}
