package com.buptnsrc.search.utils;

import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.gora.util.GoraException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class GoraUtils {

    private DataStore<String, WebPage> pageStore;

    public GoraUtils() throws GoraException {
        Configuration conf = HBaseConfiguration.create();
        pageStore = DataStoreFactory.getDataStore(String.class, WebPage.class, conf);
    }

    public GoraUtils(String host, int port) throws GoraException {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", host);
        conf.set("hbase.zookeeper.property.clientPort", port+"" );
        pageStore = DataStoreFactory.getDataStore(String.class, WebPage.class, conf);
    }

    /**
     * 在webpage表中写入
     * @param key
     * @param webPage webpage对象
     */
    public void add(String key, WebPage webPage){
        pageStore.put(key, webPage);
    }

    /**
     * 从hbase中根据key取到webpage对象
     *
     * @param key
     * @return
     * @throws GoraException
     */
    public WebPage get(String key){
        WebPage webPage = pageStore.get(key);
        return webPage;
    }

    /**
     * 从hbase中删除对应key的数据
     *
     * @param key
     * @throws GoraException
     */
    public void delete(String key) {
        pageStore.delete(key);
    }

    public void close(){
        pageStore.close();
    }
}