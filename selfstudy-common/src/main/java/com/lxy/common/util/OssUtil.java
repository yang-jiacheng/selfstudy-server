package com.lxy.common.util;

import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.lxy.common.properties.AliYunProperties.*;

/**
 * 阿里云对象存储 OSS
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 15:10
 */
@Slf4j
public class OssUtil {

    public static final String UPLOAD_FOLDER = "upload/";

    /**
     * 初始化oss客户端
     */
    public static OSS initOssClient() {
        return new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 上传到OSS服务器(流式上传)
     *
     * @param url 路径，不能包含Bucket名称 例：upload/2022-12-20/1.jpg
     */
    public static void uploadFileToOss(String url, InputStream fileInputStream) {
        OSS ossClient = initOssClient();
        try {
            ossClient.putObject(ossBucket, url, fileInputStream);
        } catch (Exception e) {
            log.error("上传文件到OSS失败: {}", url, e);
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * <p>
     * 根据文件地址删除文件及目录
     * </p>
     *
     * @param filePath 文件地址 不能包含Bucket名称 例子：upload/2022-12-20/1.jpg
     */
    public static void deleteFile(String filePath) {
        OSS ossClient = initOssClient();
        try {
            // 文件是否存在
            boolean flag = ossClient.doesObjectExist(ossBucket, filePath);
            if (flag) {
                ossClient.deleteObject(ossBucket, filePath);
            }
        } catch (Exception e) {
            log.error("删除文件失败: {}", filePath, e);
        } finally {
            ossClient.shutdown();
        }
    }

    public static AssumeRoleResponse getStsCredentials() {
        String endpoint = "sts.cn-hangzhou.aliyuncs.com";
        String roleArn = "acs:ram::1942543017597927:role/ram-sts";
        String roleSessionName = "Ram-Sts";
        try {
            // 构造profile
            DefaultProfile.addEndpoint("cn-hangzhou", "Sts", endpoint);
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            // 构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            try {
                final AssumeRoleRequest request = new AssumeRoleRequest();
                // 设置角色信息
                request.setSysMethod(MethodType.POST);
                request.setRoleArn(roleArn);
                request.setRoleSessionName(roleSessionName);
                // 设置访问方式
                request.setSysEndpoint(endpoint);
                // 设置访问协议
                request.setSysProtocol(ProtocolType.HTTPS);
                // request.setDurationSeconds(); 默认 3600秒
                final AssumeRoleResponse response = client.getAcsResponse(request);
                return response;
            } finally {
                // 关闭client
                client.shutdown();
            }
        } catch (Exception e) {
            log.error("Failed to get STS credentials", e);
        }
        return null;
    }

    /**
     * 下载文件: file目录下的文件
     *
     * @param fileName 文件名
     */
    public static void downloadOssFile(HttpServletResponse response, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        String path = "file/" + fileName;
        OSS ossClient = initOssClient();
        try {
            // 清空response
            response.reset();
            response.setCharacterEncoding("UTF-8");
            // 告知浏览器以下载的方式打开文件，文件名如果包含中文需要指定编码
            response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");
            out = response.getOutputStream();
            OSSObject ossObject = ossClient.getObject(ossBucket, path);
            in = ossObject.getObjectContent();
            // 将输入流中的数据循环写入到响应输出流中，而不是一次性读取到内存，通过响应输出流输出到前端
            byte[] data = new byte[8192];
            int len = 0;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
            out.flush();
            ossObject.close();
        } catch (Exception e) {
            log.error(StrUtil.format("下载OSS文件失败: {}", path), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                // 静默关闭
            }
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * <p>
     * 根据文件地址获取该文件的OSS对象
     * </p>
     *
     * @param filePath 文件地址
     * @return oss对象
     */
    public OSSObject getOssFileObject(String filePath, OSS ossClient) {
        OSSObject ossObject = null;
        try {
            ossObject = ossClient.getObject(ossBucket, filePath);
        } catch (Exception e) {
            log.error(StrUtil.format("获取OSS对象失败: {}", filePath), filePath, e);
        }
        return ossObject;
    }

}
