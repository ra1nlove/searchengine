package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.util.Calendar;

public class GeneratorMapper extends GoraMapper<String,WebPage,Text,WebPage>{

    @Override
    public void map(String url,WebPage page,Context context) throws IOException,InterruptedException {
        CharSequence status = page.getStatus();
        long fetchtime = page.getFetchTime();
        int inteval = page.getFetchInterval();
        if(status == null || fetchtime == 0){
            context.write(new Text(url),page);
        }else if(fetchtime > 0){
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(fetchtime);
            cal.add(Calendar.DAY_OF_WEEK, inteval);
            if(cal.compareTo(Calendar.getInstance()) == -1){
                context.write(new Text(url),page);
            }
        }
        context.getCounter("generator","all").increment(1);
    }

}
