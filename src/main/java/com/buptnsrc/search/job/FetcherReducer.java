package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by rain on 17-2-24.
 */
public class FetcherReducer extends GoraReducer<Text, WebPage, String, WebPage> {

    @Override
    public void reduce(Text key, Iterable<WebPage> pages, Context context) throws IOException, InterruptedException{
        for(WebPage page : pages){
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
