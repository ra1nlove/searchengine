package com.buptnsrc.search.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scala.collection.IndexedSeqLike;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rain on 17-2-24.
 */
public class StringTool {

    static Logger log = Logger.getLogger(StringTool.class);

    public static String getHash(String str){
        if(str != null && !"".equals(str)){
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] e = digest.digest(str.getBytes("UTF-8"));
                return Hex.encodeHexString(e);
            }catch(Exception e){
                log.error("getHash error!",e);
            }
        }
        return null;
    }

    public static String getContext(String htmlStr){
        if(htmlStr == null){
            return null;
        }
        Document doc = Jsoup.parse(htmlStr);
        htmlStr = doc.body().text();
        htmlStr = htmlStr.replaceAll("\\s*", "");
        return htmlStr.trim(); //返回文本字符串
    }

    public static String getMeta(String str,String name){
        String result = "";
        Document doc = Jsoup.parse(str);
        Elements metas =doc.head().getElementsByAttributeValue("name",name);
        for(Element e : metas){
            result = e.attr("content");
        }
        return result;
    }

    public static String getH1(String str){
        String result = "";
        Document doc = Jsoup.parse(str);
        Elements h1 = doc.select("h1");
        for(Element e : h1){
            result = e.text();
        }
        return result;
    }

}
