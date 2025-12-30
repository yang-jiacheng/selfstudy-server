package com.lxy.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 自动清理文件的 InputStream
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

@Slf4j
public class AutoDeleteFileInputStream extends FileInputStream {

    private final File file;

    public AutoDeleteFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (file.exists()) {
            file.delete();
            log.info("自动删除文件InputStream关闭并删除文件: {}", file.getAbsolutePath());
        }
    }
}
