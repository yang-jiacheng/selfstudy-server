package com.lxy.framework.security.wrapper;


import cn.hutool.core.io.IoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(CustomHttpServletRequestWrapper.class);

    private final String body;

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();

            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                char[] charBuffer = new char[128];
                int bytesRead = -1;

                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            logger.error("读取请求体异常...");
        } finally {
            IoUtil.close(bufferedReader);
        }
        this.body = stringBuilder.toString();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());

        // 创建并返回一个匿名类，继承 ServletInputStream
        ServletInputStream inputStream = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            // 实现 isFinished() 方法，表示流是否已经结束
            @Override
            public boolean isFinished() {
                // 判断流是否读取完毕
                return byteArrayInputStream.available() == 0;
            }

            // 实现 isReady() 方法，表示流是否准备好
            @Override
            public boolean isReady() {
                return true; // 通常你可以根据需要实现该方法
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // 如果需要非阻塞IO，可以实现该方法
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        return inputStream;
    }

    // getBody方法，返回请求体的内容
    public String getBody() {
        return body; // 返回已保存的请求体内容
    }


}
