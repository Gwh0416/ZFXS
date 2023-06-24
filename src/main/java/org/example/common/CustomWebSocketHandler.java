package org.example.common;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class CustomWebSocketHandler extends TextWebSocketHandler {

    // 处理逐字输出的逻辑
    public void handleOutput(WebSocketSession session, String outputData) {
        try {
            for (char c : outputData.toCharArray()) {
                session.sendMessage(new TextMessage(String.valueOf(c)));
                Thread.sleep(100);  // 可以根据需要调整输出速度
            }
            session.close();
        } catch (IOException | InterruptedException e) {
            // 异常处理逻辑
        }
    }
}









