package com.buptnsrc.search.job;

import com.buptnsrc.search.parse.ApkInfo;
import com.buptnsrc.search.parse.UrlInfo;
import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.gora.mapreduce.GoraMapper;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;

/**
 * Created by rain on 17-2-24.
 */
public class FetcherMapper extends GoraMapper<String,WebPage, Text, WebPage> {

    private Log log = LogFactory.getLog(FetcherMapper.class);
    private Queue<WebPage> pages = new ConcurrentLinkedDeque<WebPage>();
    private final CountDownLatch endGate = new CountDownLatch(50);

    private void addUrls(Context context)throws InterruptedException, IOException {
        while(context.nextKeyValue()){
            pages.add(context.getCurrentValue());
            context.getCounter("FetchJob","all").increment(1);
        }
    }

    private class FetchThread extends Thread{

        Context context ;
        int id ;

        public FetchThread(Context context,int id){
            this.context = context;
            this.id = id;
        }

        @Override
        public void run(){
            try{
                while (true) {
                    WebPage page = pages.poll();
                    if (page != null){
                            String result = PageDownload.download(page);
                        if (result != null) {
                            context.getCounter("FetchJob","success").increment(1);
                            ApkInfo.getApk(page, result);
                            List<WebPage> pages = UrlInfo.getUrl(page, result);
                            for (WebPage newpage : pages) {
                                context.write(new Text("1000000000"), newpage);
                            }
                        }
                        char[] dirty = new char[10];
                        for(int i=0;i<10;i++) {
                            if (page.isDirty(i)) {
                                dirty[i] = '1';
                            } else {
                                dirty[i] = '0';
                            }
                        }
                        context.write(new Text(String.valueOf(dirty)), page);
                    }else{
                        break;
                    }
                }
            }catch (Exception e){
                log.error("fetch error!",e);
            }finally {
                endGate.countDown();
            }
        }

    }

    @Override
    public void run(Context context) throws InterruptedException, IOException {
        try{
            addUrls(context);
            for(int i = 0 ;i<50;i++){
                FetchThread fetchThread = new FetchThread(context,i);
                fetchThread.start();
            }
            endGate.await();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            cleanup(context);
        }
    }
}
