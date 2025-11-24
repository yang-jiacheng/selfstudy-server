package com.lxy.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.lxy.common.constant.SmsConstant;
import com.lxy.common.dto.SmsSendDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lxy.common.properties.AliYunProperties.accessKeyId;
import static com.lxy.common.properties.AliYunProperties.accessKeySecret;

/**
 * 短信认证服务：适用于个人开发者
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2025/11/18 17:26
 */

@Slf4j
public class SmsVerifyCodeUtil {

    public final static String TEMPLATE_CODE = "100001";
    public final static String TEMP_CODE = "code";
    public final static String TEMP_MIN = "min";
    private final static String REGION = "cn-hangzhou";
    private final static String ENDPOINT = "dypnsapi.aliyuncs.com";
    private final static String SIGN_NAME = "速通互联验证服务";
    private static volatile Client clientInstance = null;

    public static Client getClient() {
        if (clientInstance == null) {
            synchronized (SmsUtil.class) {
                if (clientInstance == null) {
                    Config config = new Config();
                    config.setAccessKeyId(accessKeyId);
                    config.setAccessKeySecret(accessKeySecret);
                    config.setRegionId(REGION);
                    config.setEndpoint(ENDPOINT);
                    try {
                        clientInstance = new Client(config);
                    } catch (Exception e) {
                        clientInstance = null;
                        log.error("短信认证客户端初始化失败", e);
                    }
                }
            }
        }
        return clientInstance;
    }

    public static boolean sendMessage(String phone, SmsSendDTO smsSendDTO) {
        boolean flag = false;
        Client client = getClient();
        SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest();
        request.setPhoneNumber(phone);
        request.setSignName(SIGN_NAME);
        String templateCode = smsSendDTO.getTemplateCode();
        if (StrUtil.isEmpty(templateCode)) {
            request.setTemplateCode(TEMPLATE_CODE);
        } else {
            request.setTemplateCode(templateCode);
        }
        // 如果有模板参数，则设置模板参数
        String json = "";
        List<SmsSendDTO.TemplateParam> templateParams = smsSendDTO.getTemplateParams();
        if (CollUtil.isNotEmpty(templateParams)) {
            Map<String, Object> params = new HashMap<>(templateParams.size());
            for (SmsSendDTO.TemplateParam templateParam : templateParams) {
                params.put(templateParam.getName(), templateParam.getValue());
            }
            json = JsonUtil.toJson(params);
            request.setTemplateParam(json);
        }
        try {
            RuntimeOptions runtime = new RuntimeOptions();
            SendSmsVerifyCodeResponse response = client.sendSmsVerifyCodeWithOptions(request, runtime);
            String code = response.getBody().getCode();
            if (code.equals(SmsConstant.SUCCESS_CODE)) {
                flag = true;
                log.info("短信发送成功，手机号：{},模板：{},参数：{}", phone, templateCode, json);
            } else {
                log.error("短信发送失败，手机号：{},模板：{},参数：{},原因：{},requestId：{}", phone, templateCode, json,
                    response.getBody().getMessage(), response.getBody().getRequestId());
            }
        } catch (Exception e) {
            log.error(StrUtil.format("短信发送异常，手机号：{},模板：{},参数：{}", phone, templateCode, json), e);
        }
        return flag;
    }

}
