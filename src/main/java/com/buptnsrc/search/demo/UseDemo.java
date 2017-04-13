package com.buptnsrc.search.demo; /**
 * @author Xin Chen
 * Created on 2009-11-11
 * Updated on 2010-08-09
 * Email:  xchen@ir.hit.edu.cn
 * Blog:   http://hi.baidu.com/����ͬ��_����
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class UseDemo {
	
	public static void main(String[] args) throws IOException {

		String content = getHTML("http://ent.qq.com/a/20100417/000119.htm");


		System.out.println("got html");
		System.out.println(TextExtract.parse(content));
	}


	public static String getHTML(String strURL) throws IOException {
		URL url = new URL(strURL);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String s = "";
		StringBuilder sb = new StringBuilder("");
		while ((s = br.readLine()) != null) {
			sb.append(s + "\n");
		}
		return sb.toString();
	}
}
