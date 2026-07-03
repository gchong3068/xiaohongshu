package com.gchong3068.xiaohongshu.search.biz.service.impl;

import com.gchong3068.xiaohongshu.search.biz.service.ExtDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * @Description: Elasticsearch 词典热部署
 * @Author: gchong3068
 * @Date: 2024/12/12 18:39
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class ExtDictServiceImpl implements ExtDictService {

    @Value("${elasticsearch.hotUpdateExtDict}")
    private String hotUpdateExtDict;

    /**
     * 获取热更新词典
     * @Auther: gchong3068
     * @Date: 2024/12/12 18:48
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     **/
    @Override
    public ResponseEntity<String> getHotUpdateExtDict() {
        try{
            Path path = Paths.get(hotUpdateExtDict);
            long lastModifiedTime = Files.getLastModifiedTime(path).toMillis();

            // 生成 ETag（使用文件内容的哈希值）
            String fileContent = Files.lines(path).collect(Collectors.joining("\n"));
            String eTag = String.valueOf(fileContent.hashCode());

            // 设置响应头
            HttpHeaders headers  = new HttpHeaders();
            headers.set("ETag", eTag);

            //设置内容类型
            headers.setContentType(MediaType.valueOf("text/plain;charset=UTF-8"));

            //返回文件内容和head头部
            return ResponseEntity.ok()
                    .headers(headers)
                    .lastModified(lastModifiedTime)
                    .body(fileContent);

        }catch (Exception e){
            log.error("==> 获取字典异常",e);
        }
        return null;
    }
}
