package com.gchong3068.xiaohongshu.note.biz.domain.mapper;

import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteLikeDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoteLikeDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoteLikeDO record);

    int insertSelective(NoteLikeDO record);

    NoteLikeDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoteLikeDO record);

    int updateByPrimaryKey(NoteLikeDO record);

    int selectCountByUserIdAndNoteId(@Param("userId")Long userId,@Param("noteId") Long noteId);

    List<NoteLikeDO> selectByUserId(@Param("userId") Long userId);


    int selectNoteIsLiked(@Param("userId") Long userId,@Param("noteId") Long noteId);

    List<NoteLikeDO> selectLikedByUserIdAndLimit(@Param("userId") Long userId,@Param("limit") int limit);
    
    /**
     * 新增笔记点赞记录
     * @Auther: gchong3068
     * @Date: 2024/10/20 12:39 
 	 * @param noteLikeDO 笔记点赞DO
 	 * @return int
     **/
    int insertOrUpdate(NoteLikeDO noteLikeDO);


    /**
     * 取消点赞
     */
    int update2UnlikeByUserIdAndNoteId(NoteLikeDO noteLikeDO);

    /**
     * 查询某用户，对于一批量笔记的已点赞记录
     * @Auther: gchong3068
     * @Date: 2025/6/11 22:36
     * @param userId
     * @param noteIds
     * @return java.util.List<com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteLikeDO>
     **/
    List<NoteLikeDO> selectByUserIdAndNoteIds(@Param("userId") Long userId,
                                              @Param("noteIds") List<Long> noteIds);

    int selectTotalCountByUserId(@Param("userId") Long userId);

    List<Long> selectPageListByUserId(@Param("userId") Long userId,
                                      @Param("offset") long offset,
                                      @Param("pageSize") long pageSize);
}