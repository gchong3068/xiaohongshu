package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/12 18:00
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindTopicListReqVO {

    @NotBlank(message = "话题关键词不能为空")
    private String keyword;
}
