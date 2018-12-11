package com.wxy.bigdata.pc.hello.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping("/hello")
    public String hello() throws JsonProcessingException {
//        logger.info("hello world");
//        return "hello world";
//        map.put("hello", "欢迎进入HTML页面");
        return "user/index";
    }

}
