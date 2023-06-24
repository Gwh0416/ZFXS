package org.example.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Bill;
import org.example.entity.Diary;
import org.example.entity.Logg;
import org.example.entity.User;
import org.example.service.BillService;
import org.example.service.DiaryService;
import org.example.service.UserService;
import org.example.utils.IdUtil;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private BillService billService;

    @GetMapping
    public R<User> get(HttpServletRequest request) {

        if (request.getSession().getAttribute("userid") != null) {
            Long userid = Long.valueOf(request.getSession().getAttribute("userid").toString());
            User user = userService.getById(userid);

            LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Bill::getUserid,userid).eq(Bill::getType,0).eq(Bill::getIsdelete,0);
            int outcomeNum = 0;
            outcomeNum = billService.count(queryWrapper);
            float outcomeSum = 0;
            for (Bill bill : billService.list(queryWrapper)) {
                outcomeSum+=bill.getNumber();
            }

            LambdaQueryWrapper<Bill> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Bill::getUserid,userid).eq(Bill::getType,1).eq(Bill::getIsdelete,0);
            int incomeNum = 0;
            incomeNum = billService.count(queryWrapper1);
            float incomeSum = 0;
            for (Bill bill : billService.list(queryWrapper1)) {
                incomeSum+=bill.getNumber();
            }

            user.setOutcomenum(outcomeNum);
            user.setOutcome(outcomeSum);

            user.setIncomenum(incomeNum);
            user.setIncome(incomeSum);


            userService.updateById(user);
            return R.success(user);
        }
        return R.error("查询用户信息失败");

    }

    @GetMapping("/autoLogin")
    public R<User> autoLogin(HttpServletRequest request, HttpServletResponse response) {
        for (Cookie coo : request.getCookies()) {
            if (coo.getName().equals("token")) {
                String token = coo.getValue();

                if (token != null) {
                    String result = JwtUtil.verify(token);

                    if (result != null) {
                        log.info("用户已登录，用户id为：{}", result);
                        Long userid = Long.valueOf(result);
                        request.getSession().setAttribute("userid", userid);
                        User user = userService.getById(userid);

                        return R.success(user);
                    }else {
                        return R.error("用户登录过期，请重新登录");
                    }
                }else {
                    return R.error("获取用户信息失败，已跳转到登录页面");
                }
            }
        }
        return R.error("获取用户信息失败，已跳转到登录页面");
    }



    @PostMapping("/login")
    public R<User> login(HttpServletRequest request,HttpServletResponse response,@RequestBody User user){

        String password = user.getPassword();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getPhone,user.getPhone());

        User us = userService.getOne(queryWrapper);

        //        判断
        if(us==null ||!us.getPassword().equals(password)){
            return R.error("手机号或密码错误");
        }

//        查看状态
        if(us.getState()==0){
            return R.error("账户已禁用");
        }

//        登陆成功
        String token = JwtUtil.sign(us.getUserid().toString(),us.getPassword());


        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(604800);
        cookie.setPath("/");
        response.addCookie(cookie);

        HttpSession session = request.getSession(true);
        session.setAttribute("userid",us.getUserid());

        return R.success(us);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request,HttpServletResponse response){

        request.getSession().removeAttribute("userid");

        Cookie newCookie=new Cookie("token","");
        newCookie.setMaxAge(0);
        response.addCookie(newCookie);

        return R.success("退出成功");
    }

    @PostMapping("/regist")
    public R<String> save (HttpServletRequest request,@RequestBody User user){

        user.setUserid(IdUtil.IdCreate());

        if(user.getUrl()==null ||user.getUrl().length()==0){
            user.setUrl("zfxs.jpg");
        }

        user.setState(1);
        user.setTime(LocalDateTime.now());
        user.setIncome(0);
        user.setOutcome(0);
        user.setIncomenum(0);
        user.setOutcomenum(0);
//        R r = new R();
//        if(user.getUserName()==null){
//            return R.error("未填写用户名");
//        }
//        if(user.getAge()==){
//            return R.error("");
//        }
//        if(user.getPhone()==null){
//            return R.error("未填写手机号");
//        }
//        if(user.getPassword()==null){
//            return R.error("未填写密码");
//        }



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
            return R.success("注册成功");
        }

        return R.error("注册失败");
    }

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody User user){


        if(userService.updateById(user)){
            return R.success("修改成功");
        }
        else {
            return R.error("修改失败");
        }

    }

}
