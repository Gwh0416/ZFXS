package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.dto.BillDto;
import org.example.entity.*;
import org.example.service.BillService;
import org.example.service.BillkindService;
import org.example.service.DiaryService;
import org.example.service.UserService;
import org.example.utils.IdUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/bill")
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private BillkindService billkindService;
    @Autowired
    private DiaryService diaryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public R<Bill> getBill(HttpServletRequest request,Long billid){
        Bill bill = billService.getById(billid);
        return R.success(bill);
    }

    @PostMapping("/add/0")
    public R<Long> add0(HttpServletRequest request, @RequestBody Bill bill){

        Long userid = Long.valueOf(request.getSession().getAttribute("userid").toString());
        bill.setUserid(userid);
        bill.setIsdelete(0);
        bill.setBillid(IdUtil.IdCreate());
        bill.setType(0);
        if(bill.getTime()==null || bill.getTime().toString().length()==0){
            bill.setTime(LocalDateTime.now());
        }

        Long diaryid = bill.getPageid();
        Diary diary = diaryService.getById(diaryid);
        float outcome2 = diary.getOutcome();
        int outcomeNum2 = diary.getOutcomenum();
        outcome2+=bill.getNumber();
        outcomeNum2++;
        diary.setOutcomenum(outcomeNum2);
        diary.setOutcome(outcome2);

        if(billService.save(bill)&&diaryService.updateById(diary)){
            return R.success(bill.getBillid());
        }
        return R.error("添加失败");
    }
    @PostMapping("/add/1")
    public R<Long> add1(HttpServletRequest request, @RequestBody Bill bill){

        Long userid = Long.valueOf(request.getSession().getAttribute("userid").toString());
        bill.setUserid(userid);

        bill.setIsdelete(0);
        bill.setBillid(IdUtil.IdCreate());
        bill.setType(1);
        if(bill.getTime()==null || bill.getTime().toString().length()==0){
            bill.setTime(LocalDateTime.now());
        }



        Long diaryid = bill.getPageid();
        Diary diary = diaryService.getById(diaryid);
        float income2 = diary.getIncome();
        int incomeNum2 = diary.getIncomenum();
        income2+=bill.getNumber();
        incomeNum2++;
        diary.setIncomenum(incomeNum2);
        diary.setIncome(income2);
//        diaryService.updateById(diary);
//        userService.updateById(user);
//        billService.save(bill);

        if(billService.save(bill)&&diaryService.updateById(diary)){
            return R.success(bill.getBillid());
        }
        return R.error("添加失败");
    }



    @DeleteMapping
    public R<String> delete(HttpServletRequest request,String billid){

        Bill bill = billService.getById(billid);
        bill.setIsdelete(1);
        billService.updateById(bill);

        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Bill bill){

        billService.updateById(bill);
        return R.success("修改成功");
    }

    @GetMapping("/time")
    public R<List> getTimeBill(HttpServletRequest request,String diaryid,String time){
        if(diaryid!=null){

//            queryWrapper.ge(Bill::getTime,startTime).lt(Bill::getTime,endTime);

            String time1 = time.toString();
            String time2 = time1.substring(0,7);
//            log.info(time2);

//        构造条件构造器
            LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件
            queryWrapper.eq(diaryid!=null,Bill::getPageid,diaryid);
            queryWrapper.eq(Bill::getIsdelete,0);
//        添加排序条件
            queryWrapper.orderByDesc(Bill::getTime);

            List<Bill> bills  = billService.list(queryWrapper);

            List<BillDto> billDtoList = new ArrayList<>();

            for(int i = 0;i<bills.size();i++){
                Bill bill1 = bills.get(i);
                if(bill1.getTime().toString().substring(0,7).equals(time2)){

                    BillDto billDto = new BillDto();
                    Long KindId = bill1.getKindid();
                    Long DiaryId = bill1.getPageid();
                    BeanUtils.copyProperties(bill1,billDto);

                    Kind kind = billkindService.getById(KindId);
                    Diary diary = diaryService.getById(DiaryId);

                    if(kind!=null){
                        String kindName = kind.getContent();
                        billDto.setKindName(kindName);
                    }
                    if(diary!=null){
                        String diaryName = diary.getContent();
                        billDto.setDiaryName(diaryName);
                    }

                    billDtoList.add(billDto);

                }

            }

//            List<BillDto> billDtos = bills.stream().map((item)->{
//                if(item.getTime().toString().substring(0,7).equals(time2)){
//                    BillDto billDto = new BillDto();
//                    BeanUtils.copyProperties(item,billDto);
//                    Long KindId = item.getKindid();
//                    Long DiaryId = item.getPageid();
//
//                    Kind kind = billkindService.getById(KindId);
//                    Diary diary = diaryService.getById(DiaryId);
//
//                    if(kind!=null){
//                        String kindName = kind.getContent();
//                        billDto.setKindName(kindName);
//                    }
//                    if(diary!=null){
//                        String diaryName = diary.getContent();
//                        billDto.setDiaryName(diaryName);
//                    }
//
//                    return billDto;
//                }else {
//                    return null;
//                }
//
//            }).collect(Collectors.toList());

//            if(billDtos.size()==0){
//                List list = new ArrayList();
//                return R.success(list);
//            }

            if(billDtoList.size()==0){
                List list = new ArrayList();
                return R.success(list);
            }

            return R.success(billDtoList);
        }

        else{
            return R.error("查询失败");
        }
    }

    @GetMapping("/diary")
    public R<List> getBill(HttpServletRequest request, String diaryid){

        if(diaryid!=null){

//        构造条件构造器
            LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件
            queryWrapper.eq(diaryid!=null,Bill::getPageid,diaryid);
            queryWrapper.eq(Bill::getIsdelete,0);
//        添加排序条件
            queryWrapper.orderByDesc(Bill::getTime);

            List<Bill> bills  = billService.list(queryWrapper);

            List<BillDto> billDtos = bills.stream().map((item)->{
                BillDto billDto = new BillDto();
                BeanUtils.copyProperties(item,billDto);
                Long KindId = item.getKindid();
                Long DiaryId = item.getPageid();

                Kind kind = billkindService.getById(KindId);
                Diary diary = diaryService.getById(DiaryId);

                if(kind!=null){
                    String kindName = kind.getContent();
                    billDto.setKindName(kindName);
                }
                if(diary!=null){
                    String diaryName = diary.getContent();
                    billDto.setDiaryName(diaryName);
                }

                return billDto;
            }).collect(Collectors.toList());


            return R.success(billDtos);
        }

        else{
            return R.error("查询失败");
        }

    }


    @GetMapping("/user")
    public R<List> getBillByUser(HttpServletRequest request){

//        if(request.getSession().getAttribute("userid")!=null){
//            Long userid  = Long.valueOf(request.getSession().getAttribute("userid").toString());
//        }
//        else{
//            return R.error("查询失败");
//        }

        if(request.getSession().getAttribute("userid")!=null){
            Long userid  = Long.valueOf(request.getSession().getAttribute("userid").toString());

//        构造条件构造器
            LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件
            queryWrapper.eq(userid!=null,Bill::getUserid,userid);
            queryWrapper.eq(Bill::getIsdelete,0);
//        添加排序条件
            queryWrapper.orderByDesc(Bill::getTime);

            List<Bill> bills  = billService.list(queryWrapper);

            List<BillDto> billDtos = bills.stream().map((item)->{
                BillDto billDto = new BillDto();
                BeanUtils.copyProperties(item,billDto);
                Long KindId = item.getKindid();
                Long DiaryId = item.getPageid();

                Kind kind = billkindService.getById(KindId);
                Diary diary = diaryService.getById(DiaryId);

                if(kind!=null){
                    String kindName = kind.getContent();
                    billDto.setKindName(kindName);
                }
                if(diary!=null){
                    String diaryName = diary.getContent();
                    billDto.setDiaryName(diaryName);
                }

                return billDto;
            }).collect(Collectors.toList());

            return R.success(billDtos);

        }
        else{
            return R.error("查询失败");
        }
    }
}
