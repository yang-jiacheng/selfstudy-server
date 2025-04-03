package com.lxy.common.util;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static com.lxy.common.properties.AliYunProperties.*;

/**
 * 阿里云短信
 * @author jiacheng yang.
 * @since 2022/12/19 11:41
 * @version 1.0
 */
public class SmsUtil {

    private final static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

    private static Client clientInstance = null;

    private final static String REGION = "cn-qingdao";

    private final static String ENDPOINT = "dysmsapi.aliyuncs.com";

    private final static String TEMPLATE_CODE = "SMS_269495080";

    private final static String SIGN_NAME = "团团云自习";

    public static Client getClient(){
        //新sdk
        if (clientInstance == null) {
            Config config = new Config();
            config.setAccessKeyId(accessKeyId);
            config.setAccessKeySecret(accessKeySecret);
            config.setRegionId(REGION);
            config.setEndpoint(ENDPOINT);
            try {
                clientInstance = new Client(config);
            }catch (Exception e) {
                clientInstance = null;
                logger.error("短信客户端初始化失败",e);
            }
        }
        return clientInstance;
    }

    /**
     * 发送短信
     * @param phoneNumbers 手机号
     * @param param 验证码
     */
    public static boolean sendMessage(String phoneNumbers,String param){
        boolean flag = false;
        Client client = getClient();
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(SIGN_NAME);
        request.setTemplateCode(TEMPLATE_CODE);
        request.setTemplateParam("{\"code\":\""+param+"\"}");
        try {

            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsResponse response = client.sendSmsWithOptions(request, runtime);
            String code = response.getBody().getCode();
            if (code.equals("OK")){
                flag = true;
                logger.info("短信发送成功，手机号：{},验证码：{}", phoneNumbers, param);
            }
        }catch (Exception e) {
            logger.error("短信发送失败",e);
        }
        return flag;
    }

//    public static void querySmsTemplateList(){
//        IAcsClient client = getClient();
//        QuerySmsTemplateListRequest request = new QuerySmsTemplateListRequest();
//        request.setPageIndex(1).setPageSize(50);
//        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
//        try {
//            client.querySmsTemplateListWithOptions(querySmsTemplateListRequest, runtime);
//
//        }catch (Exception e) {
//            logger.error("查询短信模板失败",e);
//        }
//    }

    public static String getRandomCode(){
        Random random = new Random();
        int ranNum = (int) (random.nextDouble() * (999999 - 100000 + 1)) + 100000;
        return String.valueOf(ranNum);
    }

    public static void main(String[] args) {
        logger.error("error");
        logger.debug("debug");
    }

}
