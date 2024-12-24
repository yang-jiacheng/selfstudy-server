package com.lxy.common.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: IP工具类
 * @author: jiacheng yang.
 * @Date: 2022/04/14 18:51
 * @Version: 1.0
 */
public class IpUtil {

	/**
	 * 获取客户端IP地址，即使对方使用了代理
	 */
	public static String getClientIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			//Proxy-Client-IP 字段和 WL-Proxy-Client-IP 字段只在 Apache（Weblogic Plug-In Enable）+WebLogic 搭配下出现，其中“WL” 就是 WebLogic 的缩写
			ip = request.getHeader("Proxy-Client-IP");
		}else {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				ip = ip.substring(0, index);
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			//Proxy-Client-IP 字段和 WL-Proxy-Client-IP 字段只在 Apache（Weblogic Plug-In Enable）+WebLogic 搭配下出现，其中“WL” 就是 WebLogic 的缩写
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		//如果是本地ipv6地址就切换为本地ipv4地址
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	/**
	 * 获取网络用户真实ip
	 */
	public static String getNetUserIp(HttpServletRequest request){
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {

			ipAddress = request.getHeader("X-Real-IP");
			if(StrUtil.isEmpty(ipAddress)){
				ipAddress = request.getRemoteAddr();
			}
			if(StrUtil.isNotEmpty(ipAddress)){
				if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){
					//根据网卡取本机配置的IP
					InetAddress inet=null;
					try {
						inet = InetAddress.getLocalHost();
						ipAddress= inet.getHostAddress();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
			}
		}
		//对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if(ipAddress!=null && ipAddress.length()>15){
			if(ipAddress.indexOf(",")>0){
				ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	
}
