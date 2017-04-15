package com.buptnsrc.search.classify;

import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebPageClassify {

	public static boolean DetailPageClassify(String text){

	    try {

            Document doc = Jsoup.parse(text);

            float total = 0;
            float linknum = 0;

            Elements links = doc.body().select("a");
            for (Element link : links) {
                if (link.text().length() > 0) linknum++;
            }

            Elements elements = doc.body().getAllElements();
            for (Element e : elements) {
                int a = e.children().size();
                if (a == 0 && e.text().length() > 1 && !e.tagName().equals("option") && !e.tagName().equals("span") && !e.tagName().equals("div")) {
                    if (e.tagName().equals("p") && e.text().length() > 5) {
                        total += e.text().length() / 5;
                    } else total++;
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

    public static void main(String[] args){
        WebPage page = new WebPage();
        page.setUrl("http://zj.sina.com.cn/");
        String text = PageDownload.download(page);
        System.out.print(WebPageClassify.DetailPageClassify(text));
    }


}
