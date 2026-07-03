package com.gchong3068.xiaohongshu.note.biz.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/12 17:38
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteCountDO {


    private Long id;

    private Long noteId;

    private Long likeTotal;

    private Long collectTotal;

    private Long commentTotal;
}
