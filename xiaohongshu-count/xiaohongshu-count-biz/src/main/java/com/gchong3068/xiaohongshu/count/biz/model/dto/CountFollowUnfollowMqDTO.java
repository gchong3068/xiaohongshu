package com.gchong3068.xiaohongshu.count.biz.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 计数服务 MQ传输对象
 * @Author: gchong3068
 * @Date: 2024/10/10 18:48
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
