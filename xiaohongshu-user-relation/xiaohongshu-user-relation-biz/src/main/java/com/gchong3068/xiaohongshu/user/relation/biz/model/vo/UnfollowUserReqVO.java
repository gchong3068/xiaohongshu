package com.gchong3068.xiaohongshu.user.relation.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 取关用户请求VO
 * @Author: gchong3068
 * @Date: 2024/9/20 22:21
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnfollowUserReqVO {

    @NotNull(message = "被取关用户 ID 不能为空")
    private Long unfollowUserId;
}
