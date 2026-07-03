package com.gchong3068.xiaohongshu.user.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 根据Id查询返参
 * @Author: gchong3068
 * @Date: 2024/8/18 18:53
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByIdRspDTO {

    //用户ID
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 简介
     */
    private String introduction;
}
