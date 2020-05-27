package com.soecode.music_collector.handler;

import com.soecode.music_collector.constants.ResponseConst;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxMessageHandler;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.bean.WxXmlOutMessage;
import com.soecode.wxtools.exception.WxErrorException;

import java.util.Map;

public class HelpDocHandler implements WxMessageHandler {

    private static HelpDocHandler instance = null;

    private boolean isRun = false;

    private HelpDocHandler(){}

    public static synchronized HelpDocHandler getInstance(){
        if (instance == null) {
            instance = new HelpDocHandler();
        }
        return instance;
    }

    private synchronized  boolean getIsRun() {
        return isRun;
    }

    private synchronized void setRun(boolean run) {
        isRun = run;
    }


    @Override
    public WxXmlOutMessage handle(WxXmlMessage wxMessage, Map<String, Object> context, IService iService) throws WxErrorException {
        WxXmlOutMessage response = null;
        if (!getIsRun()) {
            setRun(true);
            response = execute(wxMessage);
            setRun(false);
        }
        return response;
    }

    private WxXmlOutMessage execute(WxXmlMessage wxMessage) {
        return WxXmlOutMessage.TEXT().content(ResponseConst.HELP).toUser(wxMessage.getFromUserName()).fromUser(wxMessage.getToUserName()).build();
    }
}
