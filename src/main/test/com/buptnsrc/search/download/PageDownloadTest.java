package com.buptnsrc.search.download;

import com.buptnsrc.search.resource.WebPage;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* PageDownload Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 20, 2017</pre> 
* @version 1.0 
*/ 
public class PageDownloadTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: download(WebPage page) 
* 
*/ 
@Test
public void testDownload() throws Exception { 
//TODO: Test goes here...
    WebPage page = new WebPage();
    page.setUrl("http://sina.com.cn");
    String content = PageDownload.download(page);

} 

/** 
* 
* Method: getContent(HttpEntity entity, WebPage page) 
* 
*/ 
@Test
public void testGetContent() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: getCharset(String page) 
* 
*/ 
@Test
public void testGetCharset() throws Exception { 
//TODO: Test goes here... 
} 


} 
