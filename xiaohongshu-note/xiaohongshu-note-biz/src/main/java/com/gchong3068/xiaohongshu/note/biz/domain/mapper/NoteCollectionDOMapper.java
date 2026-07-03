package com.gchong3068.xiaohongshu.note.biz.domain.mapper;

import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteCollectionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NoteCollectionDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoteCollectionDO record);

    int insertSelective(NoteCollectionDO record);

    NoteCollectionDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoteCollectionDO record);

    int updateByPrimaryKey(NoteCollectionDO record);

    int selectCountByUserIdAndNoteId(@Param("userId") Long userId,@Param("noteId") Long noteId);

    List<NoteCollectionDO> selectByUserId(Long userId);
    
    /**
     * 查询笔记是否已经被收藏
     * @Auther: gchong3068
     * @Date: 2024/10/27 22:25
 	 * @param userId 用户Id
 	 * @param noteId 笔记Id
 	 * @return int
     **/
    int selectNoteIsCollected(@Param("userId") Long userId,@Param("noteId") Long noteId);

    /**
     * 分页查询用户收藏的笔记
     * @Auther: gchong3068
     * @Date: 2024/10/31 14:14 
     * @param userId 用户Id
     * @param limit 分页大小
     * @return java.util.List<com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteCollectionDO>
     **/
    List<NoteCollectionDO> selectCollectedByUserIdAndLimit(@Param("userId") Long userId,@Param("limit") int limit);
    
    /**
     * 新增笔记收藏记录
     * @Auther: gchong3068
     * @Date: 2024/10/31 14:15 
 	 * @param noteCollectionDO 收藏记录
 	 * @return int
     **/
    int insertOrUpdate(NoteCollectionDO noteCollectionDO);

    /**
     * 取消点赞
     * @Auther: gchong3068
     * @Date: 2024/10/31 14:54
 	 * @param noteCollectionDO 收藏记录
 	 * @return int
     **/
    int update2UnCollectByUserIdAndNoteId(NoteCollectionDO noteCollectionDO);


    int selectTotalCountByUserId(@Param("userId") Long userId);

    List<Long> selectPageListByUserId(@Param("userId") Long userId,
                                      @Param("offset") long offset,
                                      @Param("pageSize") long pageSize);
    


}