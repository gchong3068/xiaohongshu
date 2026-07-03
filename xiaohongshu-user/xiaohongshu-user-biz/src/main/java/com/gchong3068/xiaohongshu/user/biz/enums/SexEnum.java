package com.gchong3068.xiaohongshu.user.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @Description: 性别
 * @Author: gchong3068
 * @Date: 2024/7/3 19:06
 * @Version: v1.0.0
 **/
@Getter
@AllArgsConstructor
public enum SexEnum {
    WOMAN(0),
    MAN(1);

    private final Integer value;

    public static boolean isValid(Integer value){
        for (SexEnum loginTypeEnum : SexEnum.values()){
            if (Objects.equals(value,loginTypeEnum.getValue())){
                return true;
            }
        }
        return false;
    }
}
