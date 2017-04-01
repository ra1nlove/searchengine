package com.buptnsrc.search.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by rain on 17-2-24.
 */
public class UrlUtils {

    public static Set<String> getAllUrls(String baseurl, String content) throws Exception{
        Set<String> urls = new HashSet<String>();
            URL url = new URL(baseurl);
            Document doc = Jsoup.parse(content);
            Elements links = doc.select("a");
            for (Element link : links) {
                String newlink = link.attr("href");
                try {
                    URL newurl = new URL(url, newlink);
                    if (urlFilter(newurl.toString())) {
                        urls.add(newurl.toString().replaceAll("\\s",""));
                    }
                }catch (Exception e ){
                }
            }
        return urls;
    }

    public static boolean urlFilter(String url){
        if(url.endsWith(".js")||url.endsWith(".apk")||!url.startsWith("http")||url.endsWith("zip")||url ==null ||url.contains("comment")
                ||url.contains("javascrip")||url.contains("jpg")||url.endsWith("exe") || url.endsWith("jpg") || url.contains("#")){
            return false;
        }
        return true;
    }
}
