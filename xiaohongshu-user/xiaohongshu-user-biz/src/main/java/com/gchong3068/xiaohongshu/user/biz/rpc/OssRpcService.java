package com.gchong3068.xiaohongshu.user.biz.rpc;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.oss.api.FileFeignApi;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 对象储存服务调用
 * @Author: gchong3068
 * @Date: 2024/7/5 20:35
 * @Version: v1.0.0
 **/
@Component
public class OssRpcService {

    @Resource
    private FileFeignApi fileFeignApi;


    public String uploadFile(MultipartFile file){
        Response<?> response = fileFeignApi.uploadFile(file);

        if (!response.isSuccess()){
            return null;
        }
        return (String) response.getData();
    }
}
