package com.gchong3068.xiaohongshu.comment.biz.domain.mapper;

import com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO;
import com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.NoteCountDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoteCountDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoteCountDO record);

    int insertSelective(NoteCountDO record);

    NoteCountDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoteCountDO record);

    int updateByPrimaryKey(NoteCountDO record);

    /**
     * 查询笔记评论总数
     * @Auther: gchong3068
     * @Date: 2025/4/1 16:44
     * @param noteId
     * @return java.lang.Long
     **/
    Long selectCommentTotalByNoteId(Long noteId);
    /**
     * 更新评论总数
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:57
 	 * @param noteId
 	 * @param count
 	 * @return int
     **/
    int updateCommentTotalByNoteId(@Param("noteId") Long noteId,
                                   @Param("count") int count);


}