package com.lxy.common.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.lxy.common.config.properties.CustomProperties;
import com.lxy.common.constant.ConfigConstants;
import com.lxy.common.domain.R;
import com.lxy.common.dto.GenerateImageDTO;
import com.lxy.common.service.BusinessConfigService;
import com.lxy.common.service.ResourcesService;
import com.lxy.common.util.FileUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.JsonUtil;
import com.lxy.common.util.OssUtil;
import com.lxy.common.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 统一上传服务器接口
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:57
 * @Version: 1.0
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



    @PostMapping("/upload")
    @ResponseBody
    public R<Object> upload(@RequestParam("file") MultipartFile[] file, HttpServletRequest request){
        R<Object> r = resourcesService.uploadFile(file);
        return r;
    }

    @PostMapping("/getStsToken")
    @ResponseBody
    public R<Object> getStsToken(){
        AssumeRoleResponse assumeRoleResponse = OssUtil.getStsCredentials();
        if (assumeRoleResponse == null){
            return R.fail(-1,"获取凭证失败");
        }
        AssumeRoleResponse.Credentials credentials = assumeRoleResponse.getCredentials();
        Map<String, Object> map = new HashMap<>(1);
        map.put("credentials", credentials);
        return R.ok(map);
    }

    @PostMapping("/getUploadPrefix")
    @ResponseBody
    public String getUploadPrefix(){
        return JsonUtil.toJson(new ResultVO(ImgConfigUtil.getPrefix()));
    }

    @PostMapping("/getAccessUrl")
    @ResponseBody
    public String getAccessUrl(){
        return JsonUtil.toJson(new ResultVO(ImgConfigUtil.getAccessUrl()));
    }

    @ApiOperation(value = "下载文件",  notes = "jiacheng yang.")
    @GetMapping("/downloadFile")
    public void downloadFile(HttpServletResponse response, String fileName){
        //是否开启OSS
        boolean flag = CustomProperties.ossEnabled;
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
