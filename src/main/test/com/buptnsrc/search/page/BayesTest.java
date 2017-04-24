package com.buptnsrc.search.page;

import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* Bayes Tester. 
* 
* @author <Authors name> 
* @since <pre>四月 21, 2017</pre> 
* @version 1.0 
*/ 
public class BayesTest { 

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
    *
    * Method: getStopWord()
    *
    */
    @Test
    public void testGetStopWord() throws Exception {
        //TODO: Test goes here...
    }

    /**
    *
    * Method: train()
    *
    */
    @Test
    public void testTrain() throws Exception {
        //TODO: Test goes here...
    }

    /**
    *
    * Method: getProb(String text, String category)
    *
    */
    @Test
    public void testGetProb() throws Exception {
        //TODO: Test goes here...
    }

    /**
    *
    * Method: classify(String text)
    *
    */

    @Test
    public void testClassify() throws Exception {
        //TODO: Test goes here...
        WebPage page = new WebPage();
        page.setUrl("http://sports.sina.com.cn/g/laliga/2017-04-24/doc-ifyepsec0546113.shtml");
        String content = PageDownload.download(page);
        content = TextExtract.getText(content);
        System.out.println(Bayes.classify(content));
    }
}
