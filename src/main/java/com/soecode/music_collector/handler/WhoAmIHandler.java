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
        String openid = wxMessage.getFromUserName();
        WxUserList.WxUser userInfo = iService.getUserInfoByOpenId(new WxUserList.WxUser.WxUserGet(openid, WxConsts.LANG_CHINA));
        return WxXmlOutMessage.TEXT().content(userInfo.getNickname()).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }
}
