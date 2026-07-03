package com.gchong3068.xiaohongshu.user.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * @Description: 修改用户信息VO
 * @Author: gchong3068
 * @Date: 2024/7/3 19:07
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserInfoReqVO {

    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    /**
     * 头像
     */
    private MultipartFile avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 小哈书 ID
     */
    private String xiaohongshuId;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 个人介绍
     */
    private String introduction;

    /**
     * 背景图
     */
    private MultipartFile backgroundImg;

}
