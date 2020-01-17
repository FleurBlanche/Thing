package com.sjtu.edu.cn.iot.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sjtu.edu.cn.iot.Config.WebclientConfig;
import com.sjtu.edu.cn.iot.Entity.Thing;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RestController
@Slf4j
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    WebsocketEndpoint websocketEndpoint;

    @Autowired
    Thing thing;

    @Autowired
    WebSocketClient webSocketClient;

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello, I'm online.";
    }

    @GetMapping("/table")
    @ResponseBody
    public String table(){
        //return table of functions
        //return (thing.getEncodingStandard());
        return thing.toJson();
    }

    @GetMapping("/file")
    @ResponseBody
    public void file(HttpServletResponse response){
        String fileName = Objects.requireNonNull(this.getClass().getClassLoader().getResource("static/test.jpg")).getPath();
        File file = new File(fileName);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment;fileName=image.jpg");
        byte[] buffer = new byte[1024];
        try {
            OutputStream os = response.getOutputStream();
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            os.close();
            logger.debug("下载成功");
        } catch (IOException e) {
            logger.debug("下载失败");
        }
    }

    @GetMapping("/config")
    @ResponseBody
    public void config(HttpServletResponse response){
        //return file : table of functions
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setHeader("Content-Disposition", "attachment;fileName=config.json");
        String content = thing.toJson();
        try {
            OutputStream os = response.getOutputStream();
            os.write(content.getBytes(StandardCharsets.UTF_8));
            os.close();
            logger.debug("下载成功");
        } catch (IOException e) {
            logger.debug("下载失败");
        }
    }
}
