package org.example.controller;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.example.entity.Bill;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/")
public class ReceiptController {

    @PostMapping("/receipt")
    public R<Bill> receipt(HttpServletRequest request, MultipartFile file){

        String url = "https://api.textin.com/robot/v1.0/api/receipt";

        String appId = "d6a8daf75ad864e0b51b6c5ed55c90f1";

        String secretCode = "cdbe5c26e319d4d21e8602c0de6b73a2";
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = null;
        try {
            byte[] imgData = readfile(file); // image
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", appId);
            conn.setRequestProperty("x-ti-secret-code", secretCode);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            int i=0;
            while ((line = in.readLine()) != null) {
                result = line;
//                i++;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        JSONObject jsonObject =  JSON.parseObject(result);

//        String  result1 = jsonObject.getString("result");

        JSONArray result1 = JSON.parseObject(jsonObject.getString("result")).getJSONArray("item_list");

        JSONObject jsonObject0= JSON.parseObject(result1.get(0).toString());
        JSONObject jsonObject1= JSON.parseObject(result1.get(1).toString());
        JSONObject jsonObject3= JSON.parseObject(result1.get(3).toString());
        JSONObject jsonObject5= JSON.parseObject(result1.get(5).toString());
        if (jsonObject0.getString("value").length()==0 || jsonObject1.getString("value").length()==0
         || jsonObject5.getString("value").length()==0){
            return R.error("识别失败");

        }
        String str1 = jsonObject3.getString("value");
        String str2 = jsonObject5.getString("value");
        String inputStr = str1+str2;
//        inputStr.trim();
        inputStr = inputStr.replaceAll(" ","");
        inputStr =inputStr.replaceAll(",","");
        inputStr =inputStr.replaceAll("，","");
        inputStr =inputStr.replaceAll("\\n","");
        inputStr =inputStr.replaceAll("\\s*","");




//        String inputStr = jsonObject3.getString("value")+jsonObject5.getString("value");
//        log.info(inputStr);
        inputStr = convert(inputStr);

        String urls = "http://47.100.60.29:80/model/"+inputStr;
//        log.info(inputStr);
        log.info(urls);
        InputStream is = null;
        BufferedReader br = null;
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




        inputStr = convert(jsonObject1.getString("value"));

        String urls1 = "http://127.0.0.1:8989/time/"+inputStr;
//        log.info(inputStr);
//        log.info(urls);
        InputStream is1 = null;
        BufferedReader br1 = null;
        String results1 = null;// 返回结果字符串
        LocalDateTime resultTime = null;


        try{
            URL Url = new URL(urls1);
//            log.info(Url.toString());
            HttpURLConnection conn1 = (HttpURLConnection) Url.openConnection();
            conn1.setRequestMethod("GET");
            conn1.setRequestProperty("connection", "Keep-Alive");
            conn1.setRequestProperty("Content-Type", "application/octet-stream");

            conn1.connect();
            if(conn1.getResponseCode()==200){
//                System.out.println("请求成功");
                is1 = conn1.getInputStream();
                br1 = new BufferedReader(new InputStreamReader(is1,"UTF-8"));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br1.readLine())!=null){
                    sbf.append(temp);
                }
                results1 = sbf.toString();
                System.out.println(results1);
                resultTime =Timestamp.valueOf(results1).toLocalDateTime();
            }else {
                resultTime = LocalDateTime.now();
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
//
        bill.setNumber(Float.parseFloat(jsonObject0.getString("value")));
        bill.setTime(resultTime);

//        bill.setTime(Timestamp.valueOf(jsonObject1.getString("value")).toLocalDateTime());
        bill.setStore(jsonObject3.getString("value"));
        bill.setContent(jsonObject5.getString("value"));
        if(results!=null){
            bill.setKindid(Long.valueOf(results));
        }
        bill.setType(0);


        return R.success(bill);
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
            j = (c >>>8); //取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }


    public static byte[] readfile(MultipartFile file)
    {
//        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try
        {
            File imagefile = multipartFileToFile(file);

            in = new FileInputStream(imagefile);
//            in = file.getInputStream();
            data = new byte[in.available()];
            in.read(data);
            in.close();
            delteTempFile(imagefile);
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return data;
    }


    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }


    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }



    }
