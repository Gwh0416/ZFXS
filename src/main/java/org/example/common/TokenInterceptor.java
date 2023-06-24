//package org.example.common;
//
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.example.utils.JwtUtil;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @program: smartOperationTest
// * @description: 拦截器
// **/
//@Component
//@Slf4j
//public class TokenInterceptor implements HandlerInterceptor {
//
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws IOException {
//        log.info("preHandle begin.");
//        response.setCharacterEncoding("utf-8");
//
//        String token = request.getSession().getAttribute("token").toString();
//        log.info("preHandle begin TOKEN: {}." + token);
//        // token通过验证 返回true 继续访问
//        if (null != token) {
//            boolean result = JwtUtil.verify(token) ;
//            if (result) {
//                log.info("preHandle end.");
//                return true;
//            }
//        }
//        // token验证不通过 返回失败提示
////        R r = R.init(null, -1, "failed", "token invalid");
//        R r = new R();
//        r.setMsg("验证失败");
//        response.getWriter().write(JSONObject.toJSONString(r));
//        log.info("preHandle end.");
//        return false;
//    }
//}
