package com.buptnsrc.search.page;

import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import org.apache.commons.codec.binary.Hex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextExtract {

	private final static int blocksWidth =3;
	private static int threshold = 86;

	
	public static String getText(String html) {

        List<String> lines ;
        int start;
        int end;
        StringBuilder text = new StringBuilder();
        ArrayList<Integer> indexDistribution = new ArrayList<Integer>();

        html = html.replaceAll("(?is)<!DOCTYPE.*?>", "");
        html = html.replaceAll("(?is)<!--.*?-->", "");				// remove html comment
        html = html.replaceAll("(?is)<script.*?>.*?</script>", ""); // remove javascript
        html = html.replaceAll("(?is)<style.*?>.*?</style>", "");   // remove css
        html = html.replaceAll("&.{2,5};|&#.{2,5};", " ");			// remove special char
        html = html.replaceAll("(?is)<.*?>", "");

		lines = Arrays.asList(html.split("\n"));
		indexDistribution.clear();
		
		for (int i = 0; i < lines.size() - blocksWidth; i++) {
			int wordsNum = 0;
			for (int j = i; j < i + blocksWidth; j++) { 
				lines.set(j, lines.get(j).replaceAll("\\s+", ""));
				wordsNum += lines.get(j).length();
			}
			indexDistribution.add(wordsNum);
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
				for (int ii = start; ii <= end; ii++) {
					if (lines.get(ii).length() < 5) continue;
					tmp.append(lines.get(ii) + "\n");
				}
				String str = tmp.toString();
				if (str.contains("Copyright") ) continue;
				text.append(str);
				boolstart = boolend = false;
			}
		}

		return text.toString();

	}


    public static String getHash(String str){
        if(str != null && !"".equals(str)){
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] e = digest.digest(str.getBytes("UTF-8"));
                return Hex.encodeHexString(e);
            }catch(Exception e){
            }
        }
        return null;
    }

    public static String getMeta(String str,String name){
        String result = "";
        Document doc = Jsoup.parse(str);
        Elements metas =doc.head().getElementsByAttributeValue("name",name);
        for(Element e : metas){
            result = e.attr("content");
        }
        return result;
    }

    public static String getTitle(String text){
        String title = null;
        Document doc = Jsoup.parse(text);
        title  = doc.title();
        if(title ==null ){
            return "";
        }else{
            return title;
        }
    }

    public static String getH1(String str){
        String result = "";
        Document doc = Jsoup.parse(str);
        Elements h1 = doc.select("h1");
        for(Element e : h1){
            result = e.text();
        }
        return result;
    }

}
