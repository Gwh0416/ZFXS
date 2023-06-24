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
import org.example.entity.Bill;
import org.example.utils.ImageUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api/app/v1/")
public class ImageProcessController {

    @PostMapping("/process")
    public void process(HttpServletResponse response, @RequestParam("file") MultipartFile file, int enhance_mode) throws IOException {

        String url = "https://api.textin.com/ai/service/v1/crop_enhance_image";

        String appId = "d6a8daf75ad864e0b51b6c5ed55c90f1";

        String secretCode = "cdbe5c26e319d4d21e8602c0de6b73a2";
        BufferedReader in = null;
        DataOutputStream out = null;
        String result = "";
        try {
            byte[] imgData = readfile(file); // image
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

