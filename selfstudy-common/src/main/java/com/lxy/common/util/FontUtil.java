package com.lxy.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2025/01/03 16:04
 * @Version: 1.0
 */
public class FontUtil {

    private final static Logger LOG = LoggerFactory.getLogger(FontUtil.class);

    // 缓存字体实例，使用 volatile 保证线程安全
    private static volatile Font fontInstance = null;

    // 私有构造函数，防止外部实例化
    private FontUtil() {
    }

    /**
     * 获取字体实例，单例模式
     */
    private static Font getBaseFont() {
        if (fontInstance == null) {
            synchronized (FontUtil.class) {
                if (fontInstance == null) {
                    try (InputStream fontStream = Files.newInputStream(Paths.get("/opt/fonts/Fzhtk.ttf"))) {
                        fontInstance = Font.createFont(Font.TRUETYPE_FONT, fontStream);
                        LOG.error("加载字体成功 fontName: Fzhtk.ttf");
                    } catch (Exception e) {
                        LOG.error("加载字体失败", e);
                    }
                }
            }
        }
        return fontInstance;
    }

    /**
     * 获取特定大小和样式的字体
     */
    public static Font getFont(int fontStyle, int fontSize) {
        Font baseFont = getBaseFont();
        // 基于缓存的字体创建新的实例
        return baseFont.deriveFont(fontStyle, fontSize);
    }

    public static void clearFontCache() {
        fontInstance = null;  // 移除引用
    }

}
