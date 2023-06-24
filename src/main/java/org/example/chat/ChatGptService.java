//package org.example.chat;
//
//import com.openai.gpt.ChatCompletion;
//import com.openai.gpt.Engine;
//import com.openai.gpt.OpenAIGPT;
//
//import org.springframework.stereotype.Service;
//
//@Service
//public class ChatGptService {
//    private final OpenAIGPT openAIGPT;
//
//    public ChatGptService() {
//        String apiKey = "YOUR_API_KEY";
//        this.openAIGPT = new OpenAIGPT(apiKey);
//        this.openAIGPT.setEngine(Engine.DAVID);
//    }
//
//    public String generateResponse(String message) {
//        ChatCompletion completion = openAIGPT.complete(message);
//        String response = completion.getChoices().get(0).getText();
//        return response;
//    }
//}
