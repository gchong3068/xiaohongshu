package com.gchong3068.xiaohongshu.user.relation.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 通知计数服务
 * @Author: gchong3068
 * @Date: 2024/10/8 21:20
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CountFollowUnfollowMqDTO {

    private Long userId;

    private Long targetUserId;

    private Integer type;


}
