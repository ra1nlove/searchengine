package com.buptnsrc.search.page;

import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageClassify {

	public static boolean DetailPageClassify(String text){
	    try {
            Document doc = Jsoup.parse(text);
            float total = 0;
            float linknum = 0;
            Elements links = doc.body().select("a");
            for (Element link : links) {
                if (link.text().length() > 0) linknum++;
            }
            total = linknum;
            Elements txt = doc.body().select("p");
            for(Element p : txt){
                if(p.text().length()>20 &&p.children().size()==0){
                    total += p.text().length()/5;
                }
            }

            float result = linknum / total;
            if (result < 0.9) {
                return true;
            }

        }catch (Exception e){
	        e.printStackTrace();
        }
        return false;
    }

}
