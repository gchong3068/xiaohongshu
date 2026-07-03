package com.gchong3068.xiaohongshu.oss.biz.controller;

import com.gchong3068.framework.biz.context.holder.LoginUserContextHolder;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.oss.biz.service.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 文件控制器
 * @Author: gchong3068
 * @Date: 2024/6/28 12:27
 * @Version: v1.0.0
 **/
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;


    @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> uploadFile(@RequestPart(value = "file" )MultipartFile file){
        log.info("当前用户 ID: {}", LoginUserContextHolder.getUserId());
        return fileService.uploadFile(file);
    }
}
