package com.gchong3068.xiaohongshu.oss.biz.strategy.impl;

import com.gchong3068.xiaohongshu.oss.biz.config.MinioProperties;
import com.gchong3068.xiaohongshu.oss.biz.strategy.FileStrategy;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @Description: Minio 文件策略实现
 * @Author: gchong3068
 * @Date: 2024/6/28 12:11
 * @Version: v1.0.0
 **/
@Slf4j
public class MinioFileStrategy implements FileStrategy {


    @Resource
    private MinioProperties minioProperties;

    @Resource
    private MinioClient minioClient;


    @Override
    @SneakyThrows
    public String updateFile(MultipartFile file, String bucketName) {
        log.info("## 上传文件至 Minio ...");

        if (file == null || file.getSize() == 0){
            log.error("==> 上传文件异常：文件大小为空 ...");
            throw new RuntimeException("文件大小不能为空");
        }
        //原始名称
        String originalFilename = file.getOriginalFilename();

        //Content-Type
        String contentType = file.getContentType();
        String key = UUID.randomUUID().toString().replace("-", "");
        //文件类型
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //拼接文件后缀
        String objectName = String.format("%s%s",key,suffix);

        log.info("==> 开始上传文件至 Minio, ObjectName: {}", objectName);

        //上传
        minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(contentType)
                .build());

        String url = String.format("%s/%s/%s",minioProperties.getEndpoint(),bucketName,objectName);
        log.info("==> 上传文件至 Minio 成功，访问路径: {}", url);
        return url;
    }
}
