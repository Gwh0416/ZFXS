package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Diary;
import org.example.entity.Image;
import org.example.service.ImageService;
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
@RequestMapping("/api/app/v1/image")
public class ImageController {
    @Autowired
    ImageService imageService;

    @GetMapping
    public R<List> getImage(HttpServletRequest request){

        LambdaQueryWrapper<Image> queryWrapper = new LambdaQueryWrapper<>();
        List<Image> list = imageService.list(queryWrapper);
        return R.success(list);
    }
}
