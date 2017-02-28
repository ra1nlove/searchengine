package com.buptnsrc.search.job;

import com.buptnsrc.search.resource.Sites;
import com.buptnsrc.search.utils.StringArray;
import com.buptnsrc.search.resource.WebPage;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
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
        conf.set(CommonConfigurationKeys.IO_SERIALIZATIONS_KEY, "org.apache.hadoop.io.serializer.WritableSerialization");
        Job job = Job.getInstance(conf);
        job.setJobName("GeneratorJob");
        job.setJarByClass(GeneratorJob.class);

        DataStore<String,WebPage> dataStore = DataStoreFactory.getDataStore(String.class, WebPage.class, new Configuration());
        Query<String, WebPage> query = dataStore.newQuery();
        query.setFields(StringArray.toStringArray(FIELDS));
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
