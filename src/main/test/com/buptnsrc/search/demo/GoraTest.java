package com.buptnsrc.search.demo;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.query.Query;
import org.apache.gora.query.Result;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.junit.Test;

/**
 * Created by rain on 17-4-21.
 */
public class GoraTest {

    @Test
    public void getAllData() throws Exception {
        DataStore<String, WebPage> pageStore;
        Configuration conf = HBaseConfiguration.create();
        pageStore = DataStoreFactory.getDataStore(String.class, WebPage.class, conf);
        Query<String,WebPage> query = pageStore.newQuery();
        Result<String,WebPage> result = query.execute();
        while(result.next()){
            WebPage page = result.get();
            System.out.println(page.getPagerank()+"   "+page.getUrl());
        }
    }

    @Test
    public void getData() throws Exception {
        DataStore<String, WebPage> pageStore;
        Configuration conf = HBaseConfiguration.create();
        pageStore = DataStoreFactory.getDataStore(String.class, WebPage.class, conf);
        WebPage page = pageStore.get("http://tech.sina.com.cn/zt_d/zhouhangjiayueting");
        System.out.println(page.getPagerank()+"   "+page.getUrl());
    }

}
