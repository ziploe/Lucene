package geo.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.spatial.geopoint.search.GeoPointDistanceQuery;
import org.apache.lucene.spatial.geopoint.search.GeoPointInPolygonQuery;
import org.apache.lucene.spatial.util.GeoEncodingUtils;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class GeoSearch  {

	double centerLon=102.0; 
	double centerLat=12.01;
	double radiusMeters=9000;
	public void setCircle(double lon,double lat,double radiusMeters){
	this.centerLon=lon;
	this.centerLat=lat;
	this.radiusMeters=radiusMeters;
	}
	private int docPerPage=10;

	//public void searchByCircle(String field, double centerLon, double centerLat, double radiusMeters){
	//public static void main(String[] args){
	public  void search(){
		System.out.println("program started");
		Date start = new Date();
		String field= "location";
		double[] polyLons ={100.00,106.01,107.9,105.01,100.0,100.0};
		double[] polyLats={10.0,10.0,17.9,19.01,19.01,10.0};
		
		try {
			String indexPath = "C:/Users/Administrator/Desktop/index/index9";
			//String indexPath = "///home/ziploe/zip-geo/index";	//10亿数据 ID 0~100000 位置随机

			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);

			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
			Query query= new GeoPointDistanceQuery(field, centerLon, centerLat, radiusMeters); //search by circle
			//Query query1= NumericRangeQuery.newIntRange("id", 50, 100, true, true);		//search by id range
			//Query query2 = new GeoPointInPolygonQuery(field, polyLons, polyLats);		//search by polygon
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(query,BooleanClause.Occur.FILTER);
			//bq1.add(query1,BooleanClause.Occur.FILTER);
			//bq1.add(query2,BooleanClause.Occur.FILTER);
			TopScoreDocCollector collector = TopScoreDocCollector.create(1000000);
			searcher.search(bq1.build(), collector);
			

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String command="null";
			ScoreDoc[] sds=collector.topDocs().scoreDocs;
			ScoreDoc sd;
			int page=0,currentDoc,maxDoc=sds.length,
					maxPage=(int)Math.ceil((maxDoc/(float)docPerPage))-1;
			Date end = new Date();
			long timeCost = end.getTime()-start.getTime();
			System.out.println("Query-->  "+bq1.build().toString());
			System.out.println("Find Docs: " + collector.getTotalHits()+" in: "+maxPage+" pages");
			System.err.println(timeCost+" ms cost to search" );
			while(command.length()==0 || command.charAt(0)!='q')
			{
				if(command.length()==0){
					command = br.readLine();
					continue;
				}
				else if(command.charAt(0)=='j'){
					String[] cmd=command.split(" ");
					page=Integer.parseInt(cmd[1])>maxPage?maxPage:Integer.parseInt(cmd[1]);
				}
				else if(command.charAt(0)=='p'){
					page=(page-1)<0?0:(page-1);
				}
				else if(command.charAt(0)=='x'){
					page=(page+1)>maxPage?maxPage:(page+1);
				}
				else if(command.charAt(0)=='h'){
					help();
					command = br.readLine();
					continue;
				}
				else if(command.charAt(0)=='c'){
					String[] cmd=command.split(" ");
					setPerPageDocNum(Integer.parseInt(cmd[1]));
					maxPage=(int)Math.ceil((maxDoc/(float)docPerPage))-1;
					System.out.println("Find Docs: " + collector.getTotalHits()+" in: "+maxPage+" pages");
					command = "null";
					continue;
				}
				else{
					if(command!="null"){
						System.out.println("invalid operation");
						help();
						command = br.readLine();
						continue;}
				}
				int where=1;
				System.out.println("at page: "+page);
				currentDoc=page*docPerPage;
				for(;currentDoc<((1+page)*docPerPage>maxDoc?maxDoc:(1+page)*docPerPage);currentDoc++){
					sd=sds[currentDoc];
					Document doc = searcher.doc(sd.doc);
					long lo=Long.parseLong(doc.get("location"));

					System.out.println(where+"	time:"+doc.get("time")+"	Id:"+doc.get("id")
					+"\t"+GeoEncodingUtils.mortonUnhashLon(lo)
					+"\t"+GeoEncodingUtils.mortonUnhashLat(lo));
					where++;
				}



				System.out.println("what's your command, master! type h for help");
				command = br.readLine();
				//System.out.println(command.toString());
			}
			br.close();
			System.out.println("program ends");
			//9.关闭reader
			reader.close();


		} catch (IOException e) {
			e.printStackTrace();
		} 

	}



	public void help(){
		System.out.println("h|help	:help menu\n"+
				"q	:exit the program\n"+
				"p	:privious page\n"+
				"x	:next page\n"+
				"j [num]	:jump to the num page, eg: j 4,jump to page 4\n"+
				"c [num]	:change perPage doc number to num"
				); 
		System.out.println("Your wish is my command, master!");

	}
	
	public void setPerPageDocNum(int num){
		this.docPerPage=num;
		System.out.println("PerPage doc number set to: "+num);
	}


	public static void main(String[] args){
		GeoSearch geoSearch = new GeoSearch();	
		long lon =Long.parseLong(args[0]);
		long lat =Long.parseLong(args[1]);
		long distance =Long.parseLong(args[0]);
		geoSearch.setCircle(lon, lat, distance);
		geoSearch.search();

	}


}
