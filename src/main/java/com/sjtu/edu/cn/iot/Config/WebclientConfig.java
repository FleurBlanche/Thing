package com.sjtu.edu.cn.iot.Config;


import com.alibaba.fastjson.JSON;
import com.sjtu.edu.cn.iot.Constants;
import com.sjtu.edu.cn.iot.Controller.TestController;
import com.sjtu.edu.cn.iot.Entity.Item;
import com.sjtu.edu.cn.iot.Entity.Thing;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Timer;

@Slf4j
@Component
public class WebclientConfig {

    private static String wsurl;

    private static String publicIpAddress = "192.168.1.107:8080";

    @Autowired
    Thing thing;

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            wsurl = Constants.gate + Constants.wsName;
            publicIpAddress = Constants.ipAddress + ":" + Constants.port;
            WebSocketClient webSocketClient = new WebSocketClient(new URI(wsurl),new Draft_6455()) {
                private boolean debug = true;
                private Integer reconnectInterval = 1000;
                private Integer maxReconnectInterval = 30000;
                private Double reconnectDecay = 1.5;
                private Integer reconnectAttempts = 0;
                private Integer maxReconnectAttempts = 5000;
                private Boolean forcedClose = false;
                private Timer reconnectTimer;
                private Boolean isReconnecting = false;
                private ReschedulableTimerTask reconnectTimerTask;

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[client websocket] 连接成功");
                    Item item = new Item(thing.getBasicInfo().getDeviceName(), (publicIpAddress + Constants.tableUrl), thing.getBasicInfo().getDeviceType());
                    send(JSON.toJSONString(item));
                }

                @Override
                public void onMessage(String message) {
                    log.info("[client websocket] 收到消息={}",message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[client websocket] 退出连接");
                    if (forcedClose) {
                        close();
                    }
                    else {
                        if (!isReconnecting) {
                            restartReconnectionTimer();
                        }
                        isReconnecting = true;
                    }
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[client websocket] 连接错误={}",ex.getMessage());
                }

                private void restartReconnectionTimer() {
                    cancelReconnectionTimer();
                    reconnectTimer = new Timer("reconnectTimer");
                    reconnectTimerTask = new ReschedulableTimerTask() {
                        @Override
                        public void run() {
                            if (reconnectAttempts >= maxReconnectAttempts) {
                                cancelReconnectionTimer();
                                if (debug) {
                                    System.out.println("以达到最大重试次数:" + maxReconnectAttempts + "，已停止重试!!!!");
                                }
                            }
                            reconnectAttempts++;
                            try {
                                Boolean isOpen = reconnectBlocking();
                                if (isOpen) {
                                    if (debug) {
                                        System.out.println("连接成功，重试次数为:" + reconnectAttempts);
                                    }
                                    cancelReconnectionTimer();
                                    reconnectAttempts = 0;
                                    isReconnecting = false;
                                } else {
                                    if (debug) {
                                        System.out.println("连接失败，重试次数为:" + reconnectAttempts);
                                    }
                                    double timeoutd = reconnectInterval * Math.pow(reconnectDecay, reconnectAttempts);
                                    if(timeoutd >= 2147364827){
                                        //超出int范围
                                        timeoutd = 2147364827;
                                    }
                                    int timeout = Integer.parseInt(new java.text.DecimalFormat("0").format(timeoutd));
                                    timeout = timeout > maxReconnectInterval ? maxReconnectInterval : timeout;
                                    System.out.println(timeout);
                                    reconnectTimerTask.re_schedule2(timeout);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    reconnectTimerTask.schedule(reconnectTimer,reconnectInterval);
                }
                private void cancelReconnectionTimer() {
                    if (reconnectTimer != null) {
                        reconnectTimer.cancel();
                        reconnectTimer = null;
                    }
                    if (reconnectTimerTask != null) {
                        reconnectTimerTask.cancel();
                        reconnectTimerTask = null;
                    }
                }
            };
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
