package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");

    }
    //md5加密
    public static String md5(String key){
        if (StringUtils.isNullOrEmpty(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }

    //封装成json对象，得到json格式的字符串
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg",msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }
    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }




}
