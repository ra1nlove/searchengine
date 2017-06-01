package com.buptnsrc.search.job;

import com.buptnsrc.search.page.StringArray;
import com.buptnsrc.search.resource.WebPage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.avro.util.Utf8;
import org.apache.gora.filter.FilterOp;
import org.apache.gora.filter.SingleFieldValueFilter;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.gora.query.Query;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

/**
 * 产生可以抓取的url
 * Created by rain on 17-2-23.
 */
public class GeneratorJob {

    private Collection<WebPage.Field> FIELDS = new HashSet<>();

    public GeneratorJob(){
        FIELDS.add(WebPage.Field.STATUS);
        FIELDS.add(WebPage.Field.FETCH_TIME);
        FIELDS.add(WebPage.Field.FETCH_INTERVAL);
        FIELDS.add(WebPage.Field.URL);
    }

    private void generate() throws Exception{

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJobName("GeneratorJob");
        job.setJarByClass(GeneratorJob.class);

        DataStore<String,WebPage> dataStore = DataStoreFactory.getDataStore(String.class, WebPage.class, new Configuration());
        Query<String, WebPage> query = dataStore.newQuery();
        query.setFields(StringArray.toStringArray(FIELDS));
        SingleFieldValueFilter<String,WebPage> filter = new SingleFieldValueFilter<>();
        filter.setFieldName("status");
        filter.setFilterOp(FilterOp.NOT_EQUALS);
        List<Object> list = new ArrayList<Object>();
        list.add(new Utf8("end"));
        filter.setOperands(list);
        query.setFilter(filter);

        GoraMapper.initMapperJob(job,query,dataStore, Text.class,WebPage.class,GeneratorMapper.class,true);
        GoraReducer.initReducerJob(job,dataStore,GeneratorReducer.class);

        job.waitForCompletion(true);
        job.killJob();

    }

    public static void main(String[] args) {
        try {
            GeneratorJob job = new GeneratorJob();
            job.generate();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
