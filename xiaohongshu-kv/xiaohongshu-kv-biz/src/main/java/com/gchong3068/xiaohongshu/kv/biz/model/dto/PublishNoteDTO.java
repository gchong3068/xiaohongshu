package com.gchong3068.xiaohongshu.kv.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/11 23:58
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishNoteDTO {

    private Long id;

    private String title;

    private Boolean isContentEmpty;

    private Long creatorId;

    private Long topicId;

    private String topicName;

    private Boolean isTop;

    private Integer type;

    private String imgUris;

    private String videoUri;

    private Integer visible;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer status;

    private String contentUuid;

    private String content;
}
