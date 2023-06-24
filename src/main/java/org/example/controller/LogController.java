package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.common.R;
import org.example.entity.Logg;
import org.example.entity.User;
import org.example.service.LogService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/web/v1/log")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

    //    分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        log.info("page = {},pageSize = {}",page,pageSize);
//        构造分页构造器
        Page pageInfo = new Page(page,pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Logg> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件
//        queryWrapper.like(StringUtils.isNotEmpty(name),Logg::getName,name);
//        添加排序条件
        queryWrapper.orderByDesc(Logg::getLogid);
        logService.page(pageInfo,queryWrapper);
//        执行查询
        return R.success(pageInfo);
    }

}
