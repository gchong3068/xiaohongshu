package com.gchong3068.xiaohongshu.user.relation.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 取关用户消息DTO
 * @Author: gchong3068
 * @Date: 2024/9/20 22:27
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnfollowUserMqDTO {

    private Long userId;

    private Long unfollowUserId;

    private LocalDateTime createTime;
}
