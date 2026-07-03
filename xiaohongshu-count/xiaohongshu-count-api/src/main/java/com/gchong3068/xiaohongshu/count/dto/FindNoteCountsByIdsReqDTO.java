package com.gchong3068.xiaohongshu.count.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 批量查询笔记维度相关计数
 * @Author: gchong3068
 * @Date: 2025/6/10 18:57
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindNoteCountsByIdsReqDTO {

    @NotNull(message = "笔记 ID 集合不能为空")
    @Size(min = 1, max = 20, message = "笔记 ID 集合大小必须大于等于 1, 小于等于 20")
    private List<Long> noteIds;
}
