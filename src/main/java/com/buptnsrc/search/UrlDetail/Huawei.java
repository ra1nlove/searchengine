package com.buptnsrc.search.UrlDetail;

import com.buptnsrc.search.Parse.UrlDetail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Huawei implements UrlDetail{


	public String getUrl(String url) {
		String regex = "http://appstore.huawei.com";
		if(url.startsWith(regex)){
			return url;
		}
		return null;
	}

}
