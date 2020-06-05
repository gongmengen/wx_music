package com.soecode.music_collector.handler;

import com.alibaba.fastjson.JSON;
import com.geccocrawler.gecco.annotation.PipelineName;
import com.geccocrawler.gecco.pipeline.Pipeline;
import com.soecode.music_collector.gecco.test.Test;
import com.soecode.music_collector.pojo.SougouNews;
import com.soecode.wxtools.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class MyPipeline implements Pipeline<Test> {

	public static List<SougouNews> sougouNewsList = null;

	@Override
	public void process(Test bean) {
		this.sougouNewsList = new ArrayList<SougouNews>();

		bean.getLists().forEach(testList -> {
			SougouNews sougouNews = new SougouNews();
			sougouNews.setUrl(testList.getUrl());
			sougouNews.setImgUrl(testList.getImgurl());
			sougouNews.setOriginal(false);
			sougouNews.setName(testList.getAt());
			sougouNews.setKeyword("");
			sougouNews.setDescription(testList.getP());
			if (StringUtils.isNotEmpty(sougouNews.getName())&&StringUtils.isNotEmpty(sougouNews.getUrl())&&StringUtils.isNotEmpty(sougouNews.getImgUrl())) {
				sougouNewsList.add(sougouNews);
			}
		});
		//System.out.println(JSON.toJSONString(sougouNewsList));

	}

}
