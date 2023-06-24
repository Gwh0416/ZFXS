//package org.example.chat;
//
//import org.example.common.CustomWebSocketHandler;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.socket.WebSocketSession;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@RestController
//@RequestMapping("/api")
//public class CustomController {
//
//    private final CustomWebSocketHandler webSocketHandler;
//
//    public CustomController(CustomWebSocketHandler webSocketHandler) {
//        this.webSocketHandler = webSocketHandler;
//    }
//
//    @GetMapping("/output")
//    public void outputData(HttpServletResponse response) {
//        response.setContentType("text/plain");
//        try {
//            PrintWriter writer = response.getWriter();
//            String outputData = "Hello, world!";  // 要逐字输出的数据
//            webSocketHandler.handleOutput(writer, outputData);
//        } catch (IOException e) {
//            // 异常处理逻辑
//        }
//    }
//}
