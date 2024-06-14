package com.nowcoder.community.util;

public interface CommunityConstant {
    //success activation
    int Activation_Success = 0;
    //repeated activation
    int Activation_Repeated = 1;
    //failure
    int Activation_Failure = 2;

    //默认登录状态的超时时间,不勾选“记住我”
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12; //12h

    //勾选“记住我”
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;




}
