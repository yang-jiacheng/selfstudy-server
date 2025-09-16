package com.lxy.admin.service;

import com.lxy.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/20 16:57
 */

@Service
public class PoiService {

    private final UserService userService;

    @Autowired
    public PoiService(UserService userService) {
        this.userService = userService;
    }


}
