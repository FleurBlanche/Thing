package com.sjtu.edu.cn.iot.Controller;

import com.sjtu.edu.cn.iot.Domain.SessionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket/{id}")
@Component
public class WebsocketEndpoint {

    public static final Map<String, SessionEntity> sessionMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebsocketEndpoint.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        if (sessionMap.containsKey(id)){
            String warning = String.format("Connection of %s has been established", id);
            logger.info(warning);
            session.getAsyncRemote().sendText(warning);
        }
        logger.info(String.format("Connection of %s is established", id));
        SessionEntity se = new SessionEntity(session, id);
        sessionMap.put(id, se);
    }

    @OnClose
    public void onClose(Session session, @PathParam("id") String id) {
        logger.info(String.format("Connection of %s is closed", id));
        sessionMap.remove(id);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("id") String id) {
        //TODO
        logger.info(String.format("Get Message From %s: " + message), id);
    }

    @OnError
    public void onError(Session session, Throwable error, @PathParam("id") String id) {
        error.printStackTrace();
        session.getAsyncRemote().sendText(error.getMessage());
    }
}
