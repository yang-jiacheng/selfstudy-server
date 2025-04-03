package com.lxy.app.security.service;


import com.lxy.common.domain.R;

/**
 * TODO
 * @author jiacheng yang.
 * @since 2023/02/13 10:27
 * @version 1.0
 */
public interface LoginService {

    R<Object> login(String username, String password, String device);

    void logout(String token);

}
