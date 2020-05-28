package com.soecode.music_collector.gecco.spring;

import com.alibaba.fastjson.JSON;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.soecode.music_collector.gecco.test.TestList;
import com.soecode.music_collector.gecco.test.TestSpider;
import com.soecode.music_collector.pojo.SougouNews;

import java.util.ArrayList;
import java.util.List;

public class ConsolePipeline implements Pipeline<TestSpider> {

	public static List<SougouNews> sougouNewsList = null;

	@Override
	public void process(TestSpider bean) {

		List<TestList> lists = bean.getLists();
		for (TestList test:lists
			 ) {
			System.out.println(test.getUrl());
		}

/*		sougouNewsList = new ArrayList<SougouNews>();
		sougouNewsList.add(new SougouNews();*/
	}

}
