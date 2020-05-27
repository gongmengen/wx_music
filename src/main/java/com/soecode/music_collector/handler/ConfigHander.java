package com.soecode.music_collector.handler;

import com.soecode.music_collector.config.Config;
import com.soecode.music_collector.constants.ResponseConst;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.exception.WxErrorException;
import com.soecode.wxtools.util.StringUtils;

import java.util.Map;

public class ConfigHander implements WxMessageHandler{

    private static ConfigHander instance = null;

    private boolean isRun = false;

    private ConfigHander() {
    }

    public static synchronized ConfigHander getInstance() {
        if (instance == null) {
            instance = new ConfigHander();
        }
        return instance;
    }

    private synchronized boolean getIsRun() {
        return isRun;
    }

    private synchronized void setRun(boolean run) {
        isRun = run;
    }

    @Override
    public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, IService iService) throws WxErrorException {
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
        }else{
            result.append(ResponseConst.DUPLICATE_REQUEST);
        }
        return WxXmlOutMessage.TEXT().content(result.toString()).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }

    private StringBuilder execute(WxXmlMessage wxMessage) throws Exception{
        StringBuilder returnStr = new StringBuilder();
        String content = wxMessage.getContent();
        if(content.contains("原创")){
            String[] splitArray = content.split("(:|：)");
            if(splitArray.length == 2){
                if("是".equals(splitArray[1])){
                    Config.get(wxMessage.getFromUserName()).setOriginal(true);
                }else{
                    Config.get(wxMessage.getFromUserName()).setOriginal(false);
                }
                returnStr.append(ResponseConst.SET_CONFIG_SUCCESS + "当前设置为[原创："+ (Config.get(wxMessage.getFromUserName()).isOriginal() ? "是" : "否") + "]");
                clearNewsAndPage(wxMessage.getFromUserName());
            }else {
                returnStr.append(ResponseConst.SET_ORIGINAL_CONFIG_FAILURE);
            }
        }else if(content.contains("关键词")){
            String[] splitArray = content.split("(:|：)");
            if(splitArray.length == 2){
                Config.userConfigs.get(wxMessage.getFromUserName()).setKeyword(splitArray[1]);
                returnStr.append(ResponseConst.SET_CONFIG_SUCCESS + "当前设置为[关键词："+ Config.get(wxMessage.getFromUserName()).getKeyword()+"]");
                clearNewsAndPage(wxMessage.getFromUserName());
            }else{
                returnStr.append(ResponseConst.SET_KEYWORD_CONFIG_FAILURE);
            }
        }else if(content.contains("配置")){
            returnStr.append("Hi小海疼, 当前配置如下：\n");
            returnStr.append("原创："+(Config.get(wxMessage.getFromUserName()).isOriginal() ? "是" : "否")+"\n");
            if(StringUtils.isEmpty(Config.get(wxMessage.getFromUserName()).getKeyword())){
                returnStr.append("关键词：未设置\n");
            }else{
                returnStr.append("关键词：" + Config.get(wxMessage.getFromUserName()).getKeyword() + "\n");
            }
            returnStr.append("当前页数："+Config.get(wxMessage.getFromUserName()).getPage()+"\n");
            returnStr.append("用户ID："+Config.get(wxMessage.getFromUserName()).getOpenid()+"\n");
        }else if(content.contains("帮助")){
            returnStr.append(ResponseConst.HELP);
        }
        else{
            returnStr.append(ResponseConst.DEFAULE_TEXT);
        }
        return returnStr;
    }

    private void clearNewsAndPage(String openid) {
        Config.get(openid).getSougouNews().clear();
        Config.get(openid).setPage(0);
    }

}
