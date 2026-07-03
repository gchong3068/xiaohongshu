package com.gchong3068.xiaohongshu.oss.api;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.oss.config.FeignFormConfig;
import com.gchong3068.xiaohongshu.oss.constant.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 对象储存Feign接口
 * @Author: gchong3068
 * @Date: 2024/7/4 21:51
 * @Version: v1.0.0
 **/
@FeignClient(name = ApiConstants.SERVICE_NAME,configuration = FeignFormConfig.class)
public interface FileFeignApi {

    String PREFIX = "/file";

    /**
     * 文件上传
     * @Auther: gchong3068
     * @Date: 2024/7/5 20:33
     * @param file 文件
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @PostMapping(value = PREFIX + "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<?> uploadFile(@RequestPart(value = "file")MultipartFile file);
}
