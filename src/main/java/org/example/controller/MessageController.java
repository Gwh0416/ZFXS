package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Image;
import org.example.entity.Message;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping
    public R<List> getMessage(HttpServletRequest request){

        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        List<Message> list = messageService.list(queryWrapper);
        return R.success(list);

    }
}
