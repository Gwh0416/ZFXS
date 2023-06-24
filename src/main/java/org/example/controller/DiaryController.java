package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.common.R;
import org.example.entity.Bill;
import org.example.entity.Diary;
import org.example.entity.User;
import org.example.service.BillService;
import org.example.service.DiaryService;
import org.example.service.LikeService;
import org.example.service.UserService;
import org.example.utils.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.net.ftp.FtpClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private UserService userService;

    @Autowired
    private BillService billService;



    @GetMapping
    public R<List> page(HttpServletRequest request){

        if(request.getSession().getAttribute("userid")!=null){

            Long userid  = Long.valueOf(request.getSession().getAttribute("userid").toString());

            System.out.println(userid);
            LambdaQueryWrapper<Diary> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件
            queryWrapper.eq(Diary::getIsdelete,0);
            queryWrapper.eq(Diary::getUserid,userid).or().eq(Diary::getUserid1,userid).or().eq(Diary::getUserid2,userid);
//        添加排序条件
            queryWrapper.orderByAsc(Diary::getTime);

            List<Diary> list = diaryService.list(queryWrapper);



            return R.success(list);

        }else {
            return R.error("获取账本信息失败");
        }

    }

    @PostMapping("/add")
    public R<String> add(HttpServletRequest request,@RequestBody Diary diary){

        Long userid = Long.valueOf(request.getSession().getAttribute("userid").toString());

        diary.setUserid(userid);
        diary.setTime(LocalDateTime.now());
        diary.setDiaryid(IdUtil.IdCreate());
        diary.setIncome(0);
        diary.setIncomenum(0);
        diary.setOutcome(0);
        diary.setOutcomenum(0);

        diaryService.save(diary);
        return R.success(diary.getDiaryid().toString());
    }

    @DeleteMapping
    public R<String> state0(HttpServletRequest request,Long diaryid){

        Diary diary = diaryService.getById(diaryid);
        diary.setIsdelete(1);

        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getPageid,diaryid);
        for (Bill bill : billService.list(queryWrapper)) {
            bill.setIsdelete(1);
            billService.updateById(bill);
        }


        diaryService.updateById(diary);
        return R.success("删除成功");
    }


    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Diary diary){
        diary.setTime(LocalDateTime.now());
        diaryService.updateById(diary);

        return R.success("更新成功");
    }

}
