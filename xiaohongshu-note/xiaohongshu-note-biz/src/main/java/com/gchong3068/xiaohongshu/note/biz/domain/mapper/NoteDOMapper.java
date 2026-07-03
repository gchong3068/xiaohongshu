package com.gchong3068.xiaohongshu.note.biz.domain.mapper;

import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteDO;
import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteLikeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoteDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoteDO record);

    int insertSelective(NoteDO record);

    NoteDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoteDO record);

    int updateByPrimaryKey(NoteDO record);

    int updateVisibleOnlyMe(NoteDO record);

    int updateIsTop(NoteDO noteDO);

    int selectCountByNoteId(Long noteId);

    Long selectCreatorIdByNoteId(Long noteId);


    List<NoteDO> selectPublishedNoteListByUserIdAndCursor(@Param("creatorId") Long creatorId,
                                                          @Param("cursor") Long cursor);

    int selectTotalCountByCreatorId(Long creatorId);

    List<NoteDO> selectByNoteIds(@Param("noteIds") List<Long> noteIds);

    List<NoteDO> selectPageListByCreatorId(@Param("creatorId") Long creatorId,
                                           @Param("offset") long offset,
                                           @Param("pageSize") long pageSize);


    List<NoteDO> selectPageList(@Param("channelId") Long channelId,
                                @Param("offset") long offset,
                                @Param("pageSize") long pageSize);

    int selectTotalCount(Long channelId);

}