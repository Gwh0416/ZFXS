//package org.example.chat;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class ChatController {
//    private final ChatGptService chatGptService;
//
//    @Autowired
//    public ChatController(ChatGptService chatGptService) {
//        this.chatGptService = chatGptService;
//    }
//
//    @PostMapping("/chat")
//    public String chat(@RequestBody String message) {
//        // 调用 ChatGptService 来获取 ChatGPT 的响应
//        String response = chatGptService.getChatResponse(message);
//        return response;
//    }
//}
