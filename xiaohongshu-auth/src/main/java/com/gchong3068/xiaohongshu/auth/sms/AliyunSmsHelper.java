package com.gchong3068.xiaohongshu.auth.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author gc
 * @date 2024年05月24日 19:27
 */
@Slf4j
@Component
public class AliyunSmsHelper {

    @Resource
    private Client client;


    /**
     * 发送短信
     * @author gc
     * @date 2024/5/24 19:34
     * @return boolean*/

    public boolean sendMessage(String signName, String templateCode, String phone , String templateParam){

        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phone)
                .setTemplateParam(templateParam);
        RuntimeOptions runtime = new RuntimeOptions();

        try {
            log.info("==> 开始短信发送, phone: {}, signName: {}, templateCode: {}, templateParam: {}", phone, signName, templateCode, templateParam);
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
            return true;
        }catch (Exception error){
            log.error("==> 短信发送错误: ", error);
            return false;
        }
    }
}
