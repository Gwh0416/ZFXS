package org.example.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/kindtest")
public class kindTest {

    @GetMapping("/image")
    public R<String> Test(HttpServletRequest request, String inputStr){

//        inputStr = new String(inputStr.getBytes(), StandardCharsets.UTF_8);
        inputStr = convert(inputStr);

        String url = "http://47.100.60.29:80/model/"+inputStr;
//        log.info(inputStr);
        log.info(url);
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串

        try{
            URL realUrl = new URL(url);
//            log.info(realUrl.toString());
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");

            conn.connect();
            if(conn.getResponseCode()==200){
                System.out.println("请求成功");
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine())!=null){
                    sbf.append(temp);
                }
                result = sbf.toString();

            }
            conn.disconnect();

        }catch (Exception e){
            System.out.println("发送 GET 请求出现异常！" + e);
            e.printStackTrace();
        }
        return R.success(result);

    }

    @GetMapping("/voice")
    public R<String[]>  Voice(HttpServletRequest request, String inputStr){
        String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";

        String aa = "";

        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(inputStr);

        inputStr = m.replaceAll(aa).trim();

//        System.out.println(inputStr);


//        inputStr = new String(inputStr.getBytes(), StandardCharsets.UTF_8);
//        inputStr = convert(inputStr);


        String url = "http://127.0.0.1:8989/voice/"+inputStr;
//        log.info(inputStr);
        log.info(url);
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串

        try{
            URL realUrl = new URL(url);
            log.info(realUrl.toString());
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");

            conn.connect();
            if(conn.getResponseCode()==200){
                System.out.println("请求成功");
                is = conn.getInputStream();
                br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine())!=null){
                    sbf.append(temp);
                }
                result = sbf.toString();

            }
            conn.disconnect();

        }catch (Exception e){
            System.out.println("发送 GET 请求出现异常！" + e);
            e.printStackTrace();
        }
        String[] str = result.split("_!_");
        return R.success(str);

    }

    private static String unicodeToCn(String unicode) {

        String[] strs = unicode.split("\\\\u");

        String returnStr = "";

        for (int i = 1; i < strs.length; i++) {

            returnStr += (char) Integer.valueOf(strs[i], 16).intValue();

        }
        return returnStr;

    }

    private static String cnToUnicode(String cn) {

        char[] chars = cn.toCharArray();

        String returnStr = "";

        for (int i = 0; i < chars.length; i++) {

            returnStr += "\\u" + Integer.toString(chars[i], 16);

        }

        return returnStr;

    }

    public static String convert(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }

}
