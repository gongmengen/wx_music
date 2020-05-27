package com.soecode.music_collector.handler;

import com.soecode.music_collector.collector.RankCollector;
import com.soecode.music_collector.constants.MenuKey;
import com.soecode.music_collector.constants.ResponseConst;
import com.soecode.music_collector.constants.UrlConst;
import com.soecode.music_collector.pojo.HotSinger;
import com.soecode.music_collector.pojo.Rank;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.exception.WxErrorException;

import java.io.IOException;
import java.util.Map;

public class RankHandler implements WxMessageHandler {
    private static RankHandler instance = null;

    private boolean isRun = false;

    private RankHandler() {
    }

    public static synchronized RankHandler getInstance() {
        if (instance == null) {
            instance = new RankHandler();
        }
        return instance;
    }

    @Override
    public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, IService iService)
            throws WxErrorException {
        StringBuilder result = new StringBuilder();
        if (!getIsRun()) {
            setRun(true);
            try {
                result = execute(wxMessage);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                setRun(false);
            }
        } else {
            result.append(ResponseConst.DUPLICATE_REQUEST);
        }
        return WxXmlOutMessage.TEXT().content(result.toString()).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }

    private StringBuilder execute(WxXmlMessage wxMessage) throws Exception{
        StringBuilder stringBuilder = new StringBuilder();
        try {
            switch (wxMessage.getEventKey()) {
                case MenuKey.HOT_SONG:
                    collectSongRank(stringBuilder, UrlConst.HOT_RANK_URL);
                    break;
                case MenuKey.TOP_500:
                    collectSongRank(stringBuilder, UrlConst.TOP_500_RANK_URL);
                    break;
                case MenuKey.NET_HOT_SONG:
                    collectSongRank(stringBuilder, UrlConst.NETWORK_HOT_RANK_URL);
                    break;
                case MenuKey.HUAYU_SONG:
                    collectSongRank(stringBuilder, UrlConst.HUAYU_NEW_SONG_RANK_URL);
                    break;
                case MenuKey.XINAO_SONG:
                    collectSongRank(stringBuilder, UrlConst.XINAO_SONG_RANK_URL);
                    break;
                default:
                    stringBuilder.append("暂时无此分类噢！");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder;
    }

    private void collectSongRank(StringBuilder stringBuilder, String url) throws IOException {
        RankCollector collector = new RankCollector();
        Rank rank = collector.collect(url);
        stringBuilder.append("\uD83D\uDD25" + rank.getScope() + "[" + rank.getUpdateTime() + "]\n\n");
        for (HotSinger hotSinger : rank.getHotSingerList()) {
            stringBuilder.append(hotSinger.getName() + "-" + hotSinger.getHotSong() + "\n");
        }
    }


    private synchronized boolean getIsRun() {
        return isRun;
    }

    private synchronized void setRun(boolean run) {
        isRun = run;
    }

}