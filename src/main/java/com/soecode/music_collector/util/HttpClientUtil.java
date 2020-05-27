package com.soecode.music_collector.util;

import com.soecode.music_collector.constants.HttpConst;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
// httpclient + j
public class HttpClientUtil {

    public static String get(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(HttpConst.REQUEST_HEADER);
        HttpResponse rs = HttpClients.createDefault().execute(httpGet);
        return EntityUtils.toString(rs.getEntity());
    }

    private static String generateCookie(Header[] cookieHeaders) {
        if (cookieHeaders == null || cookieHeaders.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Header header : cookieHeaders) {
            sb.append(header.getValue().split(";")[0]).append("; ");
        }
        return sb.toString();
    }
}
