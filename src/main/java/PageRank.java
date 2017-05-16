import com.google.common.collect.Iterables;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.ClientProtos;
import org.apache.hadoop.hbase.util.Base64;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by rain on 17-5-12.
 */
public class PageRank {

    public static void main(String[] args) {
        try {
            SparkConf sparkConf = new SparkConf().setAppName("demo").setMaster("local");
            JavaSparkContext sc = new JavaSparkContext(sparkConf);

            Configuration conf = HBaseConfiguration.create();
            conf.set(TableInputFormat.INPUT_TABLE, "webpage");
            conf.set("hbase.zookeeper.quorum", "10.108.114.127");
            conf.set("hbase.zookeeper.property.clientPort", "2181");

            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes("outlinks"));

            ClientProtos.Scan proto = ProtobufUtil.toScan(scan);
            String scantoString = Base64.encodeBytes(proto.toByteArray());
            conf.set(TableInputFormat.SCAN, scantoString);

            JavaPairRDD<ImmutableBytesWritable, Result> my =
                    sc.newAPIHadoopRDD(conf, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);

            JavaPairRDD<String, Iterable<String>> links = my.mapToPair(
                    new PairFunction<Tuple2<ImmutableBytesWritable, Result>, String, Iterable<String>>() {
                        @Override
                        public Tuple2<String, Iterable<String>> call(Tuple2<ImmutableBytesWritable, Result> immutableBytesWritableResultTuple2) throws Exception {
                            Result result = immutableBytesWritableResultTuple2._2();
                            String url = Bytes.toString(result.getRow());
                            Cell[] cells = result.rawCells();
                            List<String> urls = new ArrayList<>();
                            for(Cell cell : cells){
                                int qua = cell.getQualifierLength();
                                int family = cell.getFamilyLength();
                                int row = cell.getRowLength();
                                urls.add(cell.toString().substring(row+family+2,row+family+2+qua));
                            }
                            return new Tuple2<>(url,urls);
                        }
                    }
            ).cache();

            JavaPairRDD<String,Double> ranks = links.mapValues(p->1.0);

            for(int i=0;i<1;i++){
                JavaPairRDD<String,Double> contribute = links.join(ranks).flatMapToPair(
                        new PairFlatMapFunction<Tuple2<String, Tuple2<Iterable<String>, Double>>, String, Double>() {
                            @Override
                            public Iterator<Tuple2<String, Double>> call(Tuple2<String, Tuple2<Iterable<String>, Double>> stringTuple2Tuple2) throws Exception {
                                List<Tuple2<String,Double>> result = new ArrayList<>();
                                Iterable<String> urls = stringTuple2Tuple2._2()._1();
                                Double score = stringTuple2Tuple2._2()._2()/ Iterables.size(urls);
                                urls.forEach(url->result.add(new Tuple2<>(url,score)));
                                return result.iterator();
                            }
                        }
                );
                ranks = contribute.reduceByKey((a,b)->a+b).mapValues(sum->sum*0.85+0.15);
            }

            ranks.foreach(p->System.out.println(p._1()));
            HTable table = new HTable(conf,"webpage");
            for(Tuple2<String,Double> tuple : ranks.collect()){
                Put put = new Put(tuple._1().getBytes());
                put.add("common".getBytes(),"pagerank".getBytes(),Bytes.toBytes(tuple._2()));
                table.put(put);
            }

            table.close();
            return;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
