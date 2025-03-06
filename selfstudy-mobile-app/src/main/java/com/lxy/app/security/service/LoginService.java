package com.lxy.app.security.service;


import com.lxy.common.bo.R;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2023/02/13 10:27
 * @Version: 1.0
 */
public interface LoginService {

    R<Object> login(String username, String password, String device);

    void logout(String token);

}
