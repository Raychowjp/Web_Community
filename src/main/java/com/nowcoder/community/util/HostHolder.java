package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;
//用于持有用户信息，相当于session对象功能
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();
    public void setUsers(User user){
        users.set(user);
    }
    public User getUser() {
        return users.get();
    }
    public void clear(){
        users.remove();
    }

}
