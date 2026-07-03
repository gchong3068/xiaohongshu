package com.gchong3068.xiaohongshu.user.relation.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询粉丝列表
 * @Author: gchong3068
 * @Date: 2024/9/28 11:32
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindFansListReqVO {

    @NotNull(message = "查询用户ID不能为空")
    private Long userId;

    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1;

}
