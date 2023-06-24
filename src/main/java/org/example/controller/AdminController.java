package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/web/v1/")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, HttpServletResponse response, @RequestBody User user){

        String username = user.getPhone();
        String password = user.getPassword();

        if (username.equals("admin") && password.equals("123456") ){
            String token = JwtUtil.sign(username,password);


            HttpSession session = request.getSession();

            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(604800);
            cookie.setPath("/");
            response.addCookie(cookie);

            session.setAttribute("userid",0L);
            session.setAttribute("token",token);

            return R.success(user);
        }
        else{
            return R.error("登陆失败");
        }
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request,HttpServletResponse response){
//    清理session
//        R r = new R();
        request.getSession().removeAttribute("userid");
        request.getSession().removeAttribute("token");
        Cookie newCookie=new Cookie("token","");
        newCookie.setMaxAge(0);
        response.addCookie(newCookie);
//        r.setMsg("退出成功");
        return R.success("退出成功");
    }




}
