package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

//    @Test
//    public void testTextMail() {
//        mailClient.sendMail("zhourui2009ar@126.com", "TEST", "Welcome.");
//    }}

//    @Test
//    public void testHtmlMail() {
//        Context context = new Context();
//        context.setVariable("username", "sunday");
//
//        String content = templateEngine.process("/mail/demo", context);
//        System.out.println(content);
//
//        mailClient.sendMail("zhourui2009ar@126.com", "HTML", content);
//    }
}