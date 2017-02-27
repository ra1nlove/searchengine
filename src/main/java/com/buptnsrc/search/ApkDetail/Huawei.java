package com.buptnsrc.search.ApkDetail;

import java.util.List;
import com.buptnsrc.search.Parse.ApkDetail;
import com.buptnsrc.search.Parse.html.Html;
import com.buptnsrc.search.resource.Apk;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class Huawei implements ApkDetail{

	public static Logger log = Logger.getLogger(Huawei.class);
	
    public Apk getApkInfo(String url, String content) {
    	
    	if(url.startsWith("http://appstore.huawei.com:80/app")){    	
    		
    		try{			
    			Html html = new Html(content);
    			String name =html.xpath("//div[@class='app-info flt']/ul[@class='app-info-ul nofloat'][1]/li[2]/p/span[@class='title']/text()").toString();
    			String downloadurl = html.xpath("//div[@class='app-function nofloat']/a/@dlurl").toString();
    			if(name == null ||downloadurl == null){
    				return null;
    			}
    			name = name.trim();
    			String downloadtime =StringUtils.substringAfter(html.xpath("//div[@class='app-info flt']/ul[@class='app-info-ul nofloat']/li[2]/p/span[@class='grey sub']/text()").toString(), "：").replace("次", "");
    			String updatetime = html.xpath("//ul[@class='app-info-ul nofloat'][2]/li[2]/span/text()").get();
    			String size = html.xpath("//ul[@class='app-info-ul nofloat'][2]/li[1]/span/text()").get();
    			String version = html.xpath("//ul[@class='app-info-ul nofloat'][2]/li[4]/span/text()").get();
    			String desc = html.xpath("//div[@id='app_strdesc']/text()").toString().replaceAll("\\s", "");
    			List<String> images = html.xpath("//ul[@class='imgul']//img/@src").all();

//    			System.out.println(name);
//    			System.out.println(downloadurl);
//    			System.out.println(downloadtime);
//    			System.out.println(updatetime);
//    			System.out.println(size);
//    			System.out.println(version);
//    			System.out.println(os);
//    			System.out.println(desc);
//    			System.out.println(category);
//    			System.out.println(images);
    
        		Apk apk = new Apk(name,url,downloadurl,null,version,size,updatetime,"Apk",null);
        		apk.setAppDescription(desc);
        		apk.setAppDownloadTimes(downloadtime);
        		apk.setAppScreenshot(images);
        		return apk;
    		}catch(Exception e){
				log.error("getAppinfo error!",e);
    		}
    	}
    	return null;
    }
    
}
