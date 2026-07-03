package com.gchong3068.xiaohongshu.count.biz.domain.mapper;

import com.gchong3068.xiaohongshu.count.biz.domain.dataobject.CommentDO;
import org.apache.ibatis.annotations.Param;

public interface CommentDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommentDO record);

    int insertSelective(CommentDO record);

    CommentDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentDO record);

    int updateByPrimaryKey(CommentDO record);

    /*** 
     * 更新一级评论子评论总数
     * @Auther: gchong3068
     * @Date: 2025/1/12 16:11 
 	 * @param parentId 评论ID
 	 * @param count 总数
 	 * @return int
     **/
    int updateChildCommentTotal(@Param("parentId") Long parentId, @Param("count") int count);
    /**
     * 更新评论点赞数
     * @Auther: gchong3068
     * @Date: 2025/6/2 20:47 
     * @param count
     * @param commentId
     * @return int
     **/
    int updateLikeTotalByCommentId(@Param("count") Integer count,
                                   @Param("commentId") Long commentId);
}