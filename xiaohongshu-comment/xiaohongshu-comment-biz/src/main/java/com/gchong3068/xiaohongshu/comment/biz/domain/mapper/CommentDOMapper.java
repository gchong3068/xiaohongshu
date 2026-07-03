package com.gchong3068.xiaohongshu.comment.biz.domain.mapper;

import com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO;
import com.gchong3068.xiaohongshu.comment.biz.model.bo.CommentBO;
import com.gchong3068.xiaohongshu.comment.biz.model.bo.CommentHeatBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommentDO record);

    int insertSelective(CommentDO record);

    CommentDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentDO record);

    int updateByPrimaryKey(CommentDO record);

    /**
     * 根据评论Id批量查询
     * @Auther: gchong3068
     * @Date: 2025/1/10 16:23
 	 * @param commentIds 评论Id集合
 	 * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO>
     **/
    List<CommentDO> selectByCommentIds(@Param("commentIds") List<Long> commentIds);

    /**
     * 批量插入评论
     * @Auther: gchong3068
     * @Date: 2025/1/10 17:03
     * @param comments 评论集合
     * @return int
     **/
    int batchInsert(@Param("comments") List<CommentBO> comments);

    int batchUpdateHeatByCommentIds(@Param("commentIds") List<Long> commentIds,
                                    @Param("commentHeatBOS") List<CommentHeatBO> commentHeatBOS);
    
    /**
     * 查询一级评论下最早回复评论
     * @Auther: gchong3068
     * @Date: 2025/2/9 20:28 
 	 * @param parentId Id
 	 * @return com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO
     **/
    CommentDO selectEarliestByParentId(Long parentId);

    /**
     * 更新一级评论的 first_reply_comment_id
     * @Auther: gchong3068
     * @Date: 2025/2/9 20:29
 	 * @param firstReplyCommentId firstReplyCommentId
 	 * @param id id
 	 * @return int
     **/
    int updateFirstReplyCommentIdByPrimaryKey(@Param("firstReplyCommentId") Long firstReplyCommentId,
                                              @Param("id") Long id);

    /**
     * 批量查询二级评论
     * @Auther: gchong3068
     * @Date: 2025/4/1 16:51
 	 * @param commentIds
 	 * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO>
     **/
    List<CommentDO> selectTwoLevelCommentByIds(@Param("commentIds") List<Long> commentIds);

    /**
     * 查询评论分页数据
     * @Auther: gchong3068
     * @Date: 2025/4/1 16:49
     * @param noteId
     * @param offset
     * @param pageSize
     * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO>
     **/
    List<CommentDO> selectPageList(@Param("noteID") Long noteId,
                                   @Param("offset") long offset,
                                   @Param("pageSize")long pageSize);

    /**
     * 查询热门评论
     * @Auther: gchong3068
     * @Date: 2025/4/1 17:45
     * @param noteId
     * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO>
     **/
    List<CommentDO> selectHeatComments(Long noteId);
    
    /**
     * 查询一级评论下子评论总数
     * @Auther: gchong3068
     * @Date: 2025/4/22 17:35
 	 * @param commentId
 	 * @return java.lang.Long
     **/
    Long selectChildCommentTotalById(Long commentId);

    /**
     * 查询二级评论分页数据
     * @Auther: gchong3068
     * @Date: 2025/4/22 17:39
     * @param parentId
     * @param offset
     * @param pageSize
     * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO>
     **/
    List<CommentDO> selectChildPageList(@Param("parentId") Long parentId,
                                        @Param("offset") long offset,
                                        @Param("pageSize") long pageSize);

    /**
     * 批量查询计数数据
     * @Auther: gchong3068
     * @Date: 2025/4/22 20:25
     * @param commentIds
     * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO>
     **/
    List<CommentDO> selectCommentCountByIds(@Param("commentIds") List<Long> commentIds);

    /**
     * 查询子评论
     * @Auther: gchong3068
     * @Date: 2025/4/23 17:35
 	 * @param parentId
 	 * @param limit
 	 * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentDO>
     **/
    List<CommentDO> selectChildCommentsByParentIdAndLimit(@Param("parentId") Long parentId,
                                                          @Param("limit") int limit);
    /**
     * 删除一级评论下，所有二级评论
     * @Auther: gchong3068
     * @Date: 2025/6/2 21:54 
     * @param commentId
     * @return int
     **/
    int deleteByParentId(Long commentId);

    /**
     * 批量删除评论
     * @param commentIds
     * @return
     */
    int deleteByIds(@Param("commentIds") List<Long> commentIds);


    /**
     * 根据 reply_comment_id 查询
     * @param commentId
     * @return
     */
    CommentDO selectByReplyCommentId(Long commentId);
}