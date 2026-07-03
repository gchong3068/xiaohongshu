package com.gchong3068.xiaohongshu.search.biz.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 搜索用户返回结果
 * @Author: gchong3068
 * @Date: 2024/12/4 16:08
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchUserRspVO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 小哈书ID
     */
    private String xiaohongshuId;

    /**
     * 笔记发布总数
     */
    private Integer noteTotal;

    /**
     * 粉丝总数
     */
    private String fansTotal;

    /**
     * 昵称：关键词高亮
     */
    private String highlightNickname;


}
