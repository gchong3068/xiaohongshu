package com.gchong3068.xiaohongshu.note.biz.constant;

/**
 * @Description: Redis 缓存 Key
 * @Author: gchong3068
 * @Date: 2024/8/23 22:04
 * @Version: v1.0.0
 **/
public class RedisKeyConstants {

    /**
     * 前缀
     */
    public static final String NOTE_DETAIL_KEY = "note:detail:";
    /**
     * 布隆过滤器：用户笔记点赞
     */
    public static final String BLOOM_USER_NOTE_LIKE_LIST_KEY = "bloom:note:likes:";
    
    /**
     * 用户笔记点赞列表 ZSet 前缀
     **/
    public static final String USER_NOTE_LIKE_ZSET_KEY = "user:note:likes:";


    public static final String BLOOM_USER_NOTE_COLLECT_LIST_KEY = "bloom:note:collects:";

    /**
     * 用户笔记收藏列表 ZSet 前缀
     */
    public static final String USER_NOTE_COLLECT_ZSET_KEY = "user:note:collects:";

    /**
     * Roaring Bitmap：用户笔记点赞 前缀
     */
    public static final String R_BITMAP_USER_NOTE_LIKE_LIST_KEY = "rbitmap:note:likes:";

    /**
     * Roaring Bitmap：用户笔记收藏 前缀
     */
    public static final String R_BITMAP_USER_NOTE_COLLECT_LIST_KEY = "rbitmap:note:collects:";


    /**
     * 已发布笔记列表 Key 前缀
     */
    private static final String PUBLISHED_NOTE_LIST_KEY = "note:published:list:";




    /**
     * 构建完整 KEY
     * @Auther: gchong3068
     * @Date: 2024/8/23 22:06
 	 * @param noteId 笔记ID
 	 * @return java.lang.String
     **/
    public static String buildNoteDetailKey(Long noteId){
        return NOTE_DETAIL_KEY + noteId;
    }

    /**
     * 构建完整的布隆过滤器：用户笔记点赞 KEY
     * @Auther: gchong3068
     * @Date: 2024/10/17 10:34 
 	 * @param userId 用户ID
 	 * @return java.lang.String
     **/
    public static String buildBloomUserNoteLikeListKey(Long userId){
        return BLOOM_USER_NOTE_LIKE_LIST_KEY + userId;
    }

    /**
     * 构建完整的用户笔记点赞列表 ZSet KEY
     * @Auther: gchong3068
     * @Date: 2024/10/18 17:40 
 	 * @param userId 用户ID
 	 * @return java.lang.String
     **/
    public static String buildUserNoteLikeZSetKey(Long userId){
        return USER_NOTE_LIKE_ZSET_KEY + userId;
    }


    /**
     * 构建完整的布隆过滤器：用户笔记收藏 KEY
     * @Auther: gchong3068
     * @Date: 2024/10/27 21:29 
     * @param userId 用户ID
     * @return java.lang.String
     **/
    public static String buildBloomUserNoteCollectListKey(Long userId){
        return BLOOM_USER_NOTE_COLLECT_LIST_KEY + userId;
    }

    /**
     * 构建完整的用户笔记收藏列表 ZSet KEY
     * @param userId 用户ID
     */
    public static String buildUserNoteCollectZSetKey(Long userId) {
        return USER_NOTE_COLLECT_ZSET_KEY + userId;
    }


    /**
     * 构建完整 Roaring Bitmap：用户笔记点赞 KEY
     * @Auther: gchong3068
     * @Date: 2025/6/3 13:20
     * @param userId
     * @return java.lang.String
     **/
    public static String buildRBitmapUserNoteLikeListKey(Long userId){
        return R_BITMAP_USER_NOTE_LIKE_LIST_KEY + userId;
    }

    /**
     * 构建完整的 Roaring Bitmap：用户笔记收藏 KEY
     * @Auther: gchong3068
     * @Date: 2025/6/3 13:37
     * @param userId
     * @return java.lang.String
     **/
    public static String buildRBitmapUserNoteCollectListKey(Long userId){
        return R_BITMAP_USER_NOTE_COLLECT_LIST_KEY + userId;
    }

    /**
     * 构建完整的已发布笔记列表 KEY
     * @Auther: gchong3068
     * @Date: 2025/6/11 21:58
     * @param userId
     * @return java.lang.String
     **/
    public static String buildPublishedNoteListKey(Long userId){
        return PUBLISHED_NOTE_LIST_KEY + userId;
    }

}
