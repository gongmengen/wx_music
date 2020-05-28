package com.soecode.music_collector.gecco.test;

import com.geccocrawler.gecco.annotation.Href;
import com.geccocrawler.gecco.annotation.HtmlField;
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
    @HtmlField(cssPath = "a")
    String at;

    @Href(click = true)
    @HtmlField(cssPath = "a")
    String url;


}