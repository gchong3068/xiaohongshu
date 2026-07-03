package com.gchong3068.xiaohongshu.user.relation.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询粉丝用户返回
 * @Author: gchong3068
 * @Date: 2024/9/28 11:33
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindFansUserRspVO {


    private Long userId;

    private String nickname;

    private String avatar;

    private Long fansTotal;

    private Long noteTotal;
}
