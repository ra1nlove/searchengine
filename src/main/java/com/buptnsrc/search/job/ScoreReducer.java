package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * Created by rain on 17-4-25.
 */
public class ScoreReducer extends GoraReducer<Text, WebPage, String, WebPage> {

    @Override
    public void reduce(Text url, Iterable<WebPage> pages, Context context) throws IOException, InterruptedException{

    }
}