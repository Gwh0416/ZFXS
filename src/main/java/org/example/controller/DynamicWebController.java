package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.dto.PictureDto;
import org.example.entity.Dynamic;
import org.example.entity.Dynamicpic;
import org.example.service.DynamicPicService;
import org.example.service.DynamicService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/web/v1/dynamic")
public class DynamicWebController {

    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private DynamicPicService dynamicPicService;


    @GetMapping("/detail")
    public R<PictureDto> getDynamicBydynamicId(HttpServletRequest request, Long dynamicid){

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

        return R.success(pictureDto);

    }

    @GetMapping()
    public R<Page> getDynamicByUser(HttpServletRequest request,int page, int pageSize, String content){

        LambdaQueryWrapper<Dynamic> queryWrapper = new LambdaQueryWrapper<>();

//        Page pageInfo = new Page(page,pageSize);
        Page<Dynamic> pageInfo = new Page<>(page,pageSize);
        Page<PictureDto> pictureDtoPage = new Page<>();
        queryWrapper.eq(Dynamic::getIsdelete,1);
//        queryWrapper.eq(Dynamic::getIsdelete,"0");
        queryWrapper.orderByDesc(Dynamic::getTime);
        if(content!=null){
            queryWrapper.like(Dynamic::getTitle,content).or().like(Dynamic::getContent,content);
        }


        dynamicService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,pictureDtoPage,"records");
//        List<Dynamic> dynamicList = dynamicService.page(pageInfo,queryWrapper);
        List<Dynamic> dynamicList = pageInfo.getRecords();
//        pageInfo.setTotal(9);

//        return R.success(pageInfo);

        List<PictureDto> pictureDtoList = new ArrayList<>();
        for(int i=0;i<dynamicList.size();i++){
            Long dynamicId = dynamicList.get(i).getDynamicid();

            LambdaQueryWrapper<Dynamicpic> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Dynamicpic::getDynamicid,dynamicId);
            queryWrapper1.orderByAsc(Dynamicpic::getNum);
//            queryWrapper1.eq(Dynamicpic::getIsdelete,0);
            List<Dynamicpic> dynamicPicList = dynamicPicService.list(queryWrapper1);

            if(dynamicPicList.size()!=0){
                String[] pictures = new String[dynamicPicList.size()];
                for(int j=0;j<dynamicPicList.size();j++){
                    pictures[j] = dynamicPicList.get(j).getUrl();
                }
                if(dynamicList.get(i).getIsdelete()==0){
                    PictureDto pictureDto = new PictureDto();
                    pictureDto.setDynamicid(dynamicId);
                    pictureDto.setUserid(dynamicList.get(i).getUserid());
                    pictureDto.setTime(dynamicList.get(i).getTime());
                    pictureDto.setContent(dynamicList.get(i).getContent());
                    pictureDto.setPicture(pictures);
                    pictureDto.setTitle(dynamicList.get(i).getTitle());
                    pictureDto.setIsdelete(dynamicList.get(i).getIsdelete());
                    pictureDtoList.add(pictureDto);

                }


            }

        }
        pictureDtoPage.setTotal(pictureDtoList.size());

        pictureDtoPage.setRecords(pictureDtoList);

//        return R.success(pictureDtoList);

        return R.success(pictureDtoPage);
    }

    @DeleteMapping
    public R<String> delete(HttpServletRequest request,Long dynamicid){

        Dynamic dynamic = dynamicService.getById(dynamicid);
        dynamic.setIsdelete(1);
        dynamicService.updateById(dynamic);

        return R.success("删除成功");
    }
}
