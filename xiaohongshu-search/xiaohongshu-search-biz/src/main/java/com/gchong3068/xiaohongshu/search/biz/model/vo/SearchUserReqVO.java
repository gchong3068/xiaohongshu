package com.gchong3068.xiaohongshu.search.biz.model.vo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 搜索用户请求参数
 * @Author: gchong3068
 * @Date: 2024/12/4 16:08
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserReqVO {

    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;

    @Min(value = 1 ,message = "页码不能小于1")
    private Integer pageNo = 1;


}
