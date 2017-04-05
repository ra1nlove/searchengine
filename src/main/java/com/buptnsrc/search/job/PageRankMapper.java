package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.Map;

/**
 * Created by rain on 17-4-5.
 */
public class PageRankMapper extends GoraMapper<String,WebPage,Text,FloatWritable>{

    @Override
    public void map(String url,WebPage page,Context context) throws IOException, InterruptedException {
        Map<CharSequence,CharSequence> outlinks = page.getOutlinks();
        int linknumber = outlinks.size();
        if(linknumber>0){
            for(CharSequence outlink : outlinks.keySet()){
                float score = page.getScore()/linknumber;
                context.write(new Text(outlink.toString()),new FloatWritable(score));
            }
        }
    }

}
