package com.buptnsrc.search.resource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rain on 17-2-23.
 */
public class Sites {

    public static List<String>       urls       = new ArrayList<String>();
    public static Map<String,String> regexmap   = new HashMap<String,String>();
    public static Map<String,String> packagemap = new HashMap<String,String>();
    public static Map<String,String> channelId = new HashMap<String,String>();

    static{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            InputStream in = Object.class.getClass().getResourceAsStream("/sites.xml");
            Document document = db.parse(in);
            NodeList list = document.getElementsByTagName("site");
            for(int i =0 ;i<list.getLength();i++){
                Element element = (Element)list.item(i);
                String id = element.getAttribute("id");
                String url= element.getElementsByTagName("url").item(0).getTextContent();
                String regex= element.getElementsByTagName("regex").item(0).getTextContent();
                String packagename = element.getElementsByTagName("package").item(0).getTextContent();
                URL pageurl = new URL(url);
                String host = pageurl.getHost();
                urls.add(url);
                regexmap.put(host, regex);
                packagemap.put(host, packagename);
                channelId.put(host,id);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
