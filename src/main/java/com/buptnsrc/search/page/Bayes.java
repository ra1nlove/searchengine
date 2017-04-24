package com.buptnsrc.search.page;

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
        while( (s=br.readLine())!= null ){
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

	public static Set<String> getFeature(String text){

		double max = 0;
		text = text.replaceAll("\\s","").replaceAll("\r\n","");
		List<String> wordList = seg.sentenceProcess(text);
		Map<String,Double> wordnum = new HashMap<>();
		for(String word : wordList){
			if(!stopword.contains(word)) {
				wordnum.put(word, wordnum.getOrDefault(word, 0.0) + 1);
				if(wordnum.get(word)>max){
					max = wordnum.get(word);
				}
			}
		}

//		for(Map.Entry<String,Double> entry : wordnum.entrySet()){
//			String key = entry.getKey();
//			Double value = 0.4 + 0.6 * (entry.getValue()/max);
//			wordnum.put(key,value);
//		}

		List<Map.Entry<String,Double>> words = new ArrayList<>(wordnum.entrySet());
		Collections.sort(words, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
			    System.out.println((o1.getKey()).toString().compareTo(o2.getKey()));
                return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});

		Set<String> result = new HashSet<String>();
		for(int i=0;i<words.size()&&i<20;i++){
			result.add(words.get(i).getKey());
		}

		return result;
	}

	public  static double getProb(String text,String category) {
		double prob = 1.0 ;
		double prob_c = 1.0 ;
		Set<String> wordSet = getFeature(text);
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

}
