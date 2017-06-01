package com.buptnsrc.search.job;

import com.buptnsrc.search.page.UrlUtils;
import com.buptnsrc.search.resource.WebPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * Created by rain on 17-4-25.
 */
public class ScoreReducer extends GoraReducer<Text, DoubleWritable, String, WebPage> {

    @Override
    public void reduce(Text url, Iterable<DoubleWritable> scores, Context context) throws IOException, InterruptedException{
        WebPage page = new WebPage();
        page.setUrl(UrlUtils.unreverseUrl(url.toString()));
        for(DoubleWritable score : scores) {
            page.setScores(score.get());
        }
        context.write(url.toString(),page);
    }
}