package com.buptnsrc.search.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

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

        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        htmlStr = htmlStr.replaceAll("\\s*", "");

        return htmlStr.trim(); //返回文本字符串

    }

}
