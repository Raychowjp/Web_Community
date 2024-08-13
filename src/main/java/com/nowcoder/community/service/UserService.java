package com.nowcoder.community.service;

import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;


    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    //map是键值对集合
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            throw new IllegalArgumentException("参数为空"); /*throw是Java中用于抛出异常的关键字。
            new IllegalArgumentException()是创建一个IllegalArgumentException类的对象实例。*/
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }
        //验证账号是否存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            System.out.println(u.getUsername());
            System.out.println(u.getId());
            System.out.println(u.getEmail());
            System.out.println(u.getCreateTime());
            map.put("usernameMsg", "该账号已存在");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册");
            return map;
        }
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        //设置随机头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }
    public int activation(int userid, String code){
        User user = userMapper.selectById((userid));
        if (user.getStatus() == 1){
            return Activation_Repeated;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userid, 1);
            return Activation_Success;
        }else{
            return Activation_Failure;
        }

    }
    //登录的封装
    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>(); //键的类型为 String，值的类型为 Object
        //空值的处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg", "account can not be null");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg", "password can not be null");
            return map;
        }

        //验证账号密码
        User user = userMapper.selectByName(username);
        System.out.println("130" + user);
        if (user == null){  //数据库没有这个用户的信息
            map.put("usernameMsg", "the user does not exist");
            return map;
        }

        if(user.getStatus() == 0){
            map.put("usernameMsg","this account is not activated");
            return map;
        }

        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg","password is wrong");
            return map;
        }
        //在loginticket表里生成用户的数据
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000)); //换算成毫秒；
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket",loginTicket.getTicket());
        map.put("user", user);
        System.out.println("user-service" + loginTicket);
        return map;
    }

    //logout-status:1
    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket, 1);
    }

    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }


    //更新头像
    public int updateHeader(int userId, String headerUrl ){ //返回行数
        return userMapper.updateHeader(userId, headerUrl);
    }



}

