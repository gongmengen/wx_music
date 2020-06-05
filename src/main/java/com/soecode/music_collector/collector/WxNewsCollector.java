package com.soecode.music_collector.collector;

import com.soecode.music_collector.config.Config;
import com.soecode.music_collector.constants.HttpConst;
import com.soecode.music_collector.pojo.SougouNews;
import com.soecode.music_collector.util.HttpClientUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WxNewsCollector {
    private static final String SOUGOU_SEARCH_URL = "http://weixin.sogou.com/weixin?query=KEY_WORD&type=2";
    private static final String SOUGOU_URL = "http://weixin.sogou.com/";

    public List<SougouNews> collect(String openid, String keyword, int pageNum) throws IOException {
        String searchUrl = SOUGOU_SEARCH_URL.replace(HttpConst.SOUGOU_SEARCH_KEY_WORD, keyword);
        String body = HttpClientUtil.get(searchUrl);

        Document doc = Jsoup.parse(body);
        List<SougouNews> news = new ArrayList<>();
        SougouNews sougouNew;
        List<Element> liElements = doc.select("li[id^=sogou_vr_11002601_box_]");

        //图片以及新闻连接的获取交由gecco

        for (Element element : liElements) {
            String newsUrl = SOUGOU_URL+element.select("div[class=txt-box]").select("h3>a").attr("href");
            if (Config.get(openid).isOriginal() == isOriginal(newsUrl)) {
                sougouNew = new SougouNews();
                sougouNew.setKeyword(keyword);
                sougouNew.setOriginal(Config.get(openid).isOriginal());
                sougouNew.setName(element.select("div[class=txt-box]").select("h3 > a").text());
                sougouNew.setDescription(element.text());


                System.out.println("element.text ="+element.text());
                sougouNew.setImgUrl("https:"+element.select("div[class=img-box]").select("a>img").attr("src"));
                sougouNew.setUrl(newsUrl);
                news.add(sougouNew);
            }
        }
        return news;
    }

    private boolean isOriginal(String newsUrl) throws IOException {

        Document doc = Jsoup.parse(HttpClientUtil.get(newsUrl));
        if (doc.select("span[id=copyright_logo]").size() > 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        WxNewsCollector newsCollector = new WxNewsCollector();
        try {
            for (SougouNews news : newsCollector.collect("","周杰伦", 1)) {
                System.out.println(news);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
