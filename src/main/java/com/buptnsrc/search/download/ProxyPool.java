package com.buptnsrc.search.download;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by rain on 17-4-18.
 */
public class ProxyPool {

    static String[] proxys = {"proxy.asec.buptnsrc.com","proxy1.asec.buptnsrc.com","proxy2.asec.buptnsrc.com"};
    Queue<String> queue = new LinkedList<>();

    public ProxyPool(){
        Random random = new Random();
        int start = random.nextInt(3);
        int len = proxys.length;
        while(len-->0){
            queue.add(proxys[start%3]);
            start++;
        }
    }

    public String getProxy(){
        return queue.poll();
    }



}
