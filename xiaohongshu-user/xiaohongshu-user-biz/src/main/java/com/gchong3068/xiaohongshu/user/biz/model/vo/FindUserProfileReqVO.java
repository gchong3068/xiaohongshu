package com.gchong3068.xiaohongshu.user.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 获取用户主页信息
 * @Author: gchong3068
 * @Date: 2025/6/5 16:55
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserProfileReqVO {

    private Long userId;

}
