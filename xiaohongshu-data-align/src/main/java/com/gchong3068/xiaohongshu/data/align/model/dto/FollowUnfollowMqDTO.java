package com.gchong3068.xiaohongshu.data.align.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 用户关注，取关
 * @Author: gchong3068
 * @Date: 2024/11/15 17:37
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUnfollowMqDTO {

    /**
     * 原用户
     */
    private Long userId;

    /**
     * 目标用户
     */
    private Long targetUserId;

    /**
     * 1:关注 0:取关
     */
    private Integer type;

}
