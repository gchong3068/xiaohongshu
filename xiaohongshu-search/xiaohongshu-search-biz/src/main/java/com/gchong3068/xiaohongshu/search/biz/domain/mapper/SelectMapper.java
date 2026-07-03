package com.gchong3068.xiaohongshu.search.biz.domain.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description: 查询
 * @Author: gchong3068
 * @Date: 2024/12/17 18:21
 * @Version: v1.0.0
 **/
public interface SelectMapper {
    
    /**
     * 查询笔记文档所需全字段数据
     * @Auther: gchong3068
     * @Date: 2024/12/17 18:25 
 	 * @param noteId 笔记id
 	 * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String,Object>> selectEsNoteIndexData(@Param("noteId")Long noteId , @Param("userId") Long userId);

    /**
     * 查询用户索引所需全字段
     * @Auther: gchong3068
     * @Date: 2024/12/18 13:46 
     * @param userId 用户ID
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     **/
    List<Map<String,Object>> selectEsUserIndexDAta(@Param("userId") Long userId);
    
    
    
}
