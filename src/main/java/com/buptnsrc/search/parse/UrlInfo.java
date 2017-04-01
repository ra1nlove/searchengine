package com.buptnsrc.search.parse;

import com.buptnsrc.search.resource.Sites;
import com.buptnsrc.search.resource.WebPage;
import com.buptnsrc.search.utils.UrlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rain on 17-2-24.
 */
public class UrlInfo {

    static Log log = LogFactory.getLog(UrlInfo.class);

    public static List<WebPage> getUrl(WebPage page, String content){
        List<WebPage> pages = new ArrayList<>();
        try{
            Set<String> newurls = UrlUtils.getAllUrls(page.getUrl().toString(),content);
            for(String newurl : newurls){
                    WebPage newpage = new WebPage();
                    newpage.setUrl(newurl);
                    pages.add(newpage);
            }
        }catch(Exception e){
            log.error("get urlInfo error ! ",e);
        }
        return pages;
    }

}
