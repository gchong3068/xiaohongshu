package com.gchong3068.xiaohongshu.note.biz.service;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.note.biz.model.vo.*;

/**
 * @Description: 笔记业务接口
 * @Author: gchong3068
 * @Date: 2024/8/17 11:13
 * @Version: v1.0.0
 **/
public interface NoteService {

    /**
     * 笔记发布
     * @Auther: gchong3068
     * @Date: 2024/8/17 11:14
     * @param publishNoteReqVO 发布笔记请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> publishNote(PublishNoteReqVO publishNoteReqVO);
    
    /**
     * 笔记详情
     * @Auther: gchong3068
     * @Date: 2024/8/22 22:12 
 	 * @param findNoteDetailReqVO 查询笔记详情请求参数
 	 * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.note.biz.model.vo.FindNoteDetailRspVO>
     **/
    Response<FindNoteDetailRspVO> findNoteDetail(FindNoteDetailReqVO findNoteDetailReqVO);


    /**
     * 更新笔记
     * @Auther: gchong3068
     * @Date: 2024/8/27 12:29
     * @param updateNoteReqVO 更新笔记请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> updateNote(UpdateNoteReqVO updateNoteReqVO);


    /**
     * 删除本地笔记
     * @Auther: gchong3068
     * @Date: 2024/8/31 18:57
     * @param noteId 笔记ID
     **/
    void deleteNoteLocalCache(Long noteId);

    /**
     * 删除笔记
     * @Auther: gchong3068
     * @Date: 2024/8/31 19:07
     * @param deleteNoteReqVO 删除笔记请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> deleteNote(DeleteNoteReqVO deleteNoteReqVO);


    /**
     * 笔记仅对自己可见
     * @Auther: gchong3068
     * @Date: 2024/9/1 21:10 
     * @param updateNoteVisibleOnlyMeReqVO 更新笔记仅自己可见请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> visibleOnlyMe(UpdateNoteVisibleOnlyMeReqVO updateNoteVisibleOnlyMeReqVO);

    /**
     * 笔记置顶/取消置顶
     * @Auther: gchong3068
     * @Date: 2024/9/3 21:10
 	 * @param topNoteReqVO 置顶笔记请求参数
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> topNote(TopNoteReqVO topNoteReqVO);

    /**
     * 点赞笔记
     * @Auther: gchong3068
     * @Date: 2024/10/17 10:20
 	 * @param likeNoteReqVO 点赞笔记请求参数
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> likeNote(LikeNoteReqVO likeNoteReqVO);


    /**
     * 取消点赞
     * @Auther: gchong3068
     * @Date: 2024/10/22 20:44
 	 * @param unlikeNoteReqVO 取消点赞请求参数
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> unlikeNote(UnlikeNoteReqVO unlikeNoteReqVO);

    /**
     * 收藏笔记
     * @Auther: gchong3068
     * @Date: 2024/10/27 21:25
 	 * @param collectNoteReqVO 收藏笔记请求参数
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> collectNote(CollectNoteReqVO collectNoteReqVO);

    /**
     * 取消收藏笔记
     * @Auther: gchong3068
     * @Date: 2024/10/31 14:24
     * @param unCollectNoteReqVO 取消收藏笔记请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> unCollectNote(UnCollectNoteReqVO unCollectNoteReqVO);


    /**
     * 获取是否点赞、收藏数据
     * @Auther: gchong3068
     * @Date: 2025/6/5 16:18 
 	 * @param findNoteISLikedAndCollectedReqVO
 	 * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.note.biz.model.vo.FindNoteIsLikedAndCollectedRspVO>
     **/
    Response<FindNoteIsLikedAndCollectedRspVO> isLikedAndCollectedData (FindNoteISLikedAndCollectedReqVO findNoteISLikedAndCollectedReqVO);
    
    /**
     * 用户主页 - 查询已发布的笔记列表
     * @Auther: gchong3068
     * @Date: 2025/6/10 18:33 
     * @param findPublishedNoteListReqVO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.note.biz.model.vo.FindPublishedNoteListRspVO>
     **/
    Response<FindPublishedNoteListRspVO> findPublishedNoteList(FindPublishedNoteListReqVO findPublishedNoteListReqVO);
    
    
}
