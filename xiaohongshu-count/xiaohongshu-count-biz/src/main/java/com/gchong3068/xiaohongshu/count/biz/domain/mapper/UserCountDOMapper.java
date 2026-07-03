package com.gchong3068.xiaohongshu.count.biz.domain.mapper;

import com.gchong3068.xiaohongshu.count.biz.domain.dataobject.UserCountDO;
import org.apache.ibatis.annotations.Param;

public interface UserCountDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserCountDO record);

    int insertSelective(UserCountDO record);

    UserCountDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserCountDO record);

    int updateByPrimaryKey(UserCountDO record);

    /**
     * 添加或更新粉丝总数
     * @Auther: gchong3068
     * @Date: 2024/10/11 18:37
 	 * @param count 总数
 	 * @param userId 用户id
 	 * @return int
     **/
    int insertOrUpdateFansTotalByUserId(@Param("count")Integer count , @Param("userId")Long userId);

    /**
     * 添加Or更新关注总数
     * @Auther: gchong3068
     * @Date: 2024/10/13 23:11
 	 * @param count 总数
 	 * @param userId 用户id
 	 * @return int
     **/
    int insertOrUpdateFollowingTotalByUserId(@Param("count")Integer count,@Param("userId")Long userId);


    /**
     * 添加记录或更新笔记点赞数
     * @Auther: gchong3068
     * @Date: 2024/11/2 17:17
 	 * @param count 点赞数
 	 * @param userId 用户id
 	 * @return int
     **/
    int insertOrUpdateLikeTotalByUserId(@Param("count") Integer count,@Param("userId") Long userId);


    /**
     * 添加记录或更新笔记发布数
     * @Auther: gchong3068
     * @Date: 2024/11/2 17:45 
     * @param count 笔记发布数
     * @param userId 用户Id
     * @return int
     **/
    int insertOrUpdateNoteTotalByUserId(@Param("count") Long count, @Param("userId") Long userId);

    /**
     * 根据用户 ID 查询
     * @Auther: gchong3068
     * @Date: 2025/6/5 16:36 
     * @param userId
     * @return com.gchong3068.xiaohongshu.count.biz.domain.dataobject.UserCountDO
     **/
    UserCountDO selectByUserId(Long userId);

}