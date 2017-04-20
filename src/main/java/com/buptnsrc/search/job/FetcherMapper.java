package com.buptnsrc.search.job;

import com.buptnsrc.search.page.*;
import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.gora.mapreduce.GoraMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;

/**
 * Created by rain on 17-2-24.
 */
public class FetcherMapper extends GoraMapper<String,WebPage, String, WebPage> {

    BloomFilter<byte[]> bloomFilter = BloomFilter.create(Funnels.byteArrayFunnel(),1000000);

    private Log log = LogFactory.getLog(FetcherMapper.class);
    private Queue<WebPage> pages = new ConcurrentLinkedDeque<WebPage>();
    private final CountDownLatch endGate = new CountDownLatch(20);

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
            String url = null;
            try{

                while (true) {
                    WebPage page = pages.poll();
                    if (page != null){
                        url = page.getUrl().toString();
                        String result = PageDownload.download(page);
                        if (result != null && page.getStatusCode().toString().equals("200")) {
                            context.getCounter("FetchJob","success").increment(1);
                            parse(page, result, context);
                        }
                        context.write(page.getUrl().toString(), page);
                    }else{
                        break;
                    }
                }
            }catch (Exception e){
                log.error(url,e);
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

    public void parse(WebPage page,String result,Context context) throws Exception{

        String[] words = {"科技","IT","互联网","通讯","3C","数码","手机","笔记本"};

        String keywords = TextExtract.getMeta(result,"keywords");
        String des = TextExtract.getMeta(result,"description");
        String h1 = TextExtract.getH1(result);
        String title = TextExtract.getTitle(result);
        Map<CharSequence,CharSequence> pages = UrlUtils.getAllUrls(page.getUrl().toString(), result);

        boolean headtopic = false;

        for(String word : words){
            if(title.contains(word)||keywords.contains(word)||des.contains(word)){
                headtopic = true;
                break;
            }
        }

        Boolean detailPage = PageClassify.DetailPageClassify(result);
        String hash = "";
        if(detailPage){
            String content = TextExtract.getText(result);
            Boolean topic = Bayes.classify(content);
            if(headtopic || topic){
                hash = SimHash.simHash(content);
                page.setContent(content);
                page.setRelate("true");
                page.setType("detail");
            }else{
                page.setRelate("false");
                page.setStatus("end");
                page.setType("detail");
                return;
            }
        }else{
            if(headtopic){
                String mes = "";
                for(CharSequence l : pages.keySet()){
                    mes+=l.toString();
                }
                hash = TextExtract.getHash(mes);
                page.setRelate("true");
                page.setType("list");
            }else{
                page.setRelate("false");
                page.setStatus("end");
                page.setType("list");
                return;
            }

        }

        if(page.getMd5() != null && SimHash.hammingDistance(hash,page.getMd5().toString())<4){
            int interval = page.getFetchInterval();
            page.setFetchInterval(interval*2);
            page.setStatus("fetch");
        }else{
            page.setStatus("index");
        }

        page.setTitle(title);
        page.setDescription(des);
        page.setKeywords(keywords);
        page.setH1(h1);
        page.setOutlinks(pages);
        page.setMd5(hash);

        for (CharSequence newurl : pages.keySet()) {
            context.getCounter("FetchJob", "newall").increment(1);
            if(bloomFilter.mightContain(newurl.toString().getBytes())){
                continue;
            }else{
                bloomFilter.put(newurl.toString().getBytes());
                WebPage newpage = new WebPage();
                newpage.setUrl(newurl);
                context.write(newurl.toString(), newpage);
                context.getCounter("FetchJob", "new").increment(1);
            }
        }

    }

}
