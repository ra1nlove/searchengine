package com.buptnsrc.search.Parse;

import com.buptnsrc.search.resource.Apk;
import com.buptnsrc.search.resource.Sites;
import com.buptnsrc.search.resource.WebPage;
import com.buptnsrc.search.utils.SendData;

import java.net.URL;

/**
 * Created by rain on 17-2-24.
 */
public class ApkInfo {

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
                    if (apkhash != null && apkhash.equals(apk.getApkHash()) && !page.isDirty("fetchInterval")) {
                        int interval = page.getFetchInterval();
                        page.setFetchInterval(interval * 2);
                    }
                }
                page.setApk(apk.getApkHash());
    //            SendData.sendData(apk);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
