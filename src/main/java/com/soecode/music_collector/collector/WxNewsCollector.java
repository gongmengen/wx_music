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
    private static final String SOUGOU_SEARCH_URL = "http://weixin.sogou.com/weixin?query=KEY_WORD&_sug_type_=&sut=28577&lkt=1%2C1524741445786%2C1524741445786&s_from=input&_sug_=y&type=2&sst0=1524741445888&page=PAGE_NUM&ie=utf8&w=01019900&dr=1";
    private static final String SOUGOU_URL = "https://weixin.sogou.com/";

    public List<SougouNews> collect(String openid, String keyword, int pageNum) throws IOException {
        String searchUrl = SOUGOU_SEARCH_URL.replace(HttpConst.SOUGOU_SEARCH_KEY_WORD, keyword).replace(HttpConst.SOUGOU_SEARCH_PAGE_NUM, pageNum + "");
        String body = HttpClientUtil.get(searchUrl);

        Document doc = Jsoup.parse(body);
        List<SougouNews> news = new ArrayList<>();
        SougouNews sougouNew;
        List<Element> liElements = doc.select("li[id^=sogou_vr_11002601_box_]");

        for (Element element : liElements) {
            String newsUrl = element.select("div[class=txt-box]").select("h3>a").attr("href");
            if (Config.get(openid).isOriginal() == isOriginal(newsUrl)) {
                sougouNew = new SougouNews();
                sougouNew.setKeyword(keyword);
                sougouNew.setOriginal(Config.get(openid).isOriginal());
                sougouNew.setName(element.select("div[class=txt-box]").select("h3 > a").text());
                sougouNew.setDescription(element.text());
                sougouNew.setImgUrl(element.select("div[class=img-box]").select("a>img").attr("src"));
                System.out.println(sougouNew.getImgUrl());
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
