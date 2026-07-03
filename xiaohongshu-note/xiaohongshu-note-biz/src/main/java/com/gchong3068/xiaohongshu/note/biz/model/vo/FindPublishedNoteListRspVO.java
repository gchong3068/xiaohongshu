package com.gchong3068.xiaohongshu.note.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 个人主页 - 已发布笔记列表
 * @Author: gchong3068
 * @Date: 2025/6/10 18:31
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindPublishedNoteListRspVO {

    /**
     * 笔记分页数据
     */
    private List<NoteItemRspVO> notes;

    /**
     * 下一页的游标
     */
    private Long nextCursor;
}
