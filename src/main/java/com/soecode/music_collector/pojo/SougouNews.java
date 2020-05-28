package com.soecode.music_collector.pojo;

public class SougouNews {

    private String name;
    private String keyword;
    private String description;
    private String url;
    private boolean isOriginal;
    private String imgUrl;

    public SougouNews() {
    }

    public SougouNews(String url, String imgUrl) {
        this.url = url;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "SougouNews{" +
                "name='" + name + '\'' +
                ", keyword='" + keyword + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", isOriginal=" + isOriginal +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
