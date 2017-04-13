package com.buptnsrc.search.demo;

import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextExtract {
	
	private static List<String> lines;
	private final static int blocksWidth;
	private static int threshold;
	private static String html;
	private static boolean flag;
	private static int start;
	private static int end;
	private static StringBuilder text;
	private static ArrayList<Integer> indexDistribution;
	
	static {
		lines = new ArrayList<String>();
		indexDistribution = new ArrayList<Integer>();
		text = new StringBuilder();
		blocksWidth = 3;
		flag = false;
		threshold	= 86;   
	}
	
	public static void setthreshold(int value) {
		threshold = value;
	}


	public static String parse(String _html) {
		return parse(_html, false);
	}
	

	public static String parse(String _html, boolean _flag) {
		flag = _flag;
		html = _html;
		preProcess();
		return getText();
	}
	
	private static void preProcess() {
		html = html.replaceAll("(?is)<!DOCTYPE.*?>", "");
		html = html.replaceAll("(?is)<!--.*?-->", "");				// remove html comment
		html = html.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
		html = html.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
		html = html.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
		html = html.replaceAll("(?is)<.*?>", "");
	}
	
	private static String getText() {
		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();
		
		for (int i = 0; i < lines.size() - blocksWidth; i++) {
			int wordsNum = 0;
			for (int j = i; j < i + blocksWidth; j++) { 
				lines.set(j, lines.get(j).replaceAll("\\s+", ""));
				wordsNum += lines.get(j).length();
			}
			indexDistribution.add(wordsNum);
			//System.out.println(wordsNum);
		}
		
		start = -1; end = -1;
		boolean boolstart = false, boolend = false;
		text.setLength(0);
		
		for (int i = 0; i < indexDistribution.size() - 1; i++) {
			if (indexDistribution.get(i) > threshold && ! boolstart) {
				if (indexDistribution.get(i+1).intValue() != 0 
					|| indexDistribution.get(i+2).intValue() != 0
					|| indexDistribution.get(i+3).intValue() != 0) {
					boolstart = true;
					start = i;
					continue;
				}
			}
			if (boolstart) {
				if (indexDistribution.get(i).intValue() == 0 
					|| indexDistribution.get(i+1).intValue() == 0) {
					end = i;
					boolend = true;
				}
			}
			StringBuilder tmp = new StringBuilder();
			if (boolend) {
				//System.out.println(start+1 + "\t\t" + end+1);
				for (int ii = start; ii <= end; ii++) {
					if (lines.get(ii).length() < 5) continue;
					tmp.append(lines.get(ii) + "\n");
				}
				String str = tmp.toString();
				//System.out.println(str);
				if (str.contains("Copyright") ) continue;
				text.append(str);
				boolstart = boolend = false;
			}
		}
		return text.toString();
	}

	public static void main(String[] args){
        WebPage page = new WebPage();
        page.setUrl("http://shouji.baidu.com/software/11267368.html");
        String result = PageDownload.download(page);

        System.out.println(TextExtract.parse(result));
    }
}
