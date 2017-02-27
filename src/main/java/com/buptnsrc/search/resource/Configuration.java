package com.buptnsrc.search.resource;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by rain on 17-2-23.
 */
public class Configuration {

    public static String get(String pro,String pro2){
        String str = null;
        Properties property= new Properties();
        InputStream in = Configuration.class.getClass().getResourceAsStream("/configuration.properties");
        try {
            property.load(in);
            str =property.getProperty(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(str == null){
            return pro2;
        }
        return str;
    }

}
