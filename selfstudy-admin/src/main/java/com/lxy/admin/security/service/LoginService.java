package com.lxy.admin.security.service;


import com.lxy.system.dto.LoginVerifyCodeDTO;
import com.lxy.common.domain.R;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/13 10:27
 * @Version: 1.0
 */
public interface LoginService {

    R<Object> login(LoginVerifyCodeDTO dto, HttpServletResponse response);

    void logout(String token, HttpServletRequest request,HttpServletResponse response);

}
