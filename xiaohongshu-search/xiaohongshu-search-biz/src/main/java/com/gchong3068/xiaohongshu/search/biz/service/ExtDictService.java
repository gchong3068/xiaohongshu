package com.gchong3068.xiaohongshu.search.biz.service;

import org.springframework.http.ResponseEntity;

/**
 * @Description: Elasticsearch 扩展字典服务
 * @Author: gchong3068
 * @Date: 2024/12/12 18:39
 * @Version: v1.0.0
 **/
public interface ExtDictService {
    
    /**
     * 获取热更新词典
     * @Auther: gchong3068
     * @Date: 2024/12/12 18:40 
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     **/
    ResponseEntity<String> getHotUpdateExtDict();
    
    
}
