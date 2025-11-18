package com.lxy.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.QuerySmsTemplateListRequest;
import com.aliyun.dysmsapi20170525.models.QuerySmsTemplateListResponse;
import com.aliyun.dysmsapi20170525.models.QuerySmsTemplateListResponseBody;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.lxy.common.dto.SmsSendDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.lxy.common.properties.AliYunProperties.accessKeyId;
import static com.lxy.common.properties.AliYunProperties.accessKeySecret;

/**
 * 阿里云短信
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/19 11:41
 */

@Slf4j
public class SmsUtil {


    public final static String TEMPLATE_CODE = "SMS_269495080";
    private final static String REGION = "cn-hangzhou";
    private final static String ENDPOINT = "dysmsapi.aliyuncs.com";
    private final static String SIGN_NAME = "团团云自习";
    private final static String SUCCESS_CODE = "OK";
    private static volatile Client clientInstance = null;

    public static Client getClient() {
        //新sdk
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
                        log.error("短信客户端初始化失败", e);
                    }
                }
            }
        }
        return clientInstance;
    }

    /**
     * 根据模板id发送短信
     *
     * @author jiacheng yang.
     * @since 2025/4/17 11:22
     */
    public static <T> boolean sendSmsByTemplate(String smsCode, String phoneNumbers, T record) {
        String content = getTemplateContent(smsCode);
        SmsSendDTO smsDTO = new SmsSendDTO();
        smsDTO.setTemplateCode(smsCode);
        //正则提取所有 ${xxx} 中的 xxx
        List<String> variableNames = ReUtil.findAll("\\$\\{(.*?)\\}", content, 1);
        // 如果模板里有参数
        if (CollUtil.isNotEmpty(variableNames)) {
            //遍历取值、校验、组装
            List<SmsSendDTO.TemplateParam> params = new ArrayList<>();
            for (String varName : variableNames) {
                // 从 record 中通过 getter 拿到对应属性值
                Object raw = BeanUtil.getProperty(record, varName);
                String value = raw != null ? raw.toString() : null;
                if (value == null) {
                    value = "";
                }
                params.add(new SmsSendDTO.TemplateParam(varName, value));
            }
            smsDTO.setTemplateParams(params);
        }
        boolean b = sendMessage(phoneNumbers, smsDTO);
        return b;
    }

    /**
     * 发送短信
     *
     * @author jiacheng yang.
     * @since 2025/4/17 11:03
     */
    public static boolean sendMessage(String phoneNumbers, SmsSendDTO smsSendDTO) {
        boolean flag = false;
        Client client = getClient();
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(SIGN_NAME);
        String templateCode = smsSendDTO.getTemplateCode();
        request.setTemplateCode(templateCode);
        //如果有模板参数，则设置模板参数
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
            SendSmsResponse response = client.sendSmsWithOptions(request, runtime);
            String code = response.getBody().getCode();
            if (code.equals(SUCCESS_CODE)) {
                flag = true;
                log.info("短信发送成功，手机号：{},模板：{},参数：{}", phoneNumbers, templateCode, json);
            } else {
                log.error("短信发送失败，手机号：{},模板：{},参数：{},原因：{}", phoneNumbers, templateCode, json, response.getBody().getMessage());
            }
        } catch (Exception e) {
            log.error(StrUtil.format("短信发送异常，手机号：{},模板：{},参数：{}", phoneNumbers, templateCode, json), e);
        }
        return flag;
    }

    /**
     * 查询短信模板
     *
     * @author jiacheng yang.
     * @since 2025/4/17 11:03
     */
    public static List<QuerySmsTemplateListResponseBody.QuerySmsTemplateListResponseBodySmsTemplateList> querySmsTemplateList() {
        Client client = getClient();
        QuerySmsTemplateListRequest request = new QuerySmsTemplateListRequest();
        request.setPageIndex(1);
        request.setPageSize(50);
        try {
            RuntimeOptions runtime = new RuntimeOptions();
            QuerySmsTemplateListResponse response = client.querySmsTemplateListWithOptions(request, runtime);
            String code = response.getBody().getCode();
            if (code.equals(SUCCESS_CODE)) {
                return response.getBody().getSmsTemplateList();
            }
        } catch (Exception e) {
            log.error("查询短信模板失败", e);
        }
        return null;
    }

    /**
     * 根据code获取模板内容
     *
     * @author jiacheng yang.
     * @since 2025/4/17 11:07
     */
    public static String getTemplateContent(String templateCode) {
        List<QuerySmsTemplateListResponseBody.QuerySmsTemplateListResponseBodySmsTemplateList> list = querySmsTemplateList();
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        String templateContent = list.stream()
                .filter(item -> item.getTemplateCode().equals(templateCode)).findFirst()
                .map(QuerySmsTemplateListResponseBody.QuerySmsTemplateListResponseBodySmsTemplateList::getTemplateContent).orElse("");
        return templateContent;
    }

    public static String getRandomCode() {
        Random random = new Random();
        int ranNum = (int) (random.nextDouble() * (999999 - 100000 + 1)) + 100000;
        return String.valueOf(ranNum);
    }

}
