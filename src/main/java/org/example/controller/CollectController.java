package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.dto.PictureDto;
import org.example.entity.Collect;
import org.example.entity.Dynamic;
import org.example.entity.Dynamicpic;
import org.example.entity.Likes;
import org.example.service.CollectService;
import org.example.service.DynamicPicService;
import org.example.service.DynamicService;
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
@RequestMapping("/api/app/v1/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private DynamicPicService dynamicPicService;

    @PostMapping("/add")
    public R<String> addCollect(HttpServletRequest request, @RequestBody Collect collect){

        collect.setCollectid(IdUtil.IdCreate());
        collect.setCollecttime(LocalDateTime.now());
        collect.setIsdelete(0);
        collectService.save(collect);

        return R.success("收藏成功");
    }

    @DeleteMapping
    public R<String> delete(HttpServletRequest request,Long collectid){
        Collect collect = collectService.getById(collectid);
        collect.setIsdelete(1);
        collectService.updateById(collect);
        return R.success("取消收藏");
    }


    @GetMapping("/dynamic")
    public R<Integer> getCollect(HttpServletRequest request,Long dynamicid){

        LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Collect::getDynamicid,dynamicid);
        queryWrapper.eq(Collect::getIsdelete,0);
        int sum =  collectService.count(queryWrapper);
        return R.success(sum);
    }

    @GetMapping("/user")
    public R<List> getCollectByUser(HttpServletRequest request){

        if(request.getSession().getAttribute("userid")!=null){
            Long userid  = Long.valueOf(request.getSession().getAttribute("userid").toString());

            LambdaQueryWrapper<Collect> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.like(Collect::getUserid,userid);
            queryWrapper.eq(Collect::getIsdelete,0);

            List<Collect> collectList = collectService.list(queryWrapper);

            List<PictureDto> pictureDtoList = new ArrayList<>();

            for(int i=0;i<collectList.size();i++){

                Long dynamicId = collectList.get(i).getDynamicid();

                Dynamic dynamic = dynamicService.getById(dynamicId);

                LambdaQueryWrapper<Dynamicpic> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(Dynamicpic::getDynamicid,dynamicId);
                queryWrapper1.orderByAsc(Dynamicpic::getNum);
                List<Dynamicpic> dynamicPicList = dynamicPicService.list(queryWrapper1);

                if(dynamicPicList.size()!=0){
                    String[] pictures = new String[dynamicPicList.size()];
                    for(int j=0;j<dynamicPicList.size();j++){
                        pictures[j] = dynamicPicList.get(j).getUrl();
                    }

                    PictureDto pictureDto = new PictureDto();
                    pictureDto.setDynamicid(dynamicId);
                    pictureDto.setUserid(dynamic.getUserid());
                    pictureDto.setTime(dynamic.getTime());
                    pictureDto.setContent(dynamic.getContent());
                    pictureDto.setPicture(pictures);
                    pictureDtoList.add(pictureDto);
                }
            }

            return R.success(pictureDtoList);


        }
        else{
            return R.error("查询失败");
        }

//        Long userid = Long.valueOf(request.getSession().getAttribute("user").toString());


    }
}
