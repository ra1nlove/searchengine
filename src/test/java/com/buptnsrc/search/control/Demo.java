package com.buptnsrc.search.control;

import com.buptnsrc.search.page.UrlUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by rain on 17-5-31.
 */
public class Demo {
    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        URL url = new URL("http://9androidapps.com/?s=vpn");
        System.out.println(url.getHost());
        System.out.println(url.getAuthority());
        System.out.println(url.getDefaultPort());
        System.out.println(url.getFile());
        System.out.println(url.getPath());
        System.out.println(url.getProtocol());
        System.out.println(url.getRef());
        System.out.println(url.getUserInfo());
        System.out.println(url.getQuery());
        System.out.println("=================================");
        System.out.println(UrlUtils.unreverseUrl("com.9androidapps:http:80/?s=vpn"));
    }
}
