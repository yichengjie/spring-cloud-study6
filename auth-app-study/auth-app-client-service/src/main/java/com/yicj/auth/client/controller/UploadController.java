package com.yicj.auth.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Value("${custom.uploadFilePath}")
    private String uploadFilePath ;

    @GetMapping("/getFile")
    public String getFile(){
        return "/upload";
    }

    //处理文件上传的方法
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        //根据相对获取绝对路径(文件上传到的保存位置)
        //获取文件后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        //文件名我这里使用UUID和时间组成的
        String newFileNamePrefix= UUID.randomUUID().toString().replace("-","")+
                new SimpleDateFormat("yyyyMMddHHssSSS").format(new Date());
        String newFileName=newFileNamePrefix+"."+extension;
        //处理文件上传
        file.transferTo(new File(uploadFilePath,newFileName));
        //上传成功后跳转到的路径
        return "success";
    }

}
