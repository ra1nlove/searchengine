package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;


/**
 * Created by rain on 17-2-24.
 */
public class FetcherReducer extends GoraReducer<Text, WebPage, String, WebPage> {

    BloomFilter<byte[]> bloomFilter  = BloomFilter.create(Funnels.byteArrayFunnel(),10000000);

    @Override
    public void reduce(Text key, Iterable<WebPage> pages, Context context) throws IOException, InterruptedException{

        for(WebPage page : pages){
            if(bloomFilter.mightContain(page.getUrl().toString().getBytes())){
                continue;
            }else{
                bloomFilter.put(page.getUrl().toString().getBytes());
                context.getCounter("FetchJob","new").increment(1);
            }
            page.clearDirty();
            for(int i=0;i<10;i++){
                if(key.toString().charAt(i) == '1'){
                    String field = WebPage._ALL_FIELDS[i];
                    page.setDirty(field);
                }
            }
            page.setFetchTime(System.currentTimeMillis());
            context.write(page.getUrl().toString(), page);
        }
    }

}
