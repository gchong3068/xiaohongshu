package com.gchong3068.xiaohongshu.user.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 批量查询入参
 * @Author: gchong3068
 * @Date: 2024/9/25 18:56
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindUserByIdsReqDTO {

    @NotNull(message = "用户 ID 集合不能为空")
    @Size(min = 1,max=10 , message = "用户 ID 集合需大于1，小于等于10")
    private List<Long> ids;

}
