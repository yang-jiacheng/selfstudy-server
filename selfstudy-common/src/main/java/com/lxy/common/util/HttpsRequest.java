package com.lxy.common.util;

import cn.hutool.core.util.StrUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * https请求工具
 * @author jiacheng yang.
 * @since 2022/7/19 14:46
 * @version 1.0
 */
public class HttpsRequest {

    /**
     *  对目标url发起get请求
     * @param url 目标url
     * @param params 请求参数
     * @param headers 请求头
     * @return 返回结果
     */
    public static String doGet(String url, Map<String, String> params , Map<String, String> headers) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = null;
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            httpclient = SSLClient.wrapClient();
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
            }
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("resultString:" + resultString);
            }
        } catch (Exception e) {
            System.out.println("do get error:" + e.getMessage());
        } finally {
            releaseHttp(httpclient,response);
        }
        return resultString;
    }

    /**
     * Post 请求
     * @param url 请求路径
     * @param paramMap 请求参数
     * @return 响应消息
     */
    public static String doPostUrl(String url, Map<String, Object> paramMap) {
        CloseableHttpClient httpClient;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        httpClient = SSLClient.wrapClient();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 配置请求参数实例
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                .setSocketTimeout(60000)// 设置读取数据连接超时时间
                .build();
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        //httpPost.addHeader("Content-Type", "application/json");
        // 封装post请求参数
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            for (Map.Entry<String, Object> mapEntry : entrySet) {
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }
            // 为httpPost设置封装好的请求参数
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            releaseHttp(httpClient,httpResponse);
        }
        return result;
    }

    /**
     * post请求
     * @param url url
     * @param jsonString json请求体
     * @return json
     */
    public static String doPost(String url,String jsonString)  {
        CloseableHttpClient httpClient = SSLClient.wrapClient();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpPost实例设置配置
            httpPost.setConfig(requestConfig);
            if (StrUtil.isNotEmpty(jsonString)){
                StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            releaseHttp(httpClient,response);
        }
        return resultString;
    }

    /**
     * 释放资源
     * @param httpClient client
     * @param response res
     */
    private static void releaseHttp(CloseableHttpClient httpClient,CloseableHttpResponse response){
        try {
            if(httpClient!=null){
                httpClient.close();
            }
            if(response !=null){
                response.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为了避免需要证书，直接初始化客户端，忽略校验过程。
     */
    public static class SSLClient {
        /**
         * 绕过证书
         * @return 返回构造完成的client
         */
        public static CloseableHttpClient wrapClient() {
            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                X509TrustManager tm = new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0,
                                                   String arg1) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0,
                                                   String arg1) throws CertificateException {
                    }
                };
                ctx.init(null, new TrustManager[] { tm }, null);
                SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(
                        ctx, NoopHostnameVerifier.INSTANCE);
                CloseableHttpClient httpclient = HttpClients.custom()
                        .setSSLSocketFactory(ssf).build();
                return httpclient;
            } catch (Exception e) {
                return HttpClients.createDefault();
            }
        }
    }

}
