package com.soecode.music_collector.config;

import com.soecode.music_collector.pojo.SougouNews;

import java.util.LinkedList;

public class UserConfig {
    //open id
    private String openid;
    //是否是原创
    private boolean isOriginal = false;
    //当前歌手/关键词
    private String keyword;
    //UserConfig 图文
    private LinkedList<SougouNews> sougouNews = new LinkedList<>();
    //页数
    private int page;

    public UserConfig(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public LinkedList<SougouNews> getSougouNews() {
        return sougouNews;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
