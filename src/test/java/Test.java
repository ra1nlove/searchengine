import com.buptnsrc.search.download.PageDownload;
import com.buptnsrc.search.resource.WebPage;
import org.apache.gora.query.Query;
import org.apache.gora.store.DataStore;
import org.apache.gora.store.DataStoreFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;

/**
 * Created by rain on 17-2-24.
 */
public class Test {

    public static void main(String[] args) throws Exception{
        DataStore<String,WebPage> dataStore = DataStoreFactory.getDataStore(String.class, WebPage.class, new Configuration());
        WebPage page = dataStore.get("http://wx.sina.com.cn/");
        System.out.print(page);
    }

}
