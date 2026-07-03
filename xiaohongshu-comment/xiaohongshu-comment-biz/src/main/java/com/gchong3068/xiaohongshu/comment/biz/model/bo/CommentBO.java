package com.gchong3068.xiaohongshu.comment.biz.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: CommentBO
 * @Author: gchong3068
 * @Date: 2025/1/10 16:38
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentBO {

    private Long id;

    private Long noteId;

    private Long userId;

    private String contentUuid;

    private String content;

    private Boolean isContentEmpty;

    private String imageUrl;

    private Integer level;

    private Long replyTotal;

    private Long likeTotal;

    private Long parentId;

    private Long replyCommentId;

    private Long replyUserId;

    private Boolean isTop;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
