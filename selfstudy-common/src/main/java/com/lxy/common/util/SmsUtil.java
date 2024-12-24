package com.lxy.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.lxy.common.config.properties.CustomProperties;

import java.util.Random;

/**
 * @Description: 阿里云短信
 * @author: jiacheng yang.
 * @Date: 2022/12/19 11:41
 * @Version: 1.0
 */
public class SmsUtil {

    private final static String REGION = "cn-hangzhou";

    private final static String TEMPLATE_CODE = "SMS_269495080";

    private final static String SIGN_NAME = "团团云自习";

    /**
     * 发送短信
     * @param phoneNumbers 手机号
     * @param param 验证码
     */
    public static boolean sendMessage(String phoneNumbers,String param){
        boolean flag = false;
        DefaultProfile profile = DefaultProfile.getProfile(REGION, CustomProperties.accessKeyId, CustomProperties.accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(SIGN_NAME);
        request.setTemplateCode(TEMPLATE_CODE);
        request.setTemplateParam("{\"code\":\""+param+"\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            String code = response.getCode();
            if ("OK".equals(code)){
                flag = true;
                System.out.println("短信发送成功");
            }

        }catch (ClientException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static String getRandomCode(){
        Random random = new Random();
        int ranNum = (int) (random.nextDouble() * (999999 - 100000 + 1)) + 100000;
        return String.valueOf(ranNum);
    }

    public static void main(String[] args) {

    }

}
