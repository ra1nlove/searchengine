package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import com.buptnsrc.search.utils.StringArray;
import org.apache.avro.util.Utf8;
import org.apache.gora.filter.FilterOp;
import org.apache.gora.filter.SingleFieldValueFilter;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.gora.query.Query;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.elasticsearch.hadoop.mr.EsOutputFormat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rain on 17-3-30.
 */
public class IndexJob {

    private static final Collection<WebPage.Field> FIELDS = new HashSet<WebPage.Field>();

    public IndexJob(){
        FIELDS.add(WebPage.Field.STATUS);
        FIELDS.add(WebPage.Field.URL);
        FIELDS.add(WebPage.Field.CONTENT);
        FIELDS.add(WebPage.Field.TITLE);
        FIELDS.add(WebPage.Field.DESCRIPTION);
        FIELDS.add(WebPage.Field.H1);
        FIELDS.add(WebPage.Field.KEYWORDS);
    }

    public void index() throws Exception {

        DataStore<String,WebPage> dataStore = DataStoreFactory.getDataStore(String.class, WebPage.class, new Configuration());
        Query<String, WebPage> query = dataStore.newQuery();
        query.setFields(StringArray.toStringArray(FIELDS));
        SingleFieldValueFilter<String,WebPage> filter = new SingleFieldValueFilter<>();
        filter.setFieldName("status");
        filter.setFilterOp(FilterOp.EQUALS);
        filter.setFilterIfMissing(true);
        List<Object> list = new ArrayList<Object>();
        list.add(new Utf8("index"));
        filter.setOperands(list);
        query.setFilter(filter);

        Configuration conf = new Configuration();
        conf.setBoolean("mapred.map.tasks.speculative.execution", false);
        conf.setBoolean("mapred.reduce.tasks.speculative.execution", false);
        conf.set("es.nodes", "10.108.113.231:9200");
        conf.set("es.resource", "page/fulltext");
        conf.set("es.mapping.id", "url");

        Job job = Job.getInstance(conf);
        job.setJarByClass(IndexJob.class);
        job.setJobName("IndexJob");
        GoraMapper.initMapperJob(job, query, dataStore, NullWritable.class, MapWritable.class,IndexMapper.class ,  true);
        job.setOutputFormatClass(EsOutputFormat.class);
        job.setReducerClass(Reducer.class);
        job.setNumReduceTasks(0);
        job.waitForCompletion(true);
        dataStore.close();
        
    }


    public static void main(String[] args){
        try {
            IndexJob job = new IndexJob();
            job.index();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
