package com.buptnsrc.search.job;

import com.buptnsrc.search.page.UrlUtils;
import com.buptnsrc.search.resource.Sites;
import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.log4j.Logger;

import java.net.URL;
import java.text.SimpleDateFormat;

/**
 * Created by rain on 17-2-23.
 * 添加新的url到Hbase中
 */
public class InjectorJob {

    public static Logger log = Logger.getLogger(InjectorJob.class);

    public void inject() throws Exception{

        log.info("=======================InjectorJob==============================");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long start = System.currentTimeMillis();
        log.info("InjectorJob : starting at " + sdf.format(start));

        Configuration conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum", "10.108.114.127");
//        conf.set("hbase.zookeeper.property.clientPort", "2181");

        DataStore<String,WebPage> pageStore = DataStoreFactory.getDataStore(String.class, WebPage.class, conf) ;
        for(String link : Sites.urls){
            link = UrlUtils.urlNormalize(link);
            WebPage page = new WebPage();
            page.setUrl(link);
            pageStore.put(UrlUtils.reverseUrl(link),page);
        }
        pageStore.close();

        long end = System.currentTimeMillis();
        log.info("InjectorJob : ending at " + sdf.format(end));
        log.info("=======================InjectorJob==============================");

    }

    public static void main(String[] args) throws Exception {
        try {
            InjectorJob job = new InjectorJob();
            job.inject();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
