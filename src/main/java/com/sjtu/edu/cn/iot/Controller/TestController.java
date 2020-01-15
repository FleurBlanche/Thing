package com.sjtu.edu.cn.iot.Controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Autowired
    WebsocketEndpoint websocketEndpoint;

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello World!";
    }

    @GetMapping("/table")
    @ResponseBody
    public String table(){
        //return table of functions
        return "table";
    }

}
