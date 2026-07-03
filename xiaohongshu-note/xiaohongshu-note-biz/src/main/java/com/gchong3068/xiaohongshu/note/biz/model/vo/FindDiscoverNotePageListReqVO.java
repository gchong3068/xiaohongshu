package com.gchong3068.xiaohongshu.note.biz.model.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/12 17:35
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindDiscoverNotePageListReqVO {

    /**
     * 频道 ID
     */
    private Long channelId;

    @NotNull(message = "页码不能为空")
    private Integer pageNo = 1;
}
