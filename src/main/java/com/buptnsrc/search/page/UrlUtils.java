package com.buptnsrc.search.page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                    urls.put(newurl.toString().replaceAll("\\s",""),achor);
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
}
