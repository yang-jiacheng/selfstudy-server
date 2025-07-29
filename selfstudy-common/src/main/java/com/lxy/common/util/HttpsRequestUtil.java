package com.lxy.common.util;


import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpsRequestUtil {

    public HttpsRequestUtil() {
    }

    public static String doGet(String url, Map<String, Object> params, Map<String, String> headers) {
        CloseableHttpClient httpclient = null;
        String resultString = "";
        CloseableHttpResponse response = null;

        try {
            httpclient = SSLClient.wrapClient();
            URIBuilder builder = new URIBuilder(url);
            if (params != null) {
                for(String key : params.keySet()) {
                    builder.addParameter(key, (String)params.get(key));
                }
            }

            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            if (headers != null) {
                for(String key : headers.keySet()) {
                    httpGet.setHeader(key, headers.get(key));
                }
            }

            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("http get请求失败", e);
        } finally {
            releaseHttp(httpclient, response);
        }

        return resultString;
    }

    public static String doPostForm(String url, Map<String, Object> params, Map<String, String> headers) {
        CloseableHttpResponse httpResponse = null;
        String result = "";
        CloseableHttpClient httpClient = SSLClient.wrapClient();
        HttpPost httpPost = getHttpPost(url);
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        setPostHeaders(httpPost, headers);
        if (MapUtil.isNotEmpty(params)) {
            List<NameValuePair> nvps = new ArrayList<>(params.size());
            for(Map.Entry<String, Object> mapEntry : params.entrySet()) {
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
        }

        try {
            httpResponse = httpClient.execute(httpPost);
            HttpEntity entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            log.error("http post请求失败", e);
        } finally {
            releaseHttp(httpClient, httpResponse);
        }

        return result;
    }

    public static String doPostJson(String url, String jsonString,Map<String, String> headers) {
        CloseableHttpClient httpClient = SSLClient.wrapClient();
        CloseableHttpResponse response = null;
        String resultString = "";

        try {
            HttpPost httpPost = getHttpPost(url);
            setPostHeaders(httpPost, headers);
            StringEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("http post请求失败", e);
        } finally {
            releaseHttp(httpClient, response);
        }

        return resultString;
    }

    public static HttpPost getHttpPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setRedirectsEnabled(true)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(60000)
                .build();
        httpPost.setConfig(requestConfig);
        return httpPost;
    }

    public static void setPostHeaders(HttpPost httpPost,Map<String, String> headers){
        if(MapUtil.isNotEmpty(headers)){
            for(Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    public static void releaseHttp(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        try {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (IOException e) {
            log.error("release http error", e);
        }

    }

    public static class SSLClient {
        public SSLClient() {
        }

        public static CloseableHttpClient wrapClient() {
            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                X509TrustManager tm = new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    }
                };
                ctx.init((KeyManager[])null, new TrustManager[]{tm}, (SecureRandom)null);
                SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
                return HttpClients.custom().setSSLSocketFactory(ssf).build();
            } catch (Exception var4) {
                log.error("create ssl client error", var4);
                return HttpClients.createDefault();
            }
        }
    }

}
