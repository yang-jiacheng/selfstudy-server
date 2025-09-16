package com.lxy.system.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.domain.R;
import com.lxy.common.properties.AliYunProperties;
import com.lxy.common.properties.CustomProperties;
import com.lxy.common.util.FileUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2024/01/30 9:28
 */

@Slf4j
@Service
public class ResourcesService {

    private final BusinessConfigService businessConfigService;

    @Autowired
    public ResourcesService(BusinessConfigService businessConfigService) {
        this.businessConfigService = businessConfigService;
    }

    public R<List<String>> uploadFile(MultipartFile[] files) {
        List<String> paths = new ArrayList<>(files.length);

        for (MultipartFile multipartFile : files) {
            String fileName = multipartFile.getOriginalFilename();
            fileName = FileUtil.getRandomFileName(fileName);
            // 日期路径
            String datePath = DateUtil.today();
            // 上传相对路径
            String relativePath = "/upload/" + datePath + "/" + fileName;
            int size = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.COMPRESSION_SIZE));
            try (
                    InputStream in0 = multipartFile.getInputStream();
                    // 图片压缩
                    InputStream in1 = (ImgConfigUtil.isImage(fileName)
                            && multipartFile.getSize() >= 1024L * size)
                            ? ImgConfigUtil.uploadImageReStream(in0, fileName)
                            : in0
            ) {

                if (AliYunProperties.ossEnabled) {
                    OssUtil.uploadFileToOss(relativePath.substring(1), in1);
                } else {
                    // 确保目录存在
                    String saveDir = CustomProperties.uploadPath + datePath + "/";
                    FileUtil.judeDirExists(saveDir);
                    try (FileOutputStream fos0 = new FileOutputStream(CustomProperties.uploadPath + datePath + "/" + fileName)) {
                        // 将 in1 的内容写入本地文件
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = in1.read(buffer)) != -1) {
                            fos0.write(buffer, 0, len);
                        }
                    }
                }
                log.info("文件上传成功，路径：{}", ImgConfigUtil.getPrefix() + relativePath);
                paths.add(relativePath);
            } catch (Exception e) {
                log.error(StrUtil.format("文件上传发生异常: {}", relativePath), e);
                return R.fail("文件上传失败");
            }
        }

        return R.ok(paths);
    }

}
