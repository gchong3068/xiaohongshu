package com.gchong3068.xiaohongshu.auth.sms;

import com.zhenzi.sms.ZhenziSmsClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gc
 * @date 2024年05月30日 14:58
 */
@Data
@Component
@ConfigurationProperties(prefix = "zhenzi")
public class ZhenziyunSmsHelper {

    private  String apiUrl;          //apiUrl
    private  String appId;           //应用id
    private  String appSecret;       //应用secret
    private  String templateId;      //模板id


    public void sendSMS(String telNumber, String verificationCode) throws Exception{

        ZhenziSmsClient client = new ZhenziSmsClient(apiUrl,appId,appSecret);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("number",telNumber);
        params.put("templateId",templateId);

        String[] templateParams = new String[2];
        templateParams[0] = verificationCode;
        templateParams[1] = "3";
        params.put("templateParams", templateParams);
        client.send(params);
    }
}
