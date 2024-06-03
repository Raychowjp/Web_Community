package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper   //声明一个注解，表示这是数据访问的对象
public interface LoginTicketMapper {

    //*************************插入**********************
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ", //sql语句,mysql表里的变量
            "values(#{userId}, #{ticket}, #{status}, #{expired}) " // values就是对应的值
    })
    @Options(useGeneratedKeys = true, keyProperty = "id") //自动生成主键， 赋值给id
    int insertLoginTicket(LoginTicket loginTicket); //插入一条ticket数据，通常返回整数，所在的行数；loginticket是在entity里定义的类。



    //*************查询*************
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"  //#{} 指的是entity定义的参数
    })
    LoginTicket selectByTicket(String ticket); //查询： 根据ticket查询id，user信息之类



    //***********修改*************
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"

    })
    int updateStatus (String ticket, int status); //修改登录的状态，status 0/1 ；



}
//实现mapper方法有两种，第一是在resource里面新建配置文件实现，也可以直接在mapper接口/本接口里写注解(Insert/Update/Select(查询)/Delete)；
//注解里面的{}写sql语句，它会自动拼成sql语句；
