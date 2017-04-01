package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import com.buptnsrc.search.utils.StringTool;
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
        doc.put(new Text("url"),new Text(url));
        String content = page.getContent().toString();
        context.getCounter("IndexJob", "index").increment(1);
        doc.put(new Text("content"),new Text(StringTool.getContext(content)));
        context.write(NullWritable.get(),doc);
    }

}
