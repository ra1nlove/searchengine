package com.buptnsrc.search.urldetail;

import com.buptnsrc.search.parse.UrlDetail;

public class Zs360 implements UrlDetail{

	public String getUrl(String url) {
		String regex = "http://zhushou.360.cn/";
		if(url.startsWith(regex) && !url.startsWith("http://zhushou.360.cn/channel/")){
			return url;
		}
		return null;
	}

}
