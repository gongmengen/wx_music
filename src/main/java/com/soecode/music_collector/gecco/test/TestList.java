package com.soecode.music_collector.gecco.test;

import com.geccocrawler.gecco.annotation.Href;
import com.geccocrawler.gecco.annotation.HtmlField;
import com.geccocrawler.gecco.annotation.Image;
import com.geccocrawler.gecco.annotation.Text;
import com.geccocrawler.gecco.spider.HtmlBean;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TestList
 * </p>
 *
 * @author msi-
 * @package: com.geccocrawler.gecco.test
 * @description: 通用列表获取demo
 * @date: Created in 2020-05-28 16:29
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: msi-
 */
@Getter
@Setter
public class TestList implements HtmlBean {

    @Text
    @HtmlField(cssPath = "div[class=txt-box]>h3>a")
    String at;

    @Href
    @HtmlField(cssPath = "div[class=txt-box]>h3>a")
    String url;

    @Image({"div[class=img-box]>a>img", "src"})
    @HtmlField(cssPath="div[class=img-box]>a>img")
    String imgurl;

    @Text
    @HtmlField(cssPath = "div[class=txt-box]>p")
    String p;


}