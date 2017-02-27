package com.buptnsrc.search.Parse;

import com.buptnsrc.search.resource.Apk;
import com.buptnsrc.search.resource.Sites;
import com.buptnsrc.search.resource.WebPage;
import com.buptnsrc.search.utils.SendData;
import com.buptnsrc.search.utils.UrlUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by rain on 17-2-24.
 */
public class UrlInfo {

    public static List<WebPage> getUrl(WebPage page, String content){
        List<WebPage> pages = new ArrayList<>();

        try{
            URL url = new URL(page.getUrl().toString());
            String packagename = Sites.regexmap.get(url.getHost());
            UrlDetail urlDetail = (UrlDetail)Class.forName(packagename).newInstance();
            Set<String> newurls = UrlUtils.getAllUrls(page.getUrl().toString(),content);

            for(String newurl : newurls){
                String result = urlDetail.getUrl(newurl);
                if(result != null){
                    WebPage newpage = new WebPage();
                    newpage.setUrl(result);
                    newpage.setReferrer(page.getUrl().toString());
                    pages.add(newpage);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return pages;

    }

}
