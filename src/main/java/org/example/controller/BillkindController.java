package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Bill;
import org.example.entity.Kind;
import org.example.service.BillkindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1")
public class BillkindController {

    @Autowired
    private BillkindService billkindService;

    @GetMapping("/billkind0")
    public R<List> billfind0(HttpServletRequest request){

        LambdaQueryWrapper<Kind> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(Kind::getKindid);
        queryWrapper.eq(Kind::getType,0);

        return R.success(billkindService.list(queryWrapper));
    }

    @GetMapping("/billkind1")
    public R<List> billfind1(HttpServletRequest request){

        LambdaQueryWrapper<Kind> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(Kind::getKindid);
        queryWrapper.eq(Kind::getType,1);

        return R.success(billkindService.list(queryWrapper));
    }

}
