package com.gchong3068.xiaohongshu.data.align.domain.mapper;

/**
 * @Description: 删除表
 * @Author: gchong3068
 * @Date: 2024/11/12 18:51
 * @Version: v1.0.0
 **/
public interface DeleteTableMapper {

    

    /**
     * 删除日增量表：关注数计数变更
     * @Auther: gchong3068
     * @Date: 2024/11/12 18:53 
 	 * @param tableNameSuffix 表后缀
     **/
    void deleteDataAlignFollowingCountTempTable(String tableNameSuffix);
    /**
     * 删除日增量表：粉丝数计数变更
     * @Auther: gchong3068
     * @Date: 2024/11/12 18:53 
 	 * @param tableNameSuffix 表后缀
     **/
    void deleteDataAlignFansCountTempTable(String tableNameSuffix);

    /**
     * 删除日增量表：笔记收藏数计数变更
     * @Auther: gchong3068
     * @Date: 2024/11/12 18:53
     * @param tableNameSuffix 表后缀
     **/
    void deleteDataAlignNoteCollectCountTempTable(String tableNameSuffix);
    /**
     * 删除日增量表：用户被收藏数计数变更
     * @Auther: gchong3068
     * @Date: 2024/11/12 18:53
     * @param tableNameSuffix 表后缀
     **/
    void deleteDataAlignUserCollectCountTempTable(String tableNameSuffix);
    /**
     * 删除日增量表：用户被点赞数计数变更
     * @Auther: gchong3068
     * @Date: 2024/11/12 18:53
     * @param tableNameSuffix 表后缀
     **/
    void deleteDataAlignUserLikeCountTempTable(String tableNameSuffix);
    /**
     * 删除日增量表：笔记点赞数计数变更
     * @Auther: gchong3068
     * @Date: 2024/11/12 18:54
     * @param tableNameSuffix 表后缀
     **/
    void deleteDataAlignNoteLikeCountTempTable(String tableNameSuffix);
    /**
     * 删除日增量表：笔记发布数计数变更
     * @Auther: gchong3068
     * @Date: 2024/11/12 18:54
     * @param tableNameSuffix 表后缀
     **/
    void deleteDataAlignNotePublishCountTempTable(String tableNameSuffix);

}
