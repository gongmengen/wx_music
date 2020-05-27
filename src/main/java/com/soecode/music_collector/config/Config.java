package com.soecode.music_collector.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Config {

    public static ConcurrentMap<String, UserConfig> userConfigs = new ConcurrentHashMap();

    public static UserConfig get(String openid){
        if(userConfigs.get(openid) == null){
            UserConfig config = new UserConfig(openid);
            userConfigs.put(openid, config);
            return config;
        }else{
            return userConfigs.get(openid);
        }
    }

    public static void set(UserConfig config){
        userConfigs.putIfAbsent(config.getOpenid(), config);
    }
}
