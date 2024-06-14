package com.nowcoder.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//表示这个注解用于方法上（method）
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {


}

//自定义注解，有这个标记就登录以后才能执行，没有就不
