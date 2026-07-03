package com.gchong3068.framework.common.util;

import jakarta.annotation.Resource;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * @Description: 发送邮件
 * @Author: gchong3068
 * @Date: 2024/7/27 17:45
 * @Version: v1.0.0
 **/

@Component
@Slf4j
public class MailHelper {

    @Resource
    private MailProperties mailProperties;
    @Resource
    private JavaMailSender javaMailSender;

    public boolean sendHtml(String to,String title,String html){
        log.info("==>开始发送邮件。。。。");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = null;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            //发送来源
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            //发送目标
            mimeMessageHelper.setTo(to);
            //标题
            mimeMessageHelper.setSubject(title);
            //内容
            mimeMessageHelper.setText(html,true);

            javaMailSender.send(mimeMessage);
            log.info("==>邮件发送成功。。。。。to:{},title:{},内容:{}",to,title,html);
        } catch (Exception e) {
            log.error("==>发送邮件异常:",e);
            return false;
        }
        return true;
    }
}
