package com.gchong3068.xiaohongshu.user.relation.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Description: 关注用户消息
 * @Author: gchong3068
 * @Date: 2024/9/13 22:17
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUserMqDTO {

    private Long userid;

    private Long followUserId;

    private LocalDateTime createTime;
}
