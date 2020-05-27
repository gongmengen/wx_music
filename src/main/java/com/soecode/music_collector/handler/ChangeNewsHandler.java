package com.soecode.music_collector.handler;

import com.soecode.music_collector.collector.WxNewsCollector;
import com.soecode.music_collector.config.Config;
import com.soecode.music_collector.constants.ResponseConst;
import com.soecode.music_collector.pojo.SougouNews;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.bean.WxXmlOutNewsMessage;
import com.soecode.wxtools.bean.outxmlbuilder.NewsBuilder;
import com.soecode.wxtools.exception.WxErrorException;
import com.soecode.wxtools.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ChangeNewsHandler implements WxMessageHandler {

    private static ChangeNewsHandler instance = null;

    private boolean isRun = false;

    private ChangeNewsHandler(){}

    public static synchronized ChangeNewsHandler getInstance(){
        if (instance == null) {
            instance = new ChangeNewsHandler();
        }
        return instance;
    }

    private synchronized  boolean getIsRun() {
        return isRun;
    }

    private synchronized void setRun(boolean run) {
        isRun = run;
    }

    private static final int LIMIT = 8;

    @Override
    public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, IService iService)
            throws WxErrorException {
        if(!getIsRun()){
            setRun(true);
            String openid = wxMessage.getFromUserName();
            try {
                String keyword = Config.get(openid).getKeyword();
                int page = Config.get(openid).getPage();
                if(StringUtils.isEmpty(keyword)){
                    return WxXmlOutMessage.TEXT().content("关键词尚未设置。\n" + ResponseConst.SET_KEYWORD_CONFIG_FAILURE).toUser(openid).fromUser(wxMessage.getToUserName()).build();
                }else{
                    NewsBuilder newsBuilder = WxXmlOutMessage.NEWS();
                    if(Config.get(openid).getSougouNews().size() == 0){
                        //如果找不到文章，建议下一页
                        if(!refreshNewsCache(openid, keyword, page)){
                            return WxXmlOutMessage.TEXT().content(ResponseConst.NOT_FOUND_ANY).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
                        }
                    }
                    for(int i = 0; i < LIMIT; i++){
                        if(Config.get(openid).getSougouNews().size() == 0){
                            break;
                        }
                        SougouNews news = Config.get(openid).getSougouNews().pop();
                        WxXmlOutNewsMessage.Item item = new WxXmlOutNewsMessage.Item();
                        item.setTitle(news.getName());
                        item.setDescription(news.getDescription());
                        item.setUrl(news.getUrl());
                        item.setPicUrl(news.getImgUrl());
                        newsBuilder.addArticle(item);
                    }
                    return newsBuilder.toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                setRun(false);
            }
        }else{
            return WxXmlOutMessage.TEXT().content(ResponseConst.DUPLICATE_REQUEST).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
        }
        return WxXmlOutMessage.TEXT().content(ResponseConst.DEFAULE_TEXT).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }

    private boolean refreshNewsCache(String openid, String keyword, int page) throws IOException {
        Config.get(openid).setPage(page + 1);
        WxNewsCollector collector = new WxNewsCollector();
        List<SougouNews> sougouNewsList = collector.collect(openid, keyword, page + 1);
        if(sougouNewsList.size() == 0){
            return false;
        }else{
            for(SougouNews news : sougouNewsList){
                Config.get(openid).getSougouNews().push(news);
            }
            return true;
        }
    }
}