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
import javax.servlet.http.Part;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    @Value("${custom.uploadFilePath}")
    private String uploadRootPath ;

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
        file.transferTo(new File(uploadRootPath,newFileName));
        //上传成功后跳转到的路径
        return "success";
    }

    //处理文件上传的方法
    @PostMapping("/upload2")
    public String upload(HttpServletRequest request) throws Exception {
        request.setCharacterEncoding("UTF-8");
        //根据相对获取绝对路径(文件上传到的保存位置)
        Part part = request.getPart("file");
        //获得上传的文件名称
        String header = part.getHeader("Content-Disposition");//获得Content-Disposition的头信息
        int idx = header.lastIndexOf("filename=\"");//获取filename=“字符串的开始下标
        String fileName = header.substring(idx+"filename=\"".length(), header.length()-1);
        log.info("====> 文件名："+fileName);
        String extension = fileName.substring(fileName.lastIndexOf(".")) ;
        //文件名我这里使用UUID和时间组成的
        String newFileNamePrefix= UUID.randomUUID().toString().replace("-","")+
                new SimpleDateFormat("yyyyMMddHHssSSS").format(new Date());
        String newFileName=newFileNamePrefix+"."+extension;
        //获得文件内容：
        try (InputStream is = part.getInputStream();
             OutputStream os = new FileOutputStream(uploadRootPath+"/"+ newFileName)){
            byte[] bt = new byte[1024];
            int len = 0;
            while ((len = is.read(bt)) != -1) {
                os.write(bt, 0, len);
            }
        }
        //上传成功后跳转到的路径
        return "success";
    }

}
