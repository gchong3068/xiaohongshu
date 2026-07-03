package com.gchong3068.xiaohongshu.user.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 根据手机号查询用户信息响应DTO
 * @Author: gchong3068
 * @Date: 2024/7/9 22:57
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByPhoneRspDTO {

    private Long id;
    private String password;

}
