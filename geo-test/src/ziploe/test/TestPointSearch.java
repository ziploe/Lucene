package ziploe.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.spatial.geopoint.document.GeoPointField;
import org.apache.lucene.spatial.geopoint.search.GeoPointDistanceQuery;
import org.apache.lucene.spatial.geopoint.search.GeoPointInBBoxQuery;
import org.apache.lucene.spatial.geopoint.search.GeoPointInPolygonQuery;
import org.apache.lucene.spatial.util.GeoEncodingUtils;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class TestPointSearch {
	//private String indexPath = "C:/Users/Administrator/Desktop/index/index9";
	
	//private String indexPath = "E:\\data\\index\\index1";//	10亿数据 ID： 0~100000 坐标随机
	private String indexPath = "E:\\data\\index\\index14";//1000000数据 
	//private String dataPath = "E:\\data\\data\\geo.txt";	//10亿数据 ID： 0~100000 坐标随机
	//private String dataPath = "E:\\data\\data\\10millionWithoutTime1.txt";	
	private String dataPath="D:\\data\\10yiWithTime.txt";	//10亿数据 time id lon lat

	void index(){
		IndexWriter writer = null;
		try {
			//1.Create Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//Analyzer analyzer = new MyEnglishAnalyzer();
			//Analyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig iwc = new IndexWriterConfig(null);
			/********/
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			//iwc.setCodec(new SimpleTextCodec());

			writer =new IndexWriter(directory,iwc );
			//System.out.println(writer.getConfig());

			//3.Create Document
			Document doc =null;
			//indexDocs(writer, docDir);

			//4.add field for Document
			File f = new File(dataPath);
			InputStreamReader reader = new InputStreamReader( new FileInputStream(dataPath));
			BufferedReader br = new BufferedReader(reader); 
			String line = "";  	
			int id, num=0;
			float x,y;
			//while ((line=br.readLine())!=null) { 
			while (num<1000000) { 
				
				String[] dataArr=line.split("\t");
				id=Integer.parseInt(dataArr[0]);
				x=Float.parseFloat(dataArr[1]);
				y=Float.parseFloat(dataArr[2]);
				if(num%1000000==0){
					System.out.println(num);
				}
				//System.out.println(id+"\t"+x+"\t"+y);
				doc=new Document();
				//doc.add(new StringField("id", dataArr[0], Store.NO));
				doc.add(new IntField("id", id, Store.NO));
				doc.add(new GeoPointField("location",x,y, GeoPointField.PREFIX_TYPE_NOT_STORED));	
				//5.add Document to index by IndexWriter
				writer.addDocument(doc);
				num++;
			}
			br.close();

		} catch (IOException e) {	
			e.printStackTrace();
		}finally {
			try {
				if(writer!=null) writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void searchByPolygon(String field, double[] polyLons, double[] polyLats){
		try {
			//String indexPath = "C:/Users/Administrator/Desktop/index/index9";
			String indexPath = "E:\\data\\index\\index1";	//10亿数据 ID 0~100000 位置随机
			//String indexPath = "C:/Users/Administrator/Desktop/index/text3";
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
		
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
	
			Query query1 = new GeoPointInPolygonQuery(field, polyLons, polyLats);
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(query1,BooleanClause.Occur.FILTER);
			TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				long lo=Long.parseLong(doc.get("location"));
				
				System.out.println("Id:"+doc.get("id")+"\tlocation:"+doc.get("location")
				+"\t"+GeoEncodingUtils.mortonUnhashLon(lo)
				+"\t"+GeoEncodingUtils.mortonUnhashLat(lo));
				
				//GeoEncodingUtils.mortonUnhashLon(lo);
			}
			
			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void searchByBBox(String field, double minLon, double minLat, double maxLon, double maxLat){
		try {
			//String indexPath = "C:/Users/Administrator/Desktop/index/index9";
			String indexPath = "E:\\data\\index\\index1";	//10亿数据 ID 0~100000 位置随机
			//String indexPath = "C:/Users/Administrator/Desktop/index/text3";
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
		
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
			Query query1= new GeoPointInBBoxQuery(field, minLon, minLat, maxLon, maxLat);
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(query1,BooleanClause.Occur.FILTER);
			TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				long lo=Long.parseLong(doc.get("location"));
				
				System.out.println("Id:"+doc.get("id")+"\tlocation:"+doc.get("location")
				+"\t"+GeoEncodingUtils.mortonUnhashLon(lo)
				+"\t"+GeoEncodingUtils.mortonUnhashLat(lo));
				
				//GeoEncodingUtils.mortonUnhashLon(lo);
			}
			
			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	public void searchByCircle(String field, double centerLon, double centerLat, double radiusMeters){
		try {
			String indexPath = "C:/Users/Administrator/Desktop/index/index10";
			//String indexPath = "E:\\data\\index\\index1";	//10亿数据 ID 0~100000 位置随机
			//String indexPath = "C:/Users/Administrator/Desktop/index/text3";
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
		
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
			Query query1= new GeoPointDistanceQuery(field, centerLon, centerLat, radiusMeters);
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(query1,BooleanClause.Occur.FILTER);
			TopScoreDocCollector collector = TopScoreDocCollector.create(10);
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				long lo=Long.parseLong(doc.get("location"));
				
				System.out.println("Id:"+doc.get("id")+"\tlocation:"+doc.get("location")
				+"\t"+GeoEncodingUtils.mortonUnhashLon(lo)
				+"\t"+GeoEncodingUtils.mortonUnhashLat(lo));
				
				//GeoEncodingUtils.mortonUnhashLon(lo);
			}
			
			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void searchById(String id){
		String field = "id";
		try {
			//String indexPath = "C:/Users/Administrator/Desktop/index/index9";
			String indexPath = "E:\\data\\index\\index1";
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
		
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
			Query query1= new TermQuery(new Term(field,id));
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(query1,BooleanClause.Occur.FILTER);
			TopScoreDocCollector collector = TopScoreDocCollector.create(100);
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				long lo=Long.parseLong(doc.get("location"));
				
				System.out.println("Id:"+doc.get("id")+"\tlocation:"+doc.get("location")
				+"\t"+GeoEncodingUtils.mortonUnhashLon(lo)
				+"\t"+GeoEncodingUtils.mortonUnhashLat(lo));
				
				//GeoEncodingUtils.mortonUnhashLon(lo);
			}
			
			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public void searchByIdRange( Integer min, Integer max, boolean minInclusive, boolean maxInclusive){
		String field = "id";
		try {
			//String indexPath = "C:/Users/Administrator/Desktop/index/index11";
			String indexPath = "E:\\data\\index\\index1";
			//String indexPath = "C:/Users/Administrator/Desktop/index/text3";
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
		
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
			Query query1= NumericRangeQuery.newIntRange(field, min, max, minInclusive, maxInclusive);

			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(query1,BooleanClause.Occur.FILTER);
			TopScoreDocCollector collector = TopScoreDocCollector.create(100);
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				
				
				//System.out.println("Id:"+doc.get("id"));
				long lo=Long.parseLong(doc.get("location"));
				
				System.out.println("Id:"+doc.get("id")+"\tlocation:"+doc.get("location")
				+"\t"+GeoEncodingUtils.mortonUnhashLon(lo)
				+"\t"+GeoEncodingUtils.mortonUnhashLat(lo));
				
				
				//GeoEncodingUtils.mortonUnhashLon(lo);
			}
			
			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	
	public static void main(String[] args){
		Date start = new Date();
		System.out.println("program start" );
		TestPointSearch tps =new TestPointSearch();
		double[] polyLons ={100.00,106.01,107.9,105.01,100.0,100.0};
		double[] polyLats={10.0,10.0,17.9,19.01,19.01,10.0};
		String field = "location";
		//tps.index();
		//tps.searchByPolygon(field, polyLons, polyLats);
		//tps.searchByBBox(field, 100.0, 10.0, 105, 19.0);
		tps.searchByCircle(field, 102.0, 12.01, 111000);
		//tps.searchById("10001");
		//tps.searchByIdRange(10009, 10009, true, true);
		Date end = new Date();
		long timeCost = end.getTime()-start.getTime();
		System.err.println(timeCost+" ms cost to do so" );

	}

}
