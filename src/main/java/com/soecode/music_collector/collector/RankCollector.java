package com.soecode.music_collector.collector;

import com.soecode.music_collector.constants.UrlConst;
import com.soecode.music_collector.pojo.HotSinger;
import com.soecode.music_collector.pojo.Rank;
import com.soecode.music_collector.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.*;

public class RankCollector {

    public Rank collect(String url) throws IOException {
        return getRank(url);
    }

    private Rank getRank(String rankUrl) throws IOException {
        Rank rank = new Rank();
        String body = HttpClientUtil.get(rankUrl);
        Document doc = Jsoup.parse(body);
        rank.setScope(doc.select("a[class=current]").attr("title"));
        rank.setUpdateTime(doc.select("span[class=rank_update]").text());
        List<Element> aElements = doc.select("a[data-active=playDwn]");
        for(int i = 0; i < aElements.size(); i++){
            String[] splitArray = aElements.get(i).text().split("-");
            String name = splitArray[0].toString().trim();
            String song = splitArray[1].toString().trim();
            rank.getHotSingerList().add(new HotSinger(doc.select("a[class=current]").attr("title"), name, song));
        }
        return rank;
    }

    public static void main(String[] args) {
        RankCollector collector = new RankCollector();
        try {
            for(HotSinger s : collector.collect(UrlConst.HUAYU_NEW_SONG_RANK_URL).getHotSingerList()){
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
