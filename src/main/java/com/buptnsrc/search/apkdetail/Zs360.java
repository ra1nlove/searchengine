package com.buptnsrc.search.apkdetail;

import com.buptnsrc.search.parse.ApkDetail;
import com.buptnsrc.search.parse.html.Html;
import com.buptnsrc.search.resource.Apk;
import com.buptnsrc.search.utils.StringTool;
import org.apache.commons.lang.StringUtils;

import java.util.List;

public class Zs360 implements ApkDetail{

    public Apk getApkInfo(String url, String content) throws Exception{
		if(url.matches("http://zhushou.360.cn/detail/index/soft_id/[0-9]+")) {
			Html html = new Html(content);
			String appName = html.xpath("//dl[@class='clearfix']/dd/h2/span/text()").toString();
			String appVersion = html.xpath("//div[@class='base-info']/table/tbody/tr[2]/td[1]/text()").get();
			String downloadUrl = StringUtils.substringBetween(html.get(), "'downloadUrl': '", "'");
			String osPlatform = html.xpath("//div[@class='base-info']/table/tbody/tr[2]/td[2]/text()").get();
			String appSize = html.xpath("//div[@class='pf']/span[4]/text()").get();
			String appUpdateDate = html.xpath("//div[@class='base-info']/table/tbody/tr[1]/td[2]/text()").get();
			String appDownloadedTime = StringUtils.substringAfter(html.xpath("//div[@class='pf']/span[3]/text()").get(), "：");
			appDownloadedTime = appDownloadedTime.replace("次", "").replace("万", "0000");
			String appDescription = StringTool.getContext(html.xpath("//div[@id='html-brief']/").toString());
			List<String> appScreenshot = html.xpath("//div[@id='html-brief']//img/@src").all();
			String appTag = html.xpath("//div[@class='app-tags']//a/text()").all().toString();

			if(downloadUrl != null) {
				Apk apk = new Apk(appName, url, downloadUrl, osPlatform, appVersion, appSize, appUpdateDate, "Apk", null);
				apk.setAppDescription(appDescription);
				apk.setAppDownloadTimes(appDownloadedTime);
				apk.setAppScreenshot(appScreenshot);
				apk.setAppTag(appTag);
				return apk;
			}
		}
		return null;
    }
    
}
