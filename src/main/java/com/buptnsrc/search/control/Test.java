package com.buptnsrc.search.control;

import com.buptnsrc.search.resource.WebPage;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.gora.store.DataStoreFactory;
import org.apache.gora.store.DataStore;

/**
 * Created by rain on 17-2-23.
 */
public class Test {

    public static void main(String[] args) {
        try {
            Configuration conf = HBaseConfiguration.create();
            conf.set("hbase.zookeeper.quorum", "10.108.114.127");
            conf.set("hbase.zookeeper.property.clientPort", "2181");
            DataStore<String, WebPage> pageStore;
            pageStore = DataStoreFactory.getDataStore(String.class, WebPage.class, conf);
            WebPage page = new WebPage();
            page.setUrl("http://www.wandoujia.com/apps");
            pageStore.put("http://www.wandoujia.com/apps",page);
            pageStore.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
