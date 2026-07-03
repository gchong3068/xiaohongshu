package com.gchong3068.xiaohongshu.user.relation.biz.domain.mapper;

import com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FollowingDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FollowingDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FollowingDO record);

    int insertSelective(FollowingDO record);

    FollowingDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FollowingDO record);

    int updateByPrimaryKey(FollowingDO record);

    List<FollowingDO> selectByUserId(Long userId);

    int deleteByUserIdAndFollowingUserId(@Param("userId") Long userId,
                                         @Param("followingUserId")Long unfollowUserId);
    /**
     * 查询记录总数
     * @Auther: gchong3068
     * @Date: 2024/9/26 22:51 
 	 * @param userId 用户id
 	 * @return long
     **/
    long selectCountByUserId(Long userId);
    /**
     * 分页查询
     * @Auther: gchong3068
     * @Date: 2024/9/26 22:55
 	 * @param userID 用户id
 	 * @param offset 开始位置
 	 * @param limit 每页条数
 	 * @return java.util.List<com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FollowingDO>
     **/
    List<FollowingDO> selectPageListByUserId(@Param("userId") Long userID,
                                             @Param("offset") long offset,
                                             @Param("limit") long limit);
    
    /**
     * 查询关注用户列表
     * @Auther: gchong3068
     * @Date: 2024/9/26 23:14 
 	 * @param userId 用户id
 	 * @return java.util.List<com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FollowingDO>
     **/
    List<FollowingDO> selectAllByUserId(Long userId);
}