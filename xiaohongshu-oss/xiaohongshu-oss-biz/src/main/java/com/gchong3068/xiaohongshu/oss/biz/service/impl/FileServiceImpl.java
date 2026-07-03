package com.gchong3068.xiaohongshu.oss.biz.service.impl;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.oss.biz.service.FileService;
import com.gchong3068.xiaohongshu.oss.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件上传实现类
 * @Author: gchong3068
 * @Date: 2024/6/28 12:25
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;

    private static final String BUCKET_NAME = "xiaohongshu";


    @Override
    public Response<?> uploadFile(MultipartFile file) {
        String url = fileStrategy.updateFile(file, BUCKET_NAME);

        return Response.success(url);
    }
}
