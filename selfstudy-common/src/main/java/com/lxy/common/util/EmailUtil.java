package com.lxy.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.lxy.common.dto.SendEmailDTO;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 邮箱工具类
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Slf4j
public class EmailUtil {

    private final static String MAIL_USER = "1529425632@qq.com";

    private final static String AUTH_CODE = "uvegtokiqsiaihhf";

    private final static String SERVE_HOST = "smtp.qq.com";

    private final static String PERSONAL = "杨嘉诚";

    private final static Integer SERVE_PORT = 587;

    private static JavaMailSenderImpl getJavaMailSenderImpl() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //QQ邮箱的SMTP服务器地址
        mailSender.setHost(SERVE_HOST);
        //QQ邮箱的SMTP服务器端口号
        mailSender.setPort(SERVE_PORT);
        //你的QQ邮箱地址
        mailSender.setUsername(MAIL_USER);
        //你的QQ邮箱授权码
        mailSender.setPassword(AUTH_CODE);

        // 设置JavaMail属性
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");
        return mailSender;
    }

    /**
     * 批量发送邮件
     *
     * @author jiacheng yang.
     * @since 2025/5/14 10:56
     */
    public static void sendEmailBatch(List<SendEmailDTO> dtoList) {
        if (CollUtil.isEmpty(dtoList)) {
            log.warn("邮件列表为空，不进行发送！");
            return;
        }
        JavaMailSenderImpl mailSender = getJavaMailSenderImpl();
        AtomicInteger okCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();
        // 发送邮件
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (SendEmailDTO dto : dtoList) {
            String recipient = dto.getRecipient();
            String subject = dto.getSubject();
            String body = dto.getBody();
            String msg = StrUtil.format("收件人：{},主题：{},内容：{}", recipient, subject, body);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                try {
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
                    helper.setFrom(MAIL_USER, PERSONAL);
                    helper.setTo(recipient);
                    helper.setSubject(subject);
                    helper.setText(body, true);
                    mailSender.send(mimeMessage);
                    okCount.incrementAndGet();
                    log.info("邮件发送成功！,{}", msg);
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    log.error("邮件发送失败！,{}", msg, e);
                }
            }, ThreadPoolUtil.getInstance());
            futures.add(future);
        }

        // 任务完成后进行回调
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allOf.thenRun(() -> {
            log.info("邮件发送完成！成功{}条，失败{}条", okCount.get(), failCount.get());
        });

    }

    public static void main(String[] args) {


        List<SendEmailDTO> dtoList = new ArrayList<>();
        SendEmailDTO dto1 = new SendEmailDTO("yjc1529425632@gmail.com", "SSL证书通知", "<p>尊敬的览山科技-杨嘉诚：<br>您为域名<a href=\"http://oss.zhiyemao.net\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=http://oss.zhiyemao.net&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw0VLcdHLl8Pcx5CtB5ERPsm\">oss.zhiyemao.net</a>购买的SSL证书已签<wbr>发成功，<wbr>现可前往证书控制台进行下载并安装至Web服务器或一键部署到阿<wbr>里云云产品。具体操作步骤如下：<br><br>1、点击下方按钮，进入证书产品控制台；<br><br>点击进入控制台   <a href=\"https://yundun.console.aliyun.com/?p=cas\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://yundun.console.aliyun.com/?p%3Dcas&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw3xDMl8N_Anm-nlyBehnj7i\">https://yundun.console.aliyun.<wbr>com/?p=cas</a> <br><br>2、<wbr>确定您的证书是需要安装至Web服务器还是部署到阿里云云产品上<wbr>。<br><br>下载并安装至Web服务器，请点击 <a href=\"https://help.aliyun.com/document_detail/198938.html\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://help.aliyun.com/document_detail/198938.html&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw1mMOYGPP6M7THlreTNEbMY\">https://help.aliyun.com/<wbr>document_detail/198938.html</a> <br><br>一键部署到阿里云云产品，请点击 <a href=\"https://help.aliyun.com/document_detail/98575.html\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://help.aliyun.com/document_detail/98575.html&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw3J5VtrJwoZ8MJsy9u1Hscj\">https://help.aliyun.com/<wbr>document_detail/98575.html</a> <br></p>");
        SendEmailDTO dto2 = new SendEmailDTO("29922664036@gmail.com", "查询通知", "<p>尊敬的览山科技-杨嘉诚：<br>您为域名<a href=\"http://oss.zhiyemao.net\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=http://oss.zhiyemao.net&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw0VLcdHLl8Pcx5CtB5ERPsm\">oss.zhiyemao.net</a>购买的SSL证书已签<wbr>发成功，<wbr>现可前往证书控制台进行下载并安装至Web服务器或一键部署到阿<wbr>里云云产品。具体操作步骤如下：<br><br>1、点击下方按钮，进入证书产品控制台；<br><br>点击进入控制台   <a href=\"https://yundun.console.aliyun.com/?p=cas\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://yundun.console.aliyun.com/?p%3Dcas&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw3xDMl8N_Anm-nlyBehnj7i\">https://yundun.console.aliyun.<wbr>com/?p=cas</a> <br><br>2、<wbr>确定您的证书是需要安装至Web服务器还是部署到阿里云云产品上<wbr>。<br><br>下载并安装至Web服务器，请点击 <a href=\"https://help.aliyun.com/document_detail/198938.html\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://help.aliyun.com/document_detail/198938.html&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw1mMOYGPP6M7THlreTNEbMY\">https://help.aliyun.com/<wbr>document_detail/198938.html</a> <br><br>一键部署到阿里云云产品，请点击 <a href=\"https://help.aliyun.com/document_detail/98575.html\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://help.aliyun.com/document_detail/98575.html&amp;source=gmail&amp;ust=1747279138238000&amp;usg=AOvVaw3J5VtrJwoZ8MJsy9u1Hscj\">https://help.aliyun.com/<wbr>document_detail/98575.html</a> <br></p>");
        dtoList.add(dto1);
        dtoList.add(dto2);
        sendEmailBatch(dtoList);
        ThreadPoolUtil.shutdown();
    }

}
