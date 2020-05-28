package com.soecode.music_collector.gecco.test;

import com.geccocrawler.gecco.annotation.Gecco;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Request;
import com.geccocrawler.gecco.annotation.RequestParameter;
import com.geccocrawler.gecco.request.HttpRequest;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Gecco(matchUrl="https://weixin.sogou.com/weixin?type={}&query={}", pipelines="consolePipeline")
public class Test implements HtmlBean {


    private static final long serialVersionUID = 3671747227683281756L;

    @Request
    private HttpRequest request;

    @RequestParameter("type")
    private String type;


    @RequestParameter("query")
    private String query;


    @HtmlField(cssPath=".news-list >li>div>h3>a")
    private List<TestList> lists;





}
