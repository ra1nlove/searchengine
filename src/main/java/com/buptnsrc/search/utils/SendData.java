package com.buptnsrc.search.utils;

import com.buptnsrc.search.resource.Apk;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * Created by rain on 17-2-24.
 */
public class SendData {

    static Logger log = Logger.getLogger(SendData.class);

    public static void sendData(Apk apk) {
        Jedis jedis = null;
        try{
            String data = JsonUtils.getSenderData(apk);
            StringBuilder formatResult = new StringBuilder(data);
            String insertString = "\"subtaskId\":\"" + "" + "\",";
            formatResult.insert(1, insertString);
            jedis.sadd("result", formatResult.toString());
            log.info("send data to redis sucessful");
        }catch(Exception e ){
            log.error("sendData error!",e);
        }finally {
            jedis.close();
        }
    }
}
