package com.gchong3068.xiaohongshu.oss.biz.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件策略
 * @Author: gchong3068
 * @Date: 2024/6/28 12:07
 * @Version: v1.0.0
 **/
public interface FileStrategy  {

    /**
     * 文字上传
     * @Auther: gchong3068
     * @Date: 2024/6/28 12:09
 	 * @param file 文件
 	 * @param bucketName 储存桶名称
 	 * @return java.lang.String
     **/
    String updateFile(MultipartFile file, String bucketName);
}
