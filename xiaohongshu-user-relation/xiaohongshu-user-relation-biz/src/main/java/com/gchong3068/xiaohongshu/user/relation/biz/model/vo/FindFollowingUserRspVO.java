package com.gchong3068.xiaohongshu.user.relation.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 取关用户响应参数
 * @Author: gchong3068
 * @Date: 2024/9/25 21:18
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindFollowingUserRspVO {

    private Long userId;

    private String avatar;

    private String nickname;

    private String introduction;
}
