package com.buptnsrc.search.job;

import com.buptnsrc.search.page.Bayes;
import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.hadoop.io.Text;
import java.io.IOException;

/**
 * Created by rain on 17-4-25.
 */
public class ScoreMapper extends GoraMapper<String,WebPage,Text,WebPage>{

    @Override
    public void map(String url,WebPage page,Context context) throws IOException, InterruptedException {
    }
}
