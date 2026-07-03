package com.gchong3068.xiaohongshu.user.relation.biz.service;

import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.user.relation.biz.model.vo.*;

/**
 * @Description: 关注服务
 * @Author: gchong3068
 * @Date: 2024/9/9 20:56
 * @Version: v1.0.0
 **/
public interface RelationService {

    /**
     * 关注用户
     * @Auther: gchong3068
     * @Date: 2024/9/9 20:57
     * @param followUserReqVO 关注用户请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> follow(FollowUserReqVO followUserReqVO);

    /**
     * 取关用户
     * @Auther: gchong3068
     * @Date: 2024/9/20 22:30
     * @param unfollowUserReqVO 取关用户请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> unfollow(UnfollowUserReqVO unfollowUserReqVO);

    /**
     * 查询关注列表
     * @Auther: gchong3068
     * @Date: 2024/9/25 21:33
     * @param findFollowingListReqVO 查询关注列表请求参数
     * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.user.relation.biz.model.vo.FindFollowingUserRspVO>
     **/
    PageResponse<FindFollowingUserRspVO> findFollowingList(FindFollowingListReqVO findFollowingListReqVO);

    /**
     * 分页查询关注列表
     * @Auther: gchong3068
     * @Date: 2024/9/28 11:45
 	 * @param findFansListReqVO 查询粉丝列表请求参数
 	 * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.user.relation.biz.model.vo.FindFansUserRspVO>
     **/
    PageResponse<FindFansUserRspVO> findFansList(FindFansListReqVO findFansListReqVO);
}
