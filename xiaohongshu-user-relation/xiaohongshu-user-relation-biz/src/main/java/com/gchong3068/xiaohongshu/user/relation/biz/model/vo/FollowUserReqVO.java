package com.gchong3068.xiaohongshu.user.relation.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 关注用户请求参数
 * @Author: gchong3068
 * @Date: 2024/9/9 20:54
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowUserReqVO {

    @NotNull(message = "被关注用户 ID 不能为空")
    private Long followUserId;
}
