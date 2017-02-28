package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.WebPage;
import com.buptnsrc.search.utils.StringArray;
import org.apache.avro.util.Utf8;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.gora.filter.FilterOp;
import org.apache.gora.filter.SingleFieldValueFilter;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.gora.mapreduce.GoraReducer;
import org.apache.gora.query.Query;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.CommonConfigurationKeys;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by rain on 17-2-24.
 */
public class FetcherJob {

    private static final Collection<WebPage.Field> FIELDS = new HashSet<WebPage.Field>();

    public FetcherJob(){
        FIELDS.add(WebPage.Field.STATUS);
        FIELDS.add(WebPage.Field.REFERRER);
        FIELDS.add(WebPage.Field.FETCH_INTERVAL);
        FIELDS.add(WebPage.Field.URL);
        FIELDS.add(WebPage.Field.CHARSET);
        FIELDS.add(WebPage.Field.MD5);
        FIELDS.add(WebPage.Field.RETRIES_SINCE_FETCH);
        FIELDS.add(WebPage.Field.APK);
    }

    public void fetch()throws Exception{

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(FetcherJob.class);
        job.setJobName("fetcherJob");

        DataStore<String,WebPage> dataStore = DataStoreFactory.getDataStore(String.class, WebPage.class, new Configuration());
        Query<String, WebPage> query = dataStore.newQuery();
        query.setFields(StringArray.toStringArray(FIELDS));
        SingleFieldValueFilter<String,WebPage> filter = new SingleFieldValueFilter<>();
        filter.setFieldName("status");
        filter.setFilterOp(FilterOp.EQUALS);
        filter.setFilterIfMissing(true);
        List<Object> list = new ArrayList<Object>();
        list.add(new Utf8("generate"));
        filter.setOperands(list);
        query.setFilter(filter);

        GoraMapper.initMapperJob(job, query, dataStore, Text.class, WebPage.class,FetcherMapper.class ,  true);
        GoraReducer.initReducerJob(job, dataStore, FetcherReducer.class);

        job.waitForCompletion(true);
        job.killJob();

        dataStore.close();

    }

    public static void main(String[] args){
        try{
            FetcherJob job = new FetcherJob();
            job.fetch();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
