package com.lxy.app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 11:15
 */

@RestController
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "hello selfstudy-mobile-app !";
    }

}
