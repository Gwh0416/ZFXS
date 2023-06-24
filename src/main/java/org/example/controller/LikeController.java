package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Likes;
import org.example.service.LikeService;
import org.example.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/add")
    public R<String> addLike(HttpServletRequest request,@RequestBody Likes likes){

        likes.setLikepageid(IdUtil.IdCreate());

        likeService.save(likes);
        return R.success("点赞成功");
    }


    @GetMapping
    public R<Integer> getLike(HttpServletRequest request,Long dynamicid){

        LambdaQueryWrapper<Likes> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Likes::getDynamicid,dynamicid);
        int sum =  likeService.count(queryWrapper);

        return R.success(sum);

    }

}
