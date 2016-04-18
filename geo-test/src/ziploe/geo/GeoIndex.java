package ziploe.geo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.spatial.geopoint.document.GeoPointField;
import org.apache.lucene.spatial.geopoint.search.GeoPointDistanceQuery;
import org.apache.lucene.spatial.geopoint.search.GeoPointInBBoxQuery;
import org.apache.lucene.spatial.geopoint.search.GeoPointInPolygonQuery;
import org.apache.lucene.spatial.util.GeoEncodingUtils;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class GeoIndex {

	//private String indexPath = "///home/ziploe/zip-geo/index";//5000000数据 
	private String indexPath = "E:\\data\\index\\index16";//5000000数据 

	private String dataPath = "D:\\data\\10yiWithTime.txt";	//10亿数据 time id lon lat
	//private String dataPath = "///home/ziploe/zip-geo/data/data0";	
	public void setDataPath(String dataPath){
		this.dataPath=dataPath;
	}
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
			//File f = new File(dataPath);
			InputStreamReader reader = new InputStreamReader( new FileInputStream(dataPath));
			BufferedReader br = new BufferedReader(reader); 
			String line = "";  	
			int id, time,num=0;
			float x,y;
			while ((line=br.readLine())!=null) { 
			//while (num<100000) { line=br.readLine();
				
				String[] dataArr=line.split("\t");
				time=Integer.parseInt(dataArr[0]);
				id=Integer.parseInt(dataArr[1]);
				x=Float.parseFloat(dataArr[2]);
				y=Float.parseFloat(dataArr[3]);
				if(num%1000000==0){
					System.out.println(num);
				}
				//System.out.println(id+"\t"+x+"\t"+y);
				doc=new Document();
				doc.add(new IntField("time",time , Store.YES));
				doc.add(new IntField("id", id, Store.YES));
				doc.add(new GeoPointField("location",x,y, GeoPointField.PREFIX_TYPE_STORED));	
				//5.add Document to index by IndexWriter
				writer.addDocument(doc);
				num++;
				if(num>100000){
					break;
				}
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
	




	public static void main(String[] args){
		Date start = new Date();
		System.out.println("program start" );
		GeoIndex tps =new GeoIndex();
		if(args.length>0){
			tps.setDataPath(args[0]);
		}
	
		tps.index();
		Date end = new Date();
		long timeCost = end.getTime()-start.getTime();
		System.err.println(timeCost+" ms cost to do so" );

	}

}
