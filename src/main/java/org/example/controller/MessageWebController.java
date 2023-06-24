package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Message;
import org.example.service.MessageService;
import org.example.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/web/v1/message")
public class MessageWebController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public R<String> putMessage(HttpServletRequest request, @RequestBody Message message){

        message.setMessageid(IdUtil.IdCreate());
        message.setTime(LocalDateTime.now());
        messageService.save(message);

        return R.success("发布消息成功");

    }

    @GetMapping
    public R<Page> getMessage(HttpServletRequest request,int page, int pageSize){
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Message::getTime);

        Page<Message> messagePage = new Page<>(page,pageSize);
        messageService.page(messagePage,queryWrapper);
        return R.success(messagePage);

    }
}
