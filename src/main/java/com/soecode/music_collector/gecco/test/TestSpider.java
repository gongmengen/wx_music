package com.soecode.music_collector.gecco.test;

import com.geccocrawler.gecco.spider.SpiderBean;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>
 * TestSpider
 * </p>
 *
 * @author msi-
 * @package: com.soecode.music_collector.gecco.test
 * @description: 数据接收类
 * @date: Created in 2020-05-28 17:34
 * @copyright: Copyright (c) 2020
 * @version: V1.0
 * @modified: msi-
 */
@Getter
@Setter
public class TestSpider implements SpiderBean{
    List<TestList> lists;

}