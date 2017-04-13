package com.buptnsrc.search.Classify;

import com.huaban.analysis.jieba.JiebaSegmenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class Bayes {
	
	public static List<String> stopword = new ArrayList<String>();
	public static Map<String,Integer> wordmap = new HashMap<String,Integer>();
    public static JiebaSegmenter seg = new JiebaSegmenter();
	
	static{
		try{
			Bayes.getStopWord();
			Bayes.train();
		}catch(Exception e){		
		   e.printStackTrace();
	   }
	}
	
	public static void getStopWord() throws IOException{
			InputStream in = Object.class.getClass().getResourceAsStream("/stopword.txt");	
			BufferedReader br = new BufferedReader (new InputStreamReader(in));
			String s = null ;
			while( ( s=br.readLine()) != null ){
				stopword.add(s.trim());
			}
			br.close();
			in.close();
	}
	
	public static void train() throws IOException{
		InputStream in = Object.class.getClass().getResourceAsStream("/wordmap.txt");	
		BufferedReader br = new BufferedReader (new InputStreamReader(in));
		String s = null;
		while( (s=br.readLine()) != null){
			String key = s.split("\t")[0];
			String num = s.split("\t")[1];
			wordmap.put(key, Integer.valueOf(num));	
		}
		br.close();
		in.close();
	}

	public  static double getProb(String text,String category) {

		double prob = 1.0 ;
		double prob_c = 1.0 ;  


		List<String> wordList = seg.sentenceProcess(text);
		Set<String> wordSet = new HashSet<String>();
		wordSet.addAll(wordList);
		for(String word : wordSet){
			if(!stopword.contains(word)){
				String key = category+"_"+word;
				if(wordmap.containsKey(key)){
					double dcw = wordmap.get(key);
					prob_c = (dcw+1)/(10000);
				}else{
					prob_c = 1.0/(10000);
				}
				prob = prob*prob_c*200;
			}
		}
		return prob/2;

	}
	
	public static boolean classify(String text) {
		double tech = getProb(text,"科技");
		double other = getProb(text,"其它");
		if(tech>other){
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args) throws IOException{
	}
	
}
