package com.buptnsrc.search.urldetail;

import com.buptnsrc.search.parse.UrlDetail;

public class Huawei implements UrlDetail{


	public String getUrl(String url) {
		String regex = "http://appstore.huawei.com";
		if(url.startsWith(regex)){
			return url;
		}
		return null;
	}

}
