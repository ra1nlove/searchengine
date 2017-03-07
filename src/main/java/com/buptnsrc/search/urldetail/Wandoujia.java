package com.buptnsrc.search.urldetail;

import com.buptnsrc.search.parse.UrlDetail;

public class Wandoujia implements UrlDetail{

	public String getUrl(String url) {
		String regex = "http://www.wandoujia.com/";
		if(url.startsWith(regex) && !url.startsWith("http://www.wandoujia.com/award/")&&!url.contains("download")
				&& !url.contains("binding")&& !url.contains("?")){
			return url;
		}
		return null;
	}

}
