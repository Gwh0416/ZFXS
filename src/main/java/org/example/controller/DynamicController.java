package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.dto.DynamicDto;
import org.example.dto.PictureDto;
import org.example.entity.*;
import org.example.service.*;
import org.example.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/dynamic")
public class DynamicController {
    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private DynamicPicService dynamicPicService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CollectService collectService;

//    @PutMapping()
//    public R<String> update(HttpServletRequest request,@RequestBody PictureDto pictureDto){
//
//
//        return R.success("修改成功");
//    }

    @PostMapping("/add")
    public R<String> add (HttpServletRequest request, @RequestBody PictureDto pictureDto){

        Long userid = Long.valueOf(request.getSession().getAttribute("userid").toString());

        Dynamic dynamic = new Dynamic();
        Dynamicpic dynamicPic = new Dynamicpic();
        pictureDto.setUserid(userid);
        String content = pictureDto.getContent();
        String title = pictureDto.getTitle();
        String[] picture = pictureDto.getPicture();

        dynamic.setUserid(userid);
        dynamic.setContent(content);
        dynamic.setTitle(title);
        dynamic.setDynamicid(IdUtil.IdCreate());
        Long dynamicId = dynamic.getDynamicid();
        dynamic.setIsdelete(0);
        dynamic.setTime(LocalDateTime.now());

        dynamicService.save(dynamic);

        for(int i=0;i<picture.length;i++){
            dynamicPic.setDynamicpicid(IdUtil.IdCreate());
            dynamicPic.setDynamicid(dynamicId);
            dynamicPic.setNum(i+1);
            dynamicPic.setUrl(picture[i]);

            dynamicPicService.save(dynamicPic);
        }

        return R.success("发布成功！");
    }

    @GetMapping("/detail")
    public R<PictureDto> getDynamicBydynamicId(HttpServletRequest request,Long dynamicid){

        Dynamic dynamic = dynamicService.getById(dynamicid);

        PictureDto pictureDto = new PictureDto();

        pictureDto.setTime(dynamic.getTime());
        pictureDto.setDynamicid(dynamicid);
        pictureDto.setContent(dynamic.getContent());
        pictureDto.setUserid(dynamic.getUserid());
//        pictureDto.setPicture();
        LambdaQueryWrapper<Dynamicpic> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Dynamicpic::getDynamicid,dynamicid);
        queryWrapper1.orderByAsc(Dynamicpic::getNum);

        List<Dynamicpic> dynamicPicList = dynamicPicService.list(queryWrapper1);

        if(dynamicPicList.size()!=0){
            String[] pictures = new String[dynamicPicList.size()];
            for(int j=0;j<dynamicPicList.size();j++){
                pictures[j] = dynamicPicList.get(j).getUrl();
            }
            pictureDto.setPicture(pictures);
        }

        LambdaQueryWrapper<Likes> queryWrapperlike = new LambdaQueryWrapper<>();

        queryWrapperlike.eq(Likes::getDynamicid,dynamicid);
        int sum =  likeService.count(queryWrapperlike);

        LambdaQueryWrapper<Collect> queryWrappercollect = new LambdaQueryWrapper<>();

        queryWrappercollect.eq(Collect::getDynamicid,dynamicid);
        queryWrappercollect.eq(Collect::getIsdelete,0);
        int sum1 =  collectService.count(queryWrappercollect);

        pictureDto.setCollectNum(sum1);
        pictureDto.setLikeNum(sum);
        pictureDto.setUserurl(userService.getById(dynamic.getUserid()).getUrl());
        pictureDto.setUsername(userService.getById(dynamic.getUserid()).getUsername());
        pictureDto.setTitle(dynamic.getTitle());

