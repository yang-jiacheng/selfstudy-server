package com.lxy.framework.controller;

import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.lxy.common.properties.AliYunProperties;
import com.lxy.common.domain.R;
import com.lxy.system.dto.GenerateImageDTO;
import com.lxy.system.service.ResourcesService;
import com.lxy.common.util.FileUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.OssUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

/**
 * 统一上传服务器接口
 * @author jiacheng yang.
 * @since 2022/10/08 20:57
 * @version 1.0
 */

@Controller
@RequestMapping(value = "/resources")
public class ResourcesController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ResourcesService resourcesService;

    @Autowired
    public ResourcesController(ResourcesService resourcesService) {
        this.resourcesService = resourcesService;
    }

    private static final String APK_PATH="android/studyroom/apk/";



    @PostMapping(value = "/upload", produces = "application/json")
    @ResponseBody
    public R<Object> upload(@RequestParam("file") MultipartFile[] file, HttpServletRequest request){
        R<Object> r = resourcesService.uploadFile(file);
        return r;
    }

    @PostMapping(value = "/getStsToken", produces = "application/json")
    @ResponseBody
    public R<Map<String, Object>> getStsToken(){
        AssumeRoleResponse assumeRoleResponse = OssUtil.getStsCredentials();
        if (assumeRoleResponse == null){
            return R.fail(-1,"获取凭证失败");
        }
        AssumeRoleResponse.Credentials credentials = assumeRoleResponse.getCredentials();
        Map<String, Object> map = new HashMap<>(1);
        map.put("credentials", credentials);
        return R.ok(map);
    }

    @PostMapping(value = "/getUploadPrefix", produces = "application/json")
    @ResponseBody
    public R<Object> getUploadPrefix(){
        return R.ok(ImgConfigUtil.getPrefix());
    }

    @PostMapping(value = "/getAccessUrl", produces = "application/json")
    @ResponseBody
    public R<Object> getAccessUrl(){
        return R.ok(ImgConfigUtil.getAccessUrl());
    }

    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, String fileName){
        //是否开启OSS
        boolean flag = AliYunProperties.ossEnabled;
        if (flag){
            OssUtil.downloadOssFile(response,fileName);
        }else {
            FileUtil.downloadFile(response,fileName);
        }
    }

    @PostMapping(value = "/generateImage", produces = "application/json")
    @ResponseBody
    public R<Object> generateImage(@RequestBody @Valid GenerateImageDTO generateImageDTO){
        String url = ImgConfigUtil.processImage(generateImageDTO.getUrl(), generateImageDTO.getParameters());
        return R.ok(url);
    }

}
