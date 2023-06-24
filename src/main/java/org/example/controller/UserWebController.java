package org.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.example.common.R;
import org.example.entity.Diary;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.service.DiaryService;
import org.example.service.UserService;
import org.example.utils.IdUtil;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/web/v1/user")
public class UserWebController {

    @Autowired
    private UserService userService;
    @Autowired
    private DiaryService diaryService;

    //    分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

//        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

//        构造分页构造器
        Page pageInfo = new Page(page,pageSize);
//        构造条件构造器
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),User::getUsername,name);
//        添加排序条件
        queryWrapper.orderByDesc(User::getUserid);
//        执行查询
        userService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @PostMapping("/add")
    public R<String> add(HttpServletRequest request,@RequestBody User user){
        user.setUserid(IdUtil.IdCreate());
        user.setState(1);
        user.setTime(LocalDateTime.now());
        if(user.getUrl()==null ||user.getUrl().length()==0){
            user.setUrl("zfxs.jpg");
        }
        user.setIncome(0);
        user.setOutcome(0);
        user.setIncomenum(0);
        user.setOutcomenum(0);

        Diary diary = new Diary();

        diary.setUserid(user.getUserid());
        diary.setContent("日常账本");

        diary.setTime(LocalDateTime.now());
        diary.setDiaryid(IdUtil.IdCreate());
        diary.setIncome(0);
        diary.setIncomenum(0);
        diary.setOutcome(0);
        diary.setOutcomenum(0);
        diary.setIsdelete(0);
        diary.setUrl("front_money.png");

        if(userService.save(user)&&diaryService.save(diary)){
            return R.success("添加成功");
        }

        return R.error("添加失败");
    }

    @GetMapping
    public R<User> getUser(HttpServletRequest request ,Long userid){
        User user = userService.getById(userid);
        return R.success(user);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody User user){
        userService.updateById(user);
        return R.success("修改成功");
    }

    @PostMapping("/state/0")
    public R<String> state0(HttpServletRequest request,Long id){

        User user = userService.getById(id);
        user.setState(0);
        userService.updateById(user);
        return R.success("状态修改成功");
    }

    @PostMapping("/state/1")
    public R<String> state1(HttpServletRequest request,Long id){

        User user = userService.getById(id);
        user.setState(1);
        userService.updateById(user);
        return R.success("状态修改成功");
    }


}
