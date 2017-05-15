package com.buptnsrc.search.download;

import com.buptnsrc.search.resource.WebPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rain on 17-2-24.
 */
public class PageDownload {

    private static Log log = LogFactory.getLog(PageDownload.class);
    static CloseableHttpClient httpclient= HttpClientManager.getHttpClient();

    /**
     * 对页面进行下载、解析
     * @param  page
     * @return 页面的内容
     */
    public static String download(WebPage page){
        ProxyPool proxys = new ProxyPool();
        String result = null;
        HttpGet httpget = null;
        int statuscode =0;
        while (true){
            String proxy = proxys.getProxy();
            if(proxy == null) break;
            try {
                httpget = new HttpGet(page.getUrl().toString());
                HttpHost host = new HttpHost(proxy, 8001, "http");
                RequestConfig requestConfig = RequestConfig.custom()
                        .setConnectTimeout(10000)
                        .setConnectionRequestTimeout(3000)
                        .setSocketTimeout(10000)
                        .setProxy(host)
                        .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                        .build();

                //设置请求头
                httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.79 Safari/537.1");
                httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                httpget.setHeader("Accept-Encoding", "gzip, deflate, sdch");
                httpget.setConfig(requestConfig);

                CloseableHttpResponse resp = httpclient.execute(httpget);

                statuscode = resp.getStatusLine().getStatusCode();
                page.setStatusCode(String.valueOf(statuscode));

                Header[] header = resp.getHeaders("Content-Type");
                if(header == null || !header[0].getValue().contains("text/html")){
                    log.info("download file fail "+" : "+page.getUrl().toString());
                    page.setStatus("end");
                    return result;
                }

                if(statuscode==200){
                    HttpEntity entity = resp.getEntity();
                    result = getContent(entity,page);
                    page.setRetriesSinceFetch(0);
                    page.setStatus("fetch");
                    log.info("download page success : "+page.getUrl().toString());
                    return result;
                }else if(statuscode>400 && statuscode <500){
                    page.setStatus("end");
                    log.info("download page "+statuscode+"     : "+page.getUrl().toString());
                    return result;
                }else{
                    page.setStatus("fetch");
                    log.info("download page "+statuscode+"     : "+page.getUrl().toString());
                }
            }catch (Exception e ){
                page.setStatus("fail");
                log.info("download page error "+e.getMessage()+" : "+page.getUrl().toString());
            }finally {
                httpget.abort();
            }
        }

        if(statuscode>=500 || (page.getStatus()!=null || "fail".equals(page.getStatus().toString()))) {
            int num = page.getRetriesSinceFetch();
            num++;
            page.setRetriesSinceFetch(num);
            if(num>3) {
                page.setStatus("end");
            }
        }

        page.setFetchTime(System.currentTimeMillis());
        return result;

    }

    /**
     * 页面编码类型的自动识别
     * @param entity
     * @param page
     * @return 页面的内容
     * @throws Exception
     */
    public static String getContent(HttpEntity entity,WebPage page) throws Exception{
        String content = null;
        String charset = null;
        ContentType reader = ContentType.get(entity);
        if(reader != null){
            charset = reader.getParameter("charset");
        }
        if(charset != null) {
            content = EntityUtils.toString(entity,charset);
        }else {
            String html = EntityUtils.toString(entity);
            charset = getCharset(html);
            if (charset.contains("gb")) {
                content = new String(html.getBytes("8859_1"), "gb2312");
            } else {
                content = new String(html.getBytes("8859_1"), "utf-8");
            }
        }

        page.setCharset(charset);
        return content;
    }

    /**
     * 对页面的Content-Type标签进行提取
     * @param page
     * @return 页面的编码类型
     */
    public static String getCharset(String page){
        String charset = "UTF-8";
        String regex ="meta.*charset=(.[^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(page);
        if(matcher.find()){
            charset = matcher.group(1).toString();
        }
        return charset;
    }
}
