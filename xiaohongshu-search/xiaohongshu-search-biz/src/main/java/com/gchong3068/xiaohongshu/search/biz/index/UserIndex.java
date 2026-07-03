package com.gchong3068.xiaohongshu.search.biz.index;

/**
 * @Description: 用户索引
 * @Author: gchong3068
 * @Date: 2024/12/4 16:18
 * @Version: v1.0.0
 **/
public class UserIndex {

    /**
     * 索引名称
     */
    public static final String NAME = "user";

    /**
     * 用户ID
     */
    public static final String FIELD_USER_ID = "id";

    /**
     * 昵称
     */
    public static final String FIELD_USER_NICKNAME = "nickname";

    /**
     * 头像
     */
    public static final String FIELD_USER_AVATAR = "avatar";

    /**
     * 小哈书ID
     */
    public static final String FIELD_USER_XIAOHONGSHU_ID = "xiaohongshu_id";

    /**
     * 发布笔记总数
     */
    public static final String FIELD_USER_NOTE_TOTAL = "note_total";

    /**
     * 粉丝总数
     */
    public static final String FIELD_USER_FANS_TOTAL = "fans_total";

}
