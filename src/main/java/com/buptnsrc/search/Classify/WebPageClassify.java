package com.buptnsrc.search.Classify;

import com.buptnsrc.search.utils.StringTool;

public class WebPageClassify {
	 
	static String[] words = {"科技","IT","互联网","通讯","3C","数码","手机","笔记本"};
	
	public static boolean classify(String text){
		String title = StringTool.getTitle(text);
		String keyword = StringTool.getMeta(text, "keywords");
		String description = StringTool.getMeta(text, "description");
		String content =  StringTool.getContext(text) ;
		for(String word: words){
			if(title.contains(word)||keyword.contains(word)||description.contains(word)){
				return true;
			}
		}
		boolean topic = Bayes.classify(content);
		return topic;
	}

}
