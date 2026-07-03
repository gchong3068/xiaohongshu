package com.gchong3068.xiaohongshu.oss.biz.service;

import com.gchong3068.framework.common.response.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件上传服务
 * @Author: gchong3068
 * @Date: 2024/6/28 12:23
 * @Version: v1.0.0
 **/
public interface FileService {


    /**
     * 上传文件
     * @Auther: gchong3068
     * @Date: 2024/6/28 12:24
     * @param file 文件
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    Response<?> uploadFile(MultipartFile file);
}
