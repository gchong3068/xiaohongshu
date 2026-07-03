package com.gchong3068.xiaohongshu.data.align.domain.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 查询
 * @Author: gchong3068
 * @Date: 2024/11/15 18:14
 * @Version: v1.0.0
 **/
public interface SelectMapper {


    /**
     * 日增量表：关注数计数变更 - 批量查询
     * @param tableNameSuffix 后缀
     * @param batchSize 查询数量
     */
    List<Long> selectBatchFromDataAlignFollowingCountTempTable(@Param("tableNameSuffix") String tableNameSuffix,
                                                               @Param("batchSize") int batchSize);

    /**
     * 查询 t_following 关注表，获取关注总数
     * @param userId 用户ID
     */
    int selectCountFromFollowingTableByUserId(long userId);

    /**
     * 日增量表：粉丝数计数变更 - 批量查询
     */
    List<Long> selectBatchFromDataAlignFansCountTempTable(@Param("tableNameSuffix") String tableNameSuffix,
                                                          @Param("batchSize") int batchSize);

    /**
     * 查询 t_fans 粉丝表，获取粉丝总数
     */
    int selectCountFromFansTableByUserId(long userId);

    /**
     * 日增量表：用户获得的点赞数计数变更 - 批量查询
     */
    List<Long> selectBatchFromDataAlignUserLikeCountTempTable(@Param("tableNameSuffix") String tableNameSuffix,
                                                              @Param("batchSize") int batchSize);

    /**
     * 查询 t_note_like 笔记点赞表，获取用户获得的点赞总数
     */
    int selectUserLikeCountFromNoteLikeTableByUserId(long userId);

    /**
     * 日增量表：用户获得的收藏数计数变更 - 批量查询
     */
    List<Long> selectBatchFromDataAlignUserCollectCountTempTable(@Param("tableNameSuffix") String tableNameSuffix,
                                                                 @Param("batchSize") int batchSize);

    /**
     * 查询 t_note_collection 笔记收藏表，获取用户获得的收藏总数
     */
    int selectUserCollectCountFromNoteCollectionTableByUserId(long userId);

    /**
     * 日增量表：笔记点赞数变更 - 批量查询
     * @param tableNameSuffix 后缀
     */
    List<Long> selectBatchFromDataAlignNoteLikeCountTempTable(@Param("tableNameSuffix") String tableNameSuffix,
                                                              @Param("batchSize") int batchSize);

    /**
     * 查询 t_note_like 笔记点赞表，获取点赞总数
     * @param noteId 笔记ID
     */
    int selectCountFromNoteLikeTableByUserId(long noteId);

    /**
     * 日增量表：笔记发布数变更 - 批量查询
     * @param tableNameSuffix 后缀
     */
    List<Long> selectBatchFromDataAlignNotePublishCountTempTable(@Param("tableNameSuffix") String tableNameSuffix,
                                                                 @Param("batchSize") int batchSize);

    /**
     * 查询 t_note 笔记表，获取用户发布的笔记总数
     */
    int selectCountFromNoteTableByUserId(long userId);

    /**
     * 日增量表：笔记收藏数变更 - 批量查询
     * @param tableNameSuffix 后缀
     * @param batchSize 查询数量
     */
    List<Long> selectBatchFromDataAlignNoteCollectCountTempTable(@Param("tableNameSuffix") String tableNameSuffix,
                                                                 @Param("batchSize") int batchSize);

    /**
     * 查询 t_note_collection 笔记收藏表，获取收藏总数
     * @param noteId 笔记ID
     */
    int selectCountFromNoteCollectionTableByUserId(long noteId);
}
