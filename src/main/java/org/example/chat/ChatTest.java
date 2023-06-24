package org.example.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/chat")
public class ChatTest {

    @GetMapping()
    public StreamingResponseBody streamOutput() {
        return outputStream -> {
            String text = "Hello, world!"; // 要逐字输出的文本
            for (char c : text.toCharArray()) {
                outputStream.write(c);
                outputStream.flush();
                // 可根据需要进行适当的延迟
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            outputStream.close();
        };
    }
}
