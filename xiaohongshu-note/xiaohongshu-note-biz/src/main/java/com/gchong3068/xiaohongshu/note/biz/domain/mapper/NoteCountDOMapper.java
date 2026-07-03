package com.gchong3068.xiaohongshu.note.biz.domain.mapper;


import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteCountDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2025/6/12 17:36
 * @Version: v1.0.0
 **/
public interface NoteCountDOMapper {

    int deleteByPrimaryKey(Long id);

    int insert(NoteCountDO record);

    int insertSelective(NoteCountDO record);

    NoteCountDO selectByPrimaryKey(Long id);

    List<NoteCountDO> selectByNoteIds(@Param("noteIds") List<Long> noteIds);

    int updateByPrimaryKeySelective(NoteCountDO record);

    int updateByPrimaryKey(NoteCountDO record);
}
