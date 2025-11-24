package com.lxy.framework.controller;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.domain.R;
import com.lxy.common.dto.GenerateImageDTO;
import com.lxy.common.properties.AliYunProperties;
import com.lxy.common.util.FileUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.OssUtil;
import com.lxy.system.service.BusinessConfigService;
import com.lxy.system.service.resources.ResourcesService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一上传服务器接口
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 20:57
 */

@Controller
@RequestMapping(value = "/resources")
public class ResourcesController {

    private static final String APK_PATH = "android/studyroom/apk/";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private ResourcesService resourcesService;
    @Resource
    private BusinessConfigService businessConfigService;

    @PostMapping(value = "/upload", produces = "application/json")
    @ResponseBody
    public R<List<String>> upload(@RequestParam("file") MultipartFile[] file, HttpServletRequest request) {
        R<List<String>> r = resourcesService.uploadFile(file);
        return r;
    }

    @PostMapping(value = "/getStsToken", produces = "application/json")
    @ResponseBody
    public R<Map<String, Object>> getStsToken() {
        AssumeRoleResponse assumeRoleResponse = OssUtil.getStsCredentials();
        if (assumeRoleResponse == null) {
            return R.fail(-1, "获取凭证失败");
        }
        AssumeRoleResponse.Credentials credentials = assumeRoleResponse.getCredentials();
        // 压缩大小
        int size = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.COMPRESSION_SIZE));
        Map<String, Object> map = new HashMap<>(1);
        map.put("credentials", credentials);
        map.put("ossPath", AliYunProperties.ossPath);
        map.put("compressionSize", 1024L * size);
        return R.ok(map);
    }

    @PostMapping(value = "/getUploadPrefix", produces = "application/json")
    @ResponseBody
    public R<Object> getUploadPrefix() {
        return R.ok(ImgConfigUtil.getPrefix());
    }

    @PostMapping(value = "/getAccessUrl", produces = "application/json")
    @ResponseBody
    public R<Object> getAccessUrl() {
        return R.ok(ImgConfigUtil.getAccessUrl());
    }

    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, String fileName) {
        // 是否开启OSS
        boolean flag = AliYunProperties.ossEnabled;
        if (flag) {
            OssUtil.downloadOssFile(response, fileName);
        } else {
            FileUtil.downloadFile(response, fileName);
        }
    }

    @PostMapping(value = "/generateImage", produces = "application/json")
    @ResponseBody
    public R<Object> generateImage(@RequestBody @Valid GenerateImageDTO generateImageDTO) {
        String url = ImgConfigUtil.processImage(generateImageDTO.getUrl(), generateImageDTO.getParameters());
        return R.ok(url);
    }

}
