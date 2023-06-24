package org.example.filter;

//检查用户是否完成登录

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Logg;
import org.example.service.LogService;
import org.example.utils.IdUtil;
import org.example.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*",asyncSupported=true)
//@WebFilter(filterName = "CrossFilter", urlPatterns = "/*",asyncSupported=true)
@Slf4j
public class LoginCheckFilter implements Filter {

    @Autowired
    private LogService logService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

//    路径匹配，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

//        获取uri
        String requestURI = request.getRequestURI();

        log.info("拦截到请求：{}",requestURI);

//        定义不需要处理的url
        String[] urls = new String[]{
                "/api/app/v1/user/login",
                "/api/web/**",
//                "/api/web/v1/**",
//                "/api/app/v1/**",
//                "/backend/**",
//                "/load",
//                "/test",
//                "/121.43.147.9:8080/**",
                "/**"
        };
//        判断是否需要处理
        boolean check = check(urls, requestURI);

//        如果不需要处理，放行
        if(check){
            log.info("该请求放行：{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        for (Cookie coo : request.getCookies()) {
            if(coo.getName().equals("token")){
                String token = coo.getValue();

                if(token!=null){
                    String result = JwtUtil.verify(token) ;

                    if(result!=null){
                        log.info("用户已登录，用户id为：{}",result);
                        String ip = request.getRemoteAddr();
                        String url = request.getRequestURL().toString();
                        String method = request.getMethod();
                        Long userid = Long.valueOf(result);
                        request.getSession().setAttribute("userid",userid);


                        Logg logg = new Logg();
                        logg.setLogid(IdUtil.IdCreate());
                        logg.setIp(ip);
                        logg.setMethod(method);
                        logg.setTime(LocalDateTime.now());
                        logg.setUrl(url);
                        logg.setUserid(userid);
                        logService.save(logg);

                        filterChain.doFilter(request,response);
                    }
                    else{
                        log.info("token过期");
                        request.getSession().removeAttribute("userid");
                        request.getSession().removeAttribute("token");
                        response.getWriter().write(JSON.toJSONString(R.notLogin("NOTOKEN")));
                    }
                    return;
                }
                log.info("用户未登录");
                response.getWriter().write(JSON.toJSONString(R.notLogin("NOTLOGIN")));

                break;
            }
        }

//        如果未登录，返回响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.notLogin("NOTLOGIN")));
        return;

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public boolean check(String [] urls,String requestURI){
        for (String url :urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }

}
