package com.buptnsrc.search.apkdetail;

import com.buptnsrc.search.parse.ApkDetail;
import com.buptnsrc.search.parse.html.Html;
import com.buptnsrc.search.resource.Apk;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class Mumayi implements ApkDetail{


	
    public Apk getApkInfo(String url, String content) throws Exception {

		if (url.matches("http://www.mumayi.com/android-[0-9]*.html")) {
			Html html = new Html(content);
			String appName = html.xpath("//div[@class='iapp_hd mart10']/h1/text()").toString();
			String appVersion = StringUtils.substringAfterLast(appName, "V");
			String appDownloadUrl = html.xpath("//a[@class='download fl']/@href").toString();
			appDownloadUrl = appDownloadUrl == null ? html.xpath("//div[@class='ibtn fl']/a[2]/@href").toString() : appDownloadUrl;
			String osPlatform = html.xpath("//ul[@class='istyle fl']/li[5]/ul/li/div[1]/text()").get();
			String appSize = html.xpath("//ul[@class='istyle fl']/li[4]/text()").get();
			String appUpdateDate = html.xpath("//ul[@class='istyle fl']/li[3]/text()").get();
			String appDescription = html.xpath("//div[@class='ibox']/p[2]/text()").get();
			List<String> appScreenshot = html.xpath("//div[@class='ibox']//img/@src2").all();
			String appCategory = html.xpath("//ul[@class='istyle fl']/li[2]/text()").get();
			String appVenderName = html.xpath("//ul[@class='author']/li[1]/text()").toString();

			if(appDownloadUrl!=null) {
				Apk apk = new Apk(appName, url, appDownloadUrl, osPlatform, appVersion, appSize, appUpdateDate, "Apk", null);
				apk.setAppDescription(appDescription);
				apk.setAppVenderName(appVenderName);
				apk.setAppScreenshot(appScreenshot);
				apk.setAppCategory(appCategory);
				return apk;
			}
		}
		return null;
	}
}
