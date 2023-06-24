package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.dto.AssetDto;
import org.example.dto.BillDto;
import org.example.entity.Bill;
import org.example.entity.Diary;
import org.example.entity.Kind;
import org.example.service.BillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/asset")
public class AssetController {
    @Autowired
    private BillService billService;

    private float insum ;
    private float outsum ;
    private int innum ;
    private int outnum ;

    @PostMapping("/bill")
    public R<List> getAsset(HttpServletRequest request, @RequestBody AssetDto assetDto){

        if(request.getSession().getAttribute("userid")!=null){
            Long userid  = Long.valueOf(request.getSession().getAttribute("userid").toString());

            long id = userid;
            assetDto.setUserid(id);
            LocalDateTime startTime ;
            LocalDateTime endTime ;
            if(assetDto.getStarttime()!=null && assetDto.getEndtime()!=null){
                startTime = assetDto.getStarttime();
                endTime = assetDto.getEndtime();
            }else{
                startTime = LocalDateTime.parse("2003-01-01T00:00:00.000");
                endTime = LocalDateTime.now();
            }

            LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Bill::getUserid,id);
            queryWrapper.ge(Bill::getTime,startTime).lt(Bill::getTime,endTime);
            queryWrapper.orderByDesc(Bill::getTime);
            Page<Bill> billDtoPage = new Page<>(1,10000);

            billService.page(billDtoPage,queryWrapper);

            List<Bill> records = billDtoPage.getRecords();

            return R.success(records);
        }
        else{
            return R.error("查询失败");
        }



    }

    @PostMapping("/num")
    public R<AssetDto> getNum(HttpServletRequest request, @RequestBody AssetDto assetDto){

        if(request.getSession().getAttribute("userid")!=null){
            Long userid  = Long.valueOf(request.getSession().getAttribute("userid").toString());

            insum = 0;
            outsum = 0;
            innum = 0;
            outnum = 0;

            long id = userid;
            assetDto.setUserid(id);
            LocalDateTime startTime ;
            LocalDateTime endTime ;
            if(assetDto.getStarttime()!=null && assetDto.getEndtime()!=null){
                startTime = assetDto.getStarttime();
                endTime = assetDto.getEndtime();
            }else{
                startTime = LocalDateTime.parse("2003-01-01T00:00:00.000");
                endTime = LocalDateTime.now();
            }


            LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(Bill::getUserid,id);
            queryWrapper.ge(Bill::getTime,startTime).lt(Bill::getTime,endTime);

            Page<Bill> billDtoPage = new Page<>(1,10000);

            billService.page(billDtoPage,queryWrapper);

            List<Bill> records = billDtoPage.getRecords();

//        float num=0;

            List<Bill> list = records.stream().map((item)->{
//            AssetDto assetDto1 = new AssetDto();
//            BeanUtils.copyProperties(item,assetDto1);
//            int KindId = item.getKindid();
//            int DiaryId = item.getPageid();
                if(item.getType()==0){
                    outsum += item.getNumber();
                    outnum++;
                }
                if(item.getType()==1){
                    insum += item.getNumber();
                    innum++;
                }
//            assetDto1.setOutcome(outsum);
//            assetDto1.setIncome(insum);
//            assetDto1.setIncomeNum(innum);
//            assetDto1.setOutcomeNum(outnum);

                Bill bill = new Bill();
                return bill;
            }).collect(Collectors.toList());

//        billDtoPage.setRecords(list);
//        return R.success(billDtoPage);
            assetDto.setOutcome(outsum);
            assetDto.setIncome(insum);
            assetDto.setIncomeNum(innum);
            assetDto.setOutcomeNum(outnum);
            assetDto.setStarttime(startTime);
            assetDto.setEndtime(endTime);

            return R.success(assetDto);
        }
        else{
            return R.error("查询失败");
        }

    }

}
