package com.gchong3068.xiaohongshu.comment.biz.domain.mapper;

import com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentLikeDO;
import com.gchong3068.xiaohongshu.comment.biz.model.dto.LikeUnlikeCommentMqDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentLikeDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CommentLikeDO record);

    int insertSelective(CommentLikeDO record);

    CommentLikeDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentLikeDO record);

    int updateByPrimaryKey(CommentLikeDO record);

    int selectCountByUserIdAndCommentId(@Param("userId") Long userId,
                                        @Param("commentId")Long commentId);
    
    /**
     * 查询对应用户点赞的所有评论
     * @Auther: gchong3068
     * @Date: 2025/5/9 16:45
 	 * @param userId
 	 * @return java.util.List<com.gchong3068.xiaohongshu.comment.biz.domain.dataobject.CommentLikeDO>
     **/
    List<CommentLikeDO> selectByUserId(@Param("userId")Long userId);

    int batchDelete(@Param("unlikes")List<LikeUnlikeCommentMqDTO> unlikes);

    int batchInsert(@Param("likes") List<LikeUnlikeCommentMqDTO> likes);
}