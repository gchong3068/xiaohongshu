package com.gchong3068.xiaohongshu.data.align.domain.mapper;

/**
 * @Description: 自动创建表
 * @Author: gchong3068
 * @Date: 2024/11/8 15:11
 * @Version: v1.0.0
 **/
public interface CreateTableMapper {

    /**
     * 创建日增量表，关注数计数更新
     * @Auther: gchong3068
     * @Date: 2024/11/8 15:14 
     * @param tableNameSuffix 表名后缀
     **/
    void createDataAlignFollowingCountTempTable(String tableNameSuffix);

    /**
     * 创建日增量表：粉丝数计数变更
     */
    void createDataAlignFansCountTempTable(String tableNameSuffix);

    /**
     * 创建日增量表：笔记收藏数计数变更
     */
    void createDataAlignNoteCollectCountTempTable(String tableNameSuffix);

    /**
     * 创建日增量表：用户被收藏数计数变更
     */
    void createDataAlignUserCollectCountTempTable(String tableNameSuffix);

    /**
     * 创建日增量表：用户被点赞数计数变更
     */
    void createDataAlignUserLikeCountTempTable(String tableNameSuffix);

    /**
     * 创建日增量表：笔记点赞数计数变更
     */
    void createDataAlignNoteLikeCountTempTable(String tableNameSuffix);

    /**
     * 创建日增量表：笔记发布数计数变更
     */
    void createDataAlignNotePublishCountTempTable(String tableNameSuffix);
}
