package com.sjtu.edu.cn.iot;

import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final String ipAddress = "192.168.1.107";
    public static final String port = "8080";
    public static final String gate = "ws://192.168.1.103:31020/websocket/";
    public static final String wsName = "lxhCamera1";
    public static final String tableUrl = "/table";
}
