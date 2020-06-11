package com.soecode.music_collector.handler;

import cn.hutool.core.util.ReUtil;
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
			//标题关键字部分处理
			String str = testList.getAt();
			String rule1 = "<em>.+</em>";//匹配em
			String rule2 = "[\\u4e00-\\u9fa5]";//匹配em中的中文

			String match1 = ReUtil.get(rule1,str,0);
			List<String> resultFindAll = ReUtil.findAll(rule2, match1, 0, new ArrayList<String>());

			//List<String> match2 = ReUtil.getAllGroups(Pattern.compile(rule2),match1);
			StringBuffer resu = new StringBuffer();
			resultFindAll.forEach(match ->{
				resu.append(match);
			});


			str = str.replace(match1,"$keyword$");
			str = str.replace("$keyword$",resu.toString());




			sougouNews.setName(str);
			sougouNews.setKeyword("");
			sougouNews.setDescription(testList.getP());
			if (StringUtils.isNotEmpty(sougouNews.getName())&&StringUtils.isNotEmpty(sougouNews.getUrl())&&StringUtils.isNotEmpty(sougouNews.getImgUrl())) {
				sougouNewsList.add(sougouNews);
			}
		});
		//System.out.println(JSON.toJSONString(sougouNewsList));

	}

}
