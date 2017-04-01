package com.buptnsrc.search.job;

import com.buptnsrc.search.parse.UrlInfo;
import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.gora.mapreduce.GoraMapper;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;

/**
 * Created by rain on 17-2-24.
 */
public class FetcherMapper extends GoraMapper<String,WebPage, String, WebPage> {

    BloomFilter<byte[]> bloomFilter  = BloomFilter.create(Funnels.byteArrayFunnel(),1000000);

    private Log log = LogFactory.getLog(FetcherMapper.class);
    private Queue<WebPage> pages = new ConcurrentLinkedDeque<WebPage>();
    private final CountDownLatch endGate = new CountDownLatch(20);
    private int nums = 5000;

    private void addUrls(Context context)throws InterruptedException, IOException {
        while(context.nextKeyValue()){
            if(nums--<0) return;
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
                            List<WebPage> pages = UrlInfo.getUrl(page, result);
                            for (WebPage newpage : pages) {
                                context.getCounter("FetchJob", "newall").increment(1);
                                if(bloomFilter.mightContain(newpage.getUrl().toString().getBytes())){
                                    continue;
                                }else{
                                    bloomFilter.put(newpage.getUrl().toString().getBytes());
                                    context.write(newpage.getUrl().toString(), newpage);
                                    context.getCounter("FetchJob", "new").increment(1);
                                }
                            }
                        }
                        context.write(page.getUrl().toString(), page);
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
            for(int i = 0 ;i<20;i++){
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
