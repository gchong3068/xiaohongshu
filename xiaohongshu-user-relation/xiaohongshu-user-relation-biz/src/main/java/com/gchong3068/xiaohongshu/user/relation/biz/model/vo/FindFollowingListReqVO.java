package com.gchong3068.xiaohongshu.user.relation.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 关注列表请求参数
 * @Author: gchong3068
 * @Date: 2024/9/25 21:15
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindFollowingListReqVO {

    @NotNull(message = "查询用户 ID 不能为空")
    private Long userId;


    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1;
}
