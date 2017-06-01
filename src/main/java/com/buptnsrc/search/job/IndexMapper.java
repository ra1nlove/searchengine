package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import java.io.IOException;


/**
 * Created by rain on 17-3-30.
 */
public class IndexMapper extends GoraMapper<String,WebPage, NullWritable ,MapWritable> {

    @Override
    public void map(String url,WebPage page,Context context) throws IOException,InterruptedException {
        MapWritable doc = new MapWritable();
        doc.put(new Text("title"),new Text(page.getTitle().toString()));
        doc.put(new Text("url"),new Text(page.getUrl().toString()));
        doc.put(new Text("desc"),new Text(page.getDescription().toString()));
        doc.put(new Text("keyword"),new Text(page.getKeywords().toString()));
//        doc.put(new Text("h1"),new Text(page.getKeywords().toString()));
        if(page.getStatus().toString().equals("detail")){
            String content = page.getContent().toString();
            doc.put(new Text("content"),new Text(content));
        }
        context.getCounter("IndexJob", "index").increment(1);
        context.write(NullWritable.get(),doc);
    }

}
