package com.soecode.music_collector.handler;

import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxConsts;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxUserList;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.Map;

public class WhoAmIHandler implements WxMessageHandler {
    @Override
    public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, IService iService) throws WxErrorException {
        if (wxMessage.getContent().equals("彩蛋")){
            return WxXmlOutMessage.TEXT().content("哇塞！这都被你发现了！可恶。").toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();

        }else if (wxMessage.getContent().equals("我是谁")){
            String openid = wxMessage.getFromUserName();
            WxUserList.WxUser userInfo = iService.getUserInfoByOpenId(new WxUserList.WxUser.WxUserGet(openid, WxConsts.LANG_CHINA));
            return WxXmlOutMessage.TEXT().content(userInfo.getNickname()).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();

        }else if (wxMessage.getContent().equals("窦豆是谁")){
            return WxXmlOutMessage.TEXT().content("窦豆是我的老大小蒙恩的老婆！").toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();

        }else if (wxMessage.getContent().equals("你是谁")){
            return WxXmlOutMessage.TEXT().content("我是蒙恩创造的用来哄坏姐姐窦豆的人工智能音乐推送助手“小蒙恩”").toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();

        }else {
            return WxXmlOutMessage.TEXT().content("小蒙恩听不懂坏姐姐在说什么！").toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();

        }
    }
}
