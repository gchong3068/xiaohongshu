package com.gchong3068.xiaohongshu.note.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 查询发现页笔记
 * @Author: gchong3068
 * @Date: 2025/6/12 17:34
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindDiscoverNoteRspVO {


    /**
     * 笔记 ID
     */
    private String id;

    /**
     * 笔记类型
     */
    private Integer type;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 视频连接
     */
    private String videoUri;

    /**
     * 标题
     */
    private String title;

    /**
     * 发布者用户 ID
     */
    private Long creatorId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 被点赞量
     */
    private String likeTotal;

}
