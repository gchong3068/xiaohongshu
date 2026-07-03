package com.gchong3068.xiaohongshu.user.relation.biz.domain.mapper;

import com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FansDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FansDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FansDO record);

    int insertSelective(FansDO record);

    FansDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FansDO record);

    int updateByPrimaryKey(FansDO record);

    /**
     * 取关
     * @Auther: gchong3068
     * @Date: 2024/9/28 11:38 
     * @param userId 用户ID
     * @param unfollowUserId 取关用户ID
     * @return int
     **/
    int deleteByUserIdAndFansUserId(@Param("userId") Long userId,
                                    @Param("fansUserId") Long unfollowUserId);
    /**
     * 查询记录总数
     * @Auther: gchong3068
     * @Date: 2024/9/28 11:37 
     * @param userId 用户ID
     * @return long
     **/
    long selectCountByUserId(Long userId);
    /**
     * 分页查询
     * @Auther: gchong3068
     * @Date: 2024/9/28 11:42
 	 * @param userId 用户ID
 	 * @param offset 开始位置
 	 * @param limit 每页条数
 	 * @return java.util.List<com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FansDO>
     **/
    List<FansDO> selectPageListByUserId(@Param("userId") Long userId,
                                        @Param("offset") long offset,
                                        @Param("limit") long limit);

    /**
     * 查询最新关注的 5000 位粉丝
     * @Auther: gchong3068
     * @Date: 2024/9/28 12:12
 	 * @param userId 用户ID
 	 * @return java.util.List<com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FansDO>
     **/
    List<FansDO> select5000FansByUserId(Long userId);

}