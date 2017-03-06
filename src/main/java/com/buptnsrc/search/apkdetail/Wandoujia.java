package com.buptnsrc.search.apkdetail;

import com.buptnsrc.search.parse.ApkDetail;
import com.buptnsrc.search.parse.html.Html;
import com.buptnsrc.search.resource.Apk;
import com.buptnsrc.search.utils.StringTool;
import org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;


public class Wandoujia implements ApkDetail{
	
    public Apk getApkInfo(String url, String content) throws Exception{

    	if(url.startsWith("http://www.wandoujia.com/apps/")){
			Html html = new Html(content);
			String appName = html.xpath("//div[@class='app-info']/p[@class='app-name']/span/text()").toString();
			if(appName == null) return null;

			int index =1;
			for(index=1;index<5;index++){
				String info = html.xpath("dl[@class='infos-list']/dt["+index+"]/text()").toString();
				if(info.contains("版本"))
					break;
			}
			String appVersion = html.xpath("//dl[@class='infos-list']/dd["+index+"]/text()").get();
			String appDownloadUrl = html.xpath("//div[@class='qr-info']/a/@href").toString();
			String osPlatform = html.xpath("//dd[@itemprop='operatingSystems']/text()").get();
			String appSize = html.xpath("//dl[@class='infos-list']/dd[1]/text()").get();
			appSize = appSize!=null?appSize.replace(" ", ""):null;
			String appUpdateDate = html.xpath("time[@id='baidu_time']/text()").toString();
			String appDescription = StringTool.getContext(html.xpath("//div[@itemprop='description']").get());
			List<String> appScreenshot = html.xpath("//div[@class='overview']/img/@src").all();
			String appCategory = html.xpath("//div[@class='crumb']/div[@class='second']/a/span/text()").get();
			String appComment = "";
			String dowloadNum = html.xpath("//i[@itemprop='interactionCount']/text()").toString();
			List<String> appComments =html.xpath("p[@class='cmt-content']/span/text()").all();
			for(String str : appComments){
				appComment+=str+"#";
			}
			if(appDownloadUrl != null) {
				Apk apk = new Apk(appName, url, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, "Apk", null);
				apk.setAppDescription(appDescription);
				apk.setAppScreenshot(appScreenshot);
				apk.setAppCategory(appCategory);
				apk.setAppComment(appComment);
				apk.setAppDownloadTimes(dowloadNum);
				return apk;
			}
		}
    	return null;
    }
    
}