        return R.success(pictureDto);

    }

    @GetMapping("/search")
    public R<List> getDynamicBySearch(HttpServletRequest request,String search){
        LambdaQueryWrapper<Dynamic> queryWrapper = new LambdaQueryWrapper<>();

        if (search != null || search.length()!=0) {

            queryWrapper.eq(Dynamic::getIsdelete,0)
                    .and(wq->wq.like(Dynamic::getTitle,search)
                            .or().like(Dynamic::getContent,search));

        }

        queryWrapper.eq(Dynamic::getIsdelete, 0);
        queryWrapper.orderByDesc(Dynamic::getTime);

        List<Dynamic> dynamicList = dynamicService.list(queryWrapper);

        List<DynamicDto> dynamicDtoList = new ArrayList<>();

        for (int i = 0; i < dynamicList.size(); i++) {
            Long dynamicId = dynamicList.get(i).getDynamicid();
            Long userid1 = dynamicList.get(i).getUserid();


            LambdaQueryWrapper<Dynamicpic> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Dynamicpic::getDynamicid, dynamicId);
            queryWrapper1.orderByAsc(Dynamicpic::getNum);
            List<Dynamicpic> dynamicPicList = dynamicPicService.list(queryWrapper1);
            DynamicDto dynamicDto = new DynamicDto();
            dynamicDto.setUrl(dynamicPicList.get(0).getUrl());
            User user = userService.getById(userid1);
            dynamicDto.setUserurl(user.getUrl());
            dynamicDto.setDynamicid(dynamicId);
            dynamicDto.setTitle(dynamicList.get(i).getTitle());
            dynamicDto.setTime(dynamicList.get(i).getTime());
            dynamicDto.setUserid(userid1);

            LambdaQueryWrapper<Likes> queryWrapperlike = new LambdaQueryWrapper<>();

            queryWrapperlike.eq(Likes::getDynamicid,dynamicId);
            int sum =  likeService.count(queryWrapperlike);
            dynamicDto.setLikeNum(sum);

            LambdaQueryWrapper<Collect> queryWrappercollect = new LambdaQueryWrapper<>();

            queryWrappercollect.eq(Collect::getDynamicid,dynamicId);
            queryWrappercollect.eq(Collect::getIsdelete,0);
            int sum1 =  collectService.count(queryWrappercollect);

            dynamicDto.setCollectNum(sum1);
            dynamicDto.setUsername(user.getUsername());


            dynamicDtoList.add(dynamicDto);
        }
        return R.success(dynamicDtoList);
    }

    @GetMapping("/user")
    public R<List> getDynamicByUser(HttpServletRequest request,Long userid) {

        LambdaQueryWrapper<Dynamic> queryWrapper = new LambdaQueryWrapper<>();

        if (userid != null) {
            queryWrapper.eq(Dynamic::getUserid, userid);
        }

        queryWrapper.eq(Dynamic::getIsdelete, 0);
        queryWrapper.orderByDesc(Dynamic::getTime);

        List<Dynamic> dynamicList = dynamicService.list(queryWrapper);

        List<DynamicDto> dynamicDtoList = new ArrayList<>();

        for (int i = 0; i < dynamicList.size(); i++) {
            Long dynamicId = dynamicList.get(i).getDynamicid();
            Long userid1 = dynamicList.get(i).getUserid();


            LambdaQueryWrapper<Dynamicpic> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Dynamicpic::getDynamicid, dynamicId);
            queryWrapper1.orderByAsc(Dynamicpic::getNum);
            List<Dynamicpic> dynamicPicList = dynamicPicService.list(queryWrapper1);
            DynamicDto dynamicDto = new DynamicDto();
            dynamicDto.setUrl(dynamicPicList.get(0).getUrl());
            User user = userService.getById(userid1);
            dynamicDto.setUserurl(user.getUrl());
            dynamicDto.setDynamicid(dynamicId);
            dynamicDto.setTitle(dynamicList.get(i).getTitle());
            dynamicDto.setTime(dynamicList.get(i).getTime());
            dynamicDto.setUserid(userid1);

            LambdaQueryWrapper<Likes> queryWrapperlike = new LambdaQueryWrapper<>();

            queryWrapperlike.eq(Likes::getDynamicid,dynamicId);
            int sum =  likeService.count(queryWrapperlike);
            dynamicDto.setLikeNum(sum);

            LambdaQueryWrapper<Collect> queryWrappercollect = new LambdaQueryWrapper<>();

            queryWrappercollect.eq(Collect::getDynamicid,dynamicId);
            queryWrappercollect.eq(Collect::getIsdelete,0);
            int sum1 =  collectService.count(queryWrappercollect);

            dynamicDto.setCollectNum(sum1);
            dynamicDto.setUsername(user.getUsername());

            dynamicDtoList.add(dynamicDto);
        }
        return R.success(dynamicDtoList);
    }


    @DeleteMapping
    public R<String> delete(HttpServletRequest request,Long dynamicid){

        Dynamic dynamic = dynamicService.getById(dynamicid);
        dynamic.setIsdelete(1);
        dynamicService.updateById(dynamic);

        return R.success("删除成功");
    }
}
