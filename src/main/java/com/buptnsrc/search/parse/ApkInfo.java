package com.buptnsrc.search.parse;

import com.buptnsrc.search.resource.Apk;
import com.buptnsrc.search.resource.Sites;
import com.buptnsrc.search.resource.WebPage;
import com.buptnsrc.search.utils.SendData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;

/**
 * Created by rain on 17-2-24.
 */
public class ApkInfo {

    static Log log = LogFactory.getLog(ApkInfo.class);

    public static void getApk(WebPage page, String content){
        Apk apk = null;
        try{
            URL url = new URL(page.getUrl().toString());
            String packagename = Sites.packagemap.get(url.getHost());
            ApkDetail apkDetail = (ApkDetail)Class.forName(packagename).newInstance();
            apk = apkDetail.getApkInfo(page.getUrl().toString(),content);
            if(apk != null){
                if(page.getApk() != null) {
                    String apkhash = page.getApk().toString();
                    if ( apkhash.equals(apk.getApkHash()) && !page.isDirty("fetchInterval") ) {
                        int interval = page.getFetchInterval();
                        page.setFetchInterval(interval * 2);
                    }else if( !apkhash.equals(apk.getApkHash()) ){
                        SendData.sendData(apk);
                        page.setFetchInterval(1);
                    }
                }else{
                    SendData.sendData(apk);
                }
                page.setApk(apk.getApkHash());

                log.info("download page getApkInfo : "+page.getUrl().toString());
            }else{
                log.info("download page getNothing : "+page.getUrl().toString());
            }
        }catch(Exception e){
            log.info("getApk error ! ",e);
        }

    }

}
