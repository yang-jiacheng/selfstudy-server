package com.lxy.system.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import com.lxy.common.constant.ConfigConstant;
import com.lxy.common.domain.R;
import com.lxy.common.properties.AliYunProperties;
import com.lxy.common.properties.CustomProperties;
import com.lxy.common.util.FileUtil;
import com.lxy.common.util.ImgConfigUtil;
import com.lxy.common.util.OssUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2024/01/30 9:28
 * @version 1.0
 */

@Service
public class ResourcesService {

    private final Logger logger = LoggerFactory.getLogger(ResourcesService.class);

    private final BusinessConfigService businessConfigService;

    @Autowired
    public ResourcesService(BusinessConfigService businessConfigService) {
        this.businessConfigService = businessConfigService;
    }

    public R<Object> uploadFile(MultipartFile[]  file){
        List<String> paths = new ArrayList<>(file.length);
        InputStream in = null;
        FileOutputStream fos = null;
        try {


            String fileName = "";
            for (MultipartFile multipartFile : file) {
                //原始文件名
                fileName = multipartFile.getOriginalFilename();
                //随机文件名
                fileName = FileUtil.getRandomFileName(fileName);

                in = multipartFile.getInputStream();
                if (ImgConfigUtil.isImage(fileName)){
                    int size = Integer.parseInt(businessConfigService.getBusinessConfigValue(ConfigConstant.COMPRESSION_SIZE));
                    if (multipartFile.getSize() >= 1024L * size){
                        //压缩图片
                        in = ImgConfigUtil.uploadImageReStream(in);
                    }
                }
                //文件相对路径
                String path ="/upload/"+DateUtil.today()+"/"+fileName;
                //上传到OSS
                if (AliYunProperties.ossEnabled){
                    OssUtil.uploadFileToOss(path.substring(1),in);
                    //FileUtil.deleteFileAndFolderByUpload();
                }else {
                    // 设置上传图片的保存路径
                    String savePath = CustomProperties.uploadPath+ DateUtil.today()+"/";
                    String transUrl = savePath + fileName;
                    //创建文件夹
                    FileUtil.judeDirExists(savePath);
                    File transFile = new File(transUrl);
                    // 将文件保存到本地
                    fos = new FileOutputStream(transFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                }
                logger.error("文件上传成功，路径：{}",path);
                paths.add(path);
            }
        }catch (Exception e){
            logger.error("文件上传发生异常",e);
            return R.fail(-1,"调用失败");
        }finally {
            IoUtil.close(in);
            IoUtil.close(fos);
        }
        return R.ok(paths);
    }

}
