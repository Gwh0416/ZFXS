package org.example.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.example.common.R;
import org.example.entity.Bill;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/")
class SpeechController {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @PostMapping("/speech")
    public R<Bill> speech(MultipartFile file) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");

        long size = file.getSize();

        String speech = null;

        byte[] byt = null;
        try {
            byt = new byte[file.getInputStream().available()];
            file.getInputStream().read(byt);
            speech= new BASE64Encoder().encodeBuffer(byt);
            speech = speech.replaceAll("[\\s*\t\n\r]", "");
        }catch (Exception e){
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(mediaType, "{\"format\":\"wav\",\"rate\":16000,\"channel\":1,\"cuid\":\"1\",\"token\":\""+getAccessToken()+"\",\"speech\":\""+speech+"\",\"len\":"+size+"}");
        Request request = new Request.Builder()
                .url("https://vop.baidu.com/server_api")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();

        JSONObject jsonObject =  JSON.parseObject(response.body().string());

        if(jsonObject.getString("result")!=null){
            String result = jsonObject.getString("result");
            int size1 = result.length();
//        System.out.println(result.substring(2,size1-2));
            String inputStr = result.substring(2,size1-2);


            String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";

            String aa = "";

            Pattern p = Pattern.compile(regEx);

            Matcher m = p.matcher(inputStr);

            inputStr = m.replaceAll(aa).trim();

//        System.out.println(inputStr);


//        inputStr = new String(inputStr.getBytes(), StandardCharsets.UTF_8);
            inputStr = convert(inputStr);


            String url = "http://127.0.0.1:8989/voice/"+inputStr;
//        log.info(inputStr);
//        log.info(url);
            InputStream is = null;
            BufferedReader br = null;
            String result1 = null;// 返回结果字符串

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
                    result1 = sbf.toString();
                }else {
                    return R.error("识别失败");
                }
                conn.disconnect();

            }catch (Exception e){
                System.out.println("发送 GET 请求出现异常！" + e);
                e.printStackTrace();
            }
            if(result1.length()==0){
                return R.error("识别失败");
            }
            log.info(result1);
            String[] str = result1.split("_!_");
            String KindStr = str[2];
            log.info(KindStr);

            KindStr = convert(KindStr);

            String urls = "http://47.100.60.29:80/model/"+KindStr;
//        log.info(inputStr);
            log.info(urls);
            is = null;
            br = null;
            String results = null;// 返回结果字符串

            try{
                URL Url = new URL(urls);
//            log.info(Url.toString());
                HttpURLConnection conn1 = (HttpURLConnection) Url.openConnection();
                conn1.setRequestMethod("GET");
                conn1.setRequestProperty("connection", "Keep-Alive");
                conn1.setRequestProperty("Content-Type", "application/octet-stream");

                conn1.connect();
                if(conn1.getResponseCode()==200){
                System.out.println("请求成功");
                    is = conn1.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                    StringBuffer sbf = new StringBuffer();
                    String temp = null;
                    while ((temp = br.readLine())!=null){
                        sbf.append(temp);
                    }
                    results = sbf.toString();
                }else {
                    results = "9";
                }
                conn1.disconnect();

            }catch (Exception e){
//            System.out.println("发送 GET 请求出现异常！" + e);
                e.printStackTrace();
            }finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            Bill bill = new Bill();
            bill.setKindid(Long.valueOf(results));
            bill.setContent(result.substring(2,size1-2));
            bill.setNumber(Float.parseFloat(str[0]));
            bill.setType(0);


            int length = str[1].length();
            String str11 =str[1].substring(0,10);
            String str22 = str[1].substring(12,length);
//        System.out.println(length);
//        System.out.println(str11);
//        System.out.println(str22);
//        System.out.println(LocalDateTime.parse(str11+"T0"+str22));
            if(length==19){
                bill.setTime(LocalDateTime.parse(str11+"T0"+str22));
            }else {
                bill.setTime(LocalDateTime.parse(str11+"T"+str22));
            }

            return R.success(bill);
        }
        return R.error("识别失败");
    }

    static String getAccessToken() throws IOException {

        String API_KEY = "4ZELsFQMEP4B7GGKqebznTaQ";
        String SECRET_KEY = "7pLAbgRvC2WVElnlQ8vw2TIF2YQfDTDR";
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return JSON.parseObject(response.body().string()).getString("access_token");
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