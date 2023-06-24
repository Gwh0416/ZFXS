//package org.example.chat;
//
//import com.plexpt.chatgpt.ChatGPT;
//import com.plexpt.chatgpt.entity.chat.ChatCompletion;
//import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
//import com.plexpt.chatgpt.entity.chat.Message;
//import com.plexpt.chatgpt.util.Proxys;
//
//import java.net.Proxy;
//import java.util.Arrays;
//
//public class ChatTest1 {
//    //国内需要代理 国外不需要
//
//    public static void main(String[] args) {
//        Proxy proxy = Proxys.http("localhost", 8080);
//
//        ChatGPT chatGPT = ChatGPT.builder()
//                .apiKey("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")
////                .proxy(proxy)
//                .timeout(900)
//                .apiHost("https://api.openai.com/") //反向代理地址
//                .build()
//                .init();
//
//        Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
//        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
//
//        ChatCompletion chatCompletion = ChatCompletion.builder()
//                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
//                .messages(Arrays.asList(system, message))
//                .maxTokens(3000)
//                .temperature(0.9)
//                .build();
//        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
//        Message res = response.getChoices().get(0).getMessage();
//        System.out.println(res);
//
//    }
//
//}
