package com.buptnsrc.search.page;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rain on 17-2-24.
 */
public class UrlUtils {

    public static Map<CharSequence,CharSequence> getAllUrls(String baseurl, String content) throws Exception{
        Map<CharSequence,CharSequence> urls = new HashMap<CharSequence,CharSequence>();
        URL url = new URL(baseurl);
        Document doc = Jsoup.parse(content);
        Elements links = doc.select("a");
        for (Element link : links) {
            String newlink = link.attr("href");
            String achor = link.text();
            try {
                URL newurl = new URL(url, newlink);
                if (urlFilter(newurl.toString())) {
                    urls.put(urlNormalize(newurl.toString()),achor);
                }
            }catch (Exception e ){
            }
        }
        return urls;
    }

    public static boolean urlFilter(String url){
        if(url.endsWith(".js")||url.endsWith(".apk")||!url.startsWith("http")||url.endsWith("zip")||url ==null ||url.contains("comment")
                ||url.contains("javascrip")||url.contains("jpg")||url.endsWith("exe") || url.endsWith("png") || url.contains("#")){
            return false;
        }
        return true;
    }

    public static String urlNormalize(String link) throws MalformedURLException {
        URL url = new URL(link);
        String protocol = url.getProtocol();
        String host     = url.getHost();
        String file     = url.getFile();
        if(file.length()==0) file ="/";
        return protocol+"://"+host+file.replaceAll("\\s","");
    }

    public static String reverseUrl(String str) throws MalformedURLException {

        URL url = new URL(str);
        String host = url.getHost();
        String file = url.getFile();
        String protocol = url.getProtocol();
        int port = url.getPort();

        StringBuilder buf = new StringBuilder();

        /* reverse host */
        reverseAppendSplits(host, buf);

        /* add protocol */
        buf.append(':');
        buf.append(protocol);

        /* add port if necessary */
        if (port != -1) {
            buf.append(':');
            buf.append(port);
        }

        /* add path */
        if (file.length() > 0 && '/' != file.charAt(0)) {
            buf.append('/');
        }
        buf.append(file);

        return buf.toString();

    }

    private static void reverseAppendSplits(String string, StringBuilder buf) {
        String[] splits = StringUtils.split(string, '.');
        if (splits.length > 0) {
            for (int i = splits.length - 1; i > 0; i--) {
                buf.append(splits[i]);
                buf.append('.');
            }
            buf.append(splits[0]);
        } else {
            buf.append(string);
        }
    }

    public static String unreverseUrl(String reversedUrl) {
        StringBuilder buf = new StringBuilder(reversedUrl.length() + 2);

        int pathBegin = reversedUrl.indexOf('/');
        if (pathBegin == -1)
            pathBegin = reversedUrl.length();
        String sub = reversedUrl.substring(0, pathBegin);

        String[] splits = StringUtils.splitPreserveAllTokens(sub, ':'); // {<reversed
        // host>,
        // <port>,
        // <protocol>}
        buf.append(splits[1]); // add protocol
        buf.append("://");
        reverseAppendSplits(splits[0], buf); // splits[0] is reversed
        // host
        if (splits.length == 3) { // has a port
            buf.append(':');
            buf.append(splits[2]);
        }
        buf.append(reversedUrl.substring(pathBegin));
        return buf.toString();
    }


}
