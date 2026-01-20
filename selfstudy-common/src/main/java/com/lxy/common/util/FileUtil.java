package com.lxy.common.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.lxy.common.properties.CustomProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/11/10 15:25
 */

@Component
public class FileUtil {

    public static final long UPLOAD_MAX_SIZE = 50 * 1024 * 1024;
    private final static Logger LOG = LoggerFactory.getLogger(FileUtil.class);
    private static final int BUFFER_SIZE = 8192;

    /**
     * @param algorithm MD5 SHA-1 SHA-256
     */
    public static String getHash(File file, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            try (InputStream is = new FileInputStream(file)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    md.update(buffer, 0, read);
                }
            }
            byte[] hashBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断一个路径是否存在,不存在则新建
     *
     */
    public static boolean judeDirExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            System.out.println("dir not exists, create it ...");
            try {
                dir.mkdirs();        //新建所有子级目录
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 获取格式化后的随机文件名：
     * 原始文件名（最多15字符） + "_" + 当前时间戳 + 原始后缀
     */
    public static String getRandomFileName(String fileName) {
        if (StrUtil.isBlank(fileName)) {
            return "";
        }

        int index = fileName.lastIndexOf(".");
        String namePart = index != -1 ? fileName.substring(0, index) : fileName;
        String suffix = index != -1 ? fileName.substring(index) : "";

        // 包含中文就转拼音（正则判断）
        if (namePart.matches(".*[\\u4e00-\\u9fa5].*")) {
            namePart = PinyinUtil.getPinyin(namePart, "");
        }

        // 截取前15个字符
        namePart = StrUtil.sub(namePart, 0, 15);

        return StrUtil.format("{}_{}{}", namePart, DateUtil.current(), suffix);
    }


    /**
     * 删除upload文件夹里的具体文件
     *
     * @param fileName 文件名
     * @return 是否删除成功
     */
    public static boolean deleteFileInUpload(String fileName) {
        boolean flag = false;
        String filePath = CustomProperties.uploadPath + fileName;
        try {
            Path path = Paths.get(filePath);
            Files.delete(path);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除upload文件夹及其所有文件
     */
    public static void deleteFileAndFolderByUpload() {
        try {
            Path path = Paths.get(CustomProperties.uploadPath);
            Files.walkFileTree(path,
                    new SimpleFileVisitor<Path>() {
                        // 先去遍历删除文件
                        @Override
                        public FileVisitResult visitFile(Path file,
                                                         BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            LOG.error("文件被删除 : {}", file);
                            return FileVisitResult.CONTINUE;
                        }

                        // 再去遍历删除目录
                        @Override
                        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                            Files.delete(dir);
                            LOG.error("文件夹被删除: {}", dir);
                            return FileVisitResult.CONTINUE;
                        }

                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件: file目录下的文件
     *
     * @param response response
     * @param fileName 文件名
     */
    public static void downloadFile(HttpServletResponse response, String fileName) {
        InputStream in = null;
        OutputStream out = null;
        // 下载文件路径
        String downloadPath = CustomProperties.filePath + fileName;
        try {
            File file = new File(downloadPath);
            in = new FileInputStream(file);
            //清空response
            response.reset();
            response.setCharacterEncoding("UTF-8");
            //告知浏览器以下载的方式打开文件，文件名如果包含中文需要指定编码
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            out = response.getOutputStream();
            //将输入流中的数据循环写入到响应输出流中，而不是一次性读取到内存，通过响应输出流输出到前端
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        File file = new File("D:\\AndroidCode\\StudyRoom\\app\\prod\\release\\studyroom-1.0.0-1-20230310.apk");
//        System.out.println(getHash(file,"MD5"));
//        System.out.println(getHash(file,"SHA-1"));
//        System.out.println(getHash(file,"SHA-256"));
        System.out.println(getRandomFileName("新建文本文档.txt"));
    }
}
