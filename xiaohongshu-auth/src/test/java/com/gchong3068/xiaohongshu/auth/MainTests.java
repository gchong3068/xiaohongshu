package com.gchong3068.xiaohongshu.auth;

import com.gchong3068.framework.common.util.MailHelper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description: 测试
 * @Author: gchong3068
 * @Date: 2024/7/27 17:58
 * @Version: v1.0.0
 **/
@SpringBootTest
public class MainTests {

    @Autowired
    private MailHelper mailHelper;

    String to = "30689514148@qq.com";
    String title = "这是一条测试邮件";
    String html = "测试邮件";

    @Test
    void sendMailTest(){
        boolean b = mailHelper.sendHtml(to, title, html);
        System.out.println("b = " + b);
    }
}
