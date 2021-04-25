package com.ifugle.rap.data.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WenYuan
 * @version $
 * @since 4月 25, 2021 10:11
 */
@RestController
public class ApplicationController {

    @GetMapping("/hc")
    public String healthy(){
        return "OK";
    }
}
