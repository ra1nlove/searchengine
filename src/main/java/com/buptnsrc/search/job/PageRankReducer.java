package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * Created by rain on 17-4-5.
 */
public class PageRankReducer extends GoraReducer<Text, FloatWritable, String, WebPage>{

    @Override
    public void reduce(Text url ,Iterable<FloatWritable> scores,Context context) throws IOException, InterruptedException {
        Float score = 0.15f;
        for(FloatWritable s : scores){
            score += 0.85f * s.get();
        }
        WebPage page = new WebPage();
        page.setScore(score);
        context.write(url.toString(),page);
    }
}
