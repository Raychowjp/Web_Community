package com.nowcoder.community.controller;


import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

//处理用户相关的业务
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class); //日志
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    //取到当前用户是谁，hostholder
    @Autowired
    private HostHolder hostHolder;

    //点击设置，打开设置页面
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    //
    @LoginRequired
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if (headerImage == null){
            model.addAttribute("error", "no image");
            return "/site/setting";
        }
        //获取文件后缀
        String fileName = headerImage.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".")); //从最后一个点往后截取，截取到文件类型名
        if (StringUtils.isBlank(suffix)){
            model.addAttribute("error", "file format is wrong");
            return "/site/setting";
        }
        //生成随机的文件名
        fileName = CommunityUtil.generateUUID() + suffix;
        //指定存放路径
        File dest = new File(uploadPath + "/" + fileName);
        try {
            //存储图片文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("upload error" + e.getMessage());
            throw new RuntimeException("upload error" , e);
        }
        //更新用户当前头像的路径（web访问的路径）
        //首先获取当前用户
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + fileName;
        //更新用户的头像
        userService.updateHeader(user.getId(), headerUrl);
        return "redirect:/index";

    }

    @RequestMapping(path = "header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //找到这个图片的位置
        fileName = uploadPath + "/" + fileName;
        //解析文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 设置HTTP响应的内容类型（Content-Type）为图片类型
        response.setContentType("image/" + suffix);
        //通过输入流读取文件内容，并通过输出流将文件内容写入HTTP响应
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败: " + e.getMessage());
        }
    }

}
