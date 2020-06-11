package com.soecode.music_collector.matcher;

import com.soecode.wxtools.api.WxMessageMatcher;
import com.soecode.wxtools.bean.WxXmlMessage;
import com.soecode.wxtools.util.StringUtils;

public class WhoAmIMatcher implements WxMessageMatcher{

    @Override
    public boolean match(WxXmlMessage message) {
        if(StringUtils.isNotEmpty(message.getContent())){
            if(message.getContent().equals("我是谁")){
                return true;
            }else if (message.getContent().equals("彩蛋")){
                return true;
            }else if (message.getContent().equals("窦豆是谁")){
                return true;
            }else if (message.getContent().equals("你是谁")){
                return true;
            }else if (message.getContent().indexOf("关键词")>-1){
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

}
