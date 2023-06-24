package org.example.controller;

import org.example.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/app/v1/common")
@Slf4j
public class CommonController {

    @Value("${zfxs.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
     public R<String> upload(MultipartFile file){

//        log.info(file.toString());

//        获取源文件名
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
        return R.success(fileName);
     }

//     下载
    @GetMapping("/download")
    public OutputStream download(String url, HttpServletResponse response) throws IOException {

        try {
//            输入流
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + url));
//            输出流
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(url.getBytes()));
//            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
//          关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        System.out.println(response.getOutputStream().toString());
        return response.getOutputStream();
    }
}
