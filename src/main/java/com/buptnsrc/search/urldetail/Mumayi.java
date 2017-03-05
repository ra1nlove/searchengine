package com.buptnsrc.search.urldetail;

import com.buptnsrc.search.parse.UrlDetail;

public class Mumayi implements UrlDetail{


	public String getUrl(String url) {
		String regex = "http://www.mumayi.com/android";
		if(url.startsWith(regex)){
			return url;
		}
		return null;
	}

}
