package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import java.io.IOException;

/**
 * Created by rain on 17-4-25.
 */
public class ScoreMapper extends GoraMapper<String,WebPage,Text,DoubleWritable>{

    @Override
    public void map(String url,WebPage page,Context context) throws IOException, InterruptedException {
        double bayes = page.getBayes();
        double pagerank = page.getPagerank();
        double score = 0.4*bayes + 0.6*pagerank;
//        System.out.println(score + url);
        context.write(new Text(url),new DoubleWritable(score));
    }
}
