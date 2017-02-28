package com.buptnsrc.search.parse;

import com.buptnsrc.search.resource.Apk;

/**
 * Created by rain on 17-2-24.
 */
public interface ApkDetail {
    public Apk getApkInfo(String url, String content) throws Exception;
}
