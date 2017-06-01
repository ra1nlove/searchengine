package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class GeneratorReducer extends GoraReducer<Text, WebPage, String, WebPage> {

    Log log = LogFactory.getLog(GeneratorReducer.class);

    @Override
    public void reduce(Text url, Iterable<WebPage> pages, Context context) throws IOException, InterruptedException{
        for(WebPage page : pages){
            if(page.getStatus()!=null && "index".equals(page.getStatus().toString())){
                page.setStatus("fetch");
            }else {
                page.clearDirty();
                page.setStatus("generate");
                context.getCounter("generator", "urls").increment(1);
                log.info("generate url : " + page.getUrl().toString());
            }
            context.write(url.toString(), page);
        }
    }

}

