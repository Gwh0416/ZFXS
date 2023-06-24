package org.example.chat;

import org.example.common.R;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
//@RestController
@RequestMapping("/api/app/v1")
public class StreamingController {

////    @GetMapping("/chat1")
//    @GetMapping(value = "/chat1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
////    @Async
//    public SseEmitter streamData(HttpServletResponse response) {
//        SseEmitter emitter = new SseEmitter();
////        response.setContentType("text/event-stream");
//
//        // 异步处理逐字输出的逻辑
//        new Thread(() -> {
//            try {
//
//                String outputData = "您的五月账单如下：" +
//                        "支出：" +
//                        "饮食：643元" +
//                        "出行：44元" +
//                        "娱乐：249元" +
//                        "学习：0元" +
//                        "日用品：0元" +
//                        "服饰：0元" +
//                        "医疗：0元" +
//                        "住房：0元" +
//                        "其他：0元" +
//                        "收入：" +
//                        "红包收入：66元" +
//                        "工资收入：0元" +
//                        "理财收入：0元" +
//                        "其他收入：0元" +
//                        "总支出：936元    总收入：66元" +
//                        "本月您的饮食占比超过60%，注意健康消费";  // 要逐字输出的数据
//
//                for (char c : outputData.toCharArray()) {
//                    emitter.send(c,MediaType.TEXT_PLAIN);
//                    Thread.sleep(100);  // 可以根据需要调整发送速度
//                }
//                emitter.complete();
//            } catch (IOException | InterruptedException e) {
//                emitter.completeWithError(e);
//            }
//        }).start();
//
//        return emitter;
//    }

    @GetMapping("chat1")
    public String chat(HttpServletResponse response){

        String output = "### 您的五月账单如下：\n" +
                "\n" +
                "| 支出        | 收入       |\n" +
                "| ----------- | ---------- |\n" +
                "| 饮食：643元 | 红包：66元 |\n" +
                "| 出行：44元  | 工资：0元  |\n" +
                "| 娱乐：249元 | 理财：0元  |\n" +
                "| 学习：0元   | 其他：0元  |\n" +
                "| 日用品：0元 |            |\n" +
                "| 服饰：0元   |            |\n" +
                "| 医疗：0元   |            |\n" +
                "| 住房：0元   |            |\n" +
                "| 其他：0元   |            |\n" +
                "\n" +
                "#### 总支出：936元\t总支出：66元\n" +
                "\n" +
                "**本月您的饮食占比超过60%，注意健康消费**";

        String outputData = "<h3> 您的五月账单如下：</h3>" +
                "<br>"+

                "<h4>支出：</h4>" +
                "饮食：643元" +
                "<br>"+
                "出行：44元" +
                "<br>"+
                "娱乐：249元" +
                "<br>"+
                "学习：0元" +
                "<br>"+
                "日用品：0元" +
                "<br>"+
                "服饰：0元" +
                "<br>"+
                "医疗：0元" +
                "<br>"+
                "住房：0元" +
                "<br>"+
                "其他：0元" +
                "<br>"+
//                "\n"+
                "<br>"+
                "<h4>收入：</h4>" +
                "红包收入：66元" +
                "<br>"+
                "工资收入：0元" +
                "<br>"+
                "理财收入：0元" +
                "<br>"+
                "其他收入：0元" +
                "<br>"+
                "<br>"+
                "<h4>总支出：936元</h4>" +
                "<h4>总支出：66元</h4>" +
                "<br>"+
                "本月您的<b>饮食</b>占比超过60%，注意健康消费";  // 要逐字输出的数据
        return outputData;
    }

//    @RequestMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    @ResponseBody
//    @Async
//    public SseEmitter streamSse() {
//        SseEmitter emitter = new SseEmitter();
//        List<String> messages = getMessages();
//        new Thread(() -> {
//            for (String message : messages) {
//                try {
//                    emitter.send(message, MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE));
//                    sleep(1000);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            emitter.complete();
//        }).start();
//
//        return emitter;
//    }
//
//    // 辅助方法，返回一些文本消息
//    private List<String> getMessages() {
//        List<String> messages = new ArrayList<>();
//        messages.add("Hello, World!");
//        messages.add("How are you?");
//        messages.add("What's up?");
//        messages.add("Have a good day!");
//        messages.add("See you soon.");
//        return messages;
//    }
//
//    // 辅助方法，使线程休眠指定时间
//    private void sleep(long time) {
//        try {
//            Thread.sleep(time);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }

}
