import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import org.apache.hadoop.io.IntWritable;

/**
 * Created by rain on 17-2-24.
 */
public class Test {

    public static void main(String[] args) throws Exception{
        WebPage page = new WebPage();
        page.setUrl("http://zhuanlan.sina.com.cn/");
        System.out.println(PageDownload.download(page));
    }

}
