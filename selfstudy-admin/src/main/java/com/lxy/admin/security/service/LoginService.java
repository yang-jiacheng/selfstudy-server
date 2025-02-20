package com.lxy.admin.security.service;


import com.lxy.common.vo.ResultVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/13 10:27
 * @Version: 1.0
 */
public interface LoginService {

    ResultVO login(String username, String password, String device, HttpServletResponse response);

    void logout(String token, HttpServletRequest request,HttpServletResponse response);

}
