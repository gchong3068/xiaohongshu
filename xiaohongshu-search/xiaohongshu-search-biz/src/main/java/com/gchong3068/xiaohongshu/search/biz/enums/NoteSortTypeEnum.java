package com.gchong3068.xiaohongshu.search.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 笔记排序类型
 * @Author: gchong3068
 * @Date: 2024/12/7 22:10
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum NoteSortTypeEnum {

    LATEST(0), //最新
    MOST_LIKE(1),  //最新点赞
    MOST_COMMENT(2), //最多评论
    MOST_COLLECT(3), // 最多收藏
    ;


    private final Integer code;

    /**
     * 根据code获取类型
     * @Auther: gchong3068
     * @Date: 2024/12/7 22:13
 	 * @param code code
 	 * @return com.gchong3068.xiaohongshu.search.enums.NoteSortTypeEnum
     **/
    public static NoteSortTypeEnum valueOf(Integer code){
        for (NoteSortTypeEnum noteSortTypeEnum : NoteSortTypeEnum.values()){
            if (Objects.equals(code,noteSortTypeEnum.getCode())){
                return noteSortTypeEnum;
            }
        }
        return null;
    }

}
