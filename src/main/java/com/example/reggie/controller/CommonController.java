package com.example.reggie.controller;

import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 处理文件上传下载的Controller
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.upload-file}")
    private String uploadPath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //file为一个临时文件，需要转存，否则本次请求后会自动清除

        //获取上传文件名
        String originalFilename = file.getOriginalFilename();
        //截取文件名后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID重新生成文件名防止文件名重复
        String newFilename = UUID.randomUUID().toString()+substring;
        File dir = new File(uploadPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        log.info("保存路径"+uploadPath+newFilename);
        try {
            //将文件另存
            file.transferTo(new File(uploadPath+newFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将新文件名返回给界面保存到数据库
        return R.success(newFilename);
    }

    @GetMapping("/download")
    public void getImge(String name, HttpServletResponse response){
        try (FileInputStream file = new FileInputStream(uploadPath+name);
             ServletOutputStream outputStream = response.getOutputStream()){
            log.info("下載路徑"+uploadPath+name);
            byte[] bytes = new byte[1024];
            int len= 0;
            while ((len = file.read(bytes))!=-1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
