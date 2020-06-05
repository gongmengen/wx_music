package com.soecode.music_collector.handler;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.geccocrawler.gecco.GeccoEngine;
import com.geccocrawler.gecco.pipeline.PipelineFactory;
import com.soecode.music_collector.collector.WxNewsCollector;
import com.soecode.music_collector.config.Config;
import com.soecode.music_collector.constants.ResponseConst;

import com.soecode.music_collector.pojo.SougouNews;
import com.soecode.music_collector.util.SpringContextUtil;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.bean.WxXmlOutNewsMessage;
import com.soecode.wxtools.bean.outxmlbuilder.NewsBuilder;
import com.soecode.wxtools.exception.WxErrorException;
import com.soecode.wxtools.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ChangeNewsHandler implements WxMessageHandler {


    //加入gecco
    @Resource
    protected PipelineFactory springPipelineFactory;


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
                        if(!refreshNewsCache(openid, keyword, page==0?1:page)){
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
                        System.out.println(JSON.toJSONString(item));
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

    private boolean refreshNewsCache(String openid, String keyword, int page) throws IOException, InterruptedException {
        //改用gecco 获取搜狗列表
        this.initGecco().init(keyword,page);





        Config.get(openid).setPage(page + 1);//下一页
//        WxNewsCollector collector = new WxNewsCollector();
/*
        //jsoup 抓取
        List<SougouNews> sougouNewsList = collector.collect(openid, keyword, page + 1);
*/

       //gecco 抓取
        List<SougouNews> sougouNewsList = null;
        while (MyPipeline.sougouNewsList==null){

            Thread.sleep(5000);//休眠等待gecco抓取数据
        }

        //从管道中获取数据
        sougouNewsList  = MyPipeline.sougouNewsList;
        //利用迭代器 把不含有图片或页面url的数据 删除
        Iterator<SougouNews> iterator = sougouNewsList.iterator();
        while (iterator.hasNext()){
            SougouNews next = iterator.next();
            if (StrUtil.hasEmpty(next.getImgUrl())||StrUtil.hasEmpty(next.getUrl())){
                iterator.remove();
            }
        }

        if(sougouNewsList.size() == 0){
            return false;
        }else{
            for(SougouNews news : sougouNewsList){
                news.setKeyword(keyword);
                Config.get(openid).getSougouNews().push(news);
            }
            //由于是保存在内存中 因此获取完要清空一下
            MyPipeline.sougouNewsList = null;
            return true;
        }
    }

    @Bean
    public SpringGeccoEngine initGecco() {
        return new SpringGeccoEngine() {
            @Override
            public void init(String keyword,int page) {
                SpringPipelineFactory springPipelineFactory = SpringContextUtil.getBean("springPipelineFactory");
                GeccoEngine.create()
                        .pipelineFactory(springPipelineFactory)
                        .classpath("com.soecode.music_collector.gecco.test")
                        .start("https://weixin.sogou.com/weixin?type=2&query="+keyword+"&page="+page)
                        .interval(3000)
                        .loop(false)
                        .start();
            }
        };
    }

}