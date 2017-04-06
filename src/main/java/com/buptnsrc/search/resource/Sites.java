package com.buptnsrc.search.resource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rain on 17-2-23.
 */
public class Sites {

    public static List<String> urls = new ArrayList<String>();

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
                String url= element.getElementsByTagName("url").item(0).getTextContent();
                urls.add(url);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
