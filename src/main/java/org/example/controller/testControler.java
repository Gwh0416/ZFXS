package org.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.ImageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/test")
public class testControler {

    @Value("${zfxs.path}")
    private String basePath;

    @PostMapping()
    public void process(HttpServletResponse response, MultipartFile file, int enhance_mode) throws IOException {


        String originalFilename = file.getOriginalFilename();

        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = UUID.randomUUID().toString() + substring;

//        判断目录是否存在
        File dir = new File(basePath);

        if(!dir.exists()){
            dir.mkdirs();
        }

//        将临时文件转存到指定位置
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String url = "https://api.textin.com/ai/service/v1/crop_enhance_image";

        String appId = "d6a8daf75ad864e0b51b6c5ed55c90f1";

        String secretCode = "cdbe5c26e319d4d21e8602c0de6b73a2";
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readfile(basePath+fileName); // image
            URL realUrl = new URL(url+"?enhance_mode="+enhance_mode);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("x-ti-app-id", appId);
            conn.setRequestProperty("x-ti-secret-code", secretCode);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
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
//        System.out.println(result);
        JSONObject jsonObject =  JSON.parseObject(result);
//        String  result1 = jsonObject.getString("result");
        JSONArray result1 = JSON.parseObject(jsonObject.getString("result")).getJSONArray("image_list");

        JSONObject jsonObject1 = JSON.parseObject(result1.get(0).toString());
        String image = jsonObject1.getString("image");

        ByteArrayInputStream byteArrayInputStream = ImageUtil.convertBase64StrToImage(image);
        ServletOutputStream outputStream = response.getOutputStream();

        response.setContentType("image/jpeg");

        int len = 0;
        byte[] bytes = new byte[1024];
        while ((len = byteArrayInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
//          关闭资源
        outputStream.close();

//        return image;
    }


    public static byte[] readfile(String path)
    {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}


