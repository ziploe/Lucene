package org.ziploe.test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsQuery;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.ziploe.lemma.MyEnglishAnalyzer;

/**
 * Lucene 高级应用
 * 
 * @author 李 国涛（软件工程师, IBM）  原创者
 * @author 蒲路  修改者
 *@see http://www.ibm.com/developerworks/cn/java/j-lo-Lucene/index.html
 *{@link http://www.ibm.com/developerworks/cn/java/j-lo-Lucene/index.html}
 */
public class SearchUtil {
	private Directory directory = null;
	private String indexPath = "C:/Users/Administrator/Desktop/index/index0";

	public IndexSearcher getSearcher() {
		IndexReader reader = null;
		try {
			System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
			directory = FSDirectory.open(Paths.get(indexPath));
			reader = DirectoryReader.open(directory);

			/**********changed*********************/
			/*System.out.println("getContext:"+reader.getContext());
			System.out.println("docFreq:"+reader.docFreq(new Term("name","abc")));
			System.out.println("getDocCount:"+reader.getDocCount("name"));
			System.out.println("getSumDocFreq:"+reader.getSumDocFreq("name"));
			System.out.println("getSumTotalTermFreq (name):"+reader.getSumTotalTermFreq("name"));
			System.out.println("ghashCode:"+reader.hashCode());
			System.out.println("maxDoc:"+reader.maxDoc());
			System.out.println("numDocs:"+reader.numDocs());
			System.out.println("document:"+reader.document(1));
			System.out.println("getCoreCacheKey:"+reader.getCoreCacheKey());
			System.out.println("getTermVector(1,name):"+reader.getTermVector(1, "name"));
			System.out.println("tryIncRef:"+reader.tryIncRef());


			System.out.println("------------------------------------------");*/
			/****************************************/
			IndexSearcher searcher = new IndexSearcher(reader);
			return searcher;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//说明：TermQuery 是 Lucene 查询中最基本的一种查询，它只能针对一个字段进行查询。
	public void searchByTerm(String field, String name, int num) {

		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByTerm:");
			Query query = new TermQuery(new Term(field, name));
			System.out.println("Query-->  "+query.toString());
			/***********changed***********/
			//	System.out.println(query.getBoost()+":"+query.toString());


			/***********************/
			TopDocs tds = searcher.search(query, num);
			System.out.println("count:" + tds.totalHits);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//说明：TermRangeQuery query=new TermRangeQuery(字段名, 起始值, 终止值, 起始值是否包含边界, 终止值是否包含边界)
	//感觉并没有多少用 可以被TermRangeQuery代替？
	public void searchByTermRange(String field,String start,String end,int num) {
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByTermRange:");
			BytesRef lowerTerm = new BytesRef(start);
			BytesRef upperTerm = new BytesRef(end);
			Query query = new TermRangeQuery(field,lowerTerm,upperTerm,true, true);
			System.out.println("Query-->  "+query.toString());
			/********changed************/
			System.out.println(query.toString());

			/********************/

			TopDocs tds = searcher.search(query, num);
			System.out.println("count:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//说明：前缀查询, 搜索匹配开始位置的数据类似百度的输入框。
	public void searchByPrefix(String field, String value, int num) {
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByPrefix:");
			Query query = new PrefixQuery(new Term(field, value));
			System.out.println("Query-->  "+query.toString());
			TopDocs tds = searcher.search(query, num);
			System.out.println("count:" + tds.totalHits);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//WildcardQuery 通配符查询  通配符分为两种，“*”和“？”，“*”表示任何字符，“？”表示任意一个字符。
	public void searchByWildcard(String field, String value, int num) {
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByWildcard:");
			Query query = new WildcardQuery(new Term(field, value));
			System.out.println("Query-->  "+query.toString());
			TopDocs tds = searcher.search(query, num);
			System.out.println("count" + tds.totalHits);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("content"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * FuzzyQuery 模糊搜索
	 * FuzzyQuery(new Term("name","acc"),1,1)，需要 3 个参数，
	 * 第一个参数是词条对象，第二个参数是 levenshtein 算法的最小相似度，
	 * 第三个参数是指与多少个前缀字符匹配。
	 * @param num
	 */
	public void searchByFuzzy(int num) {
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByFuzzy:");
			FuzzyQuery query = new FuzzyQuery(new Term("name","acc"),1,1);
			System.out.println("Query-->  "+query.toString());
			//System.out.println(query.getPrefixLength());
			TopDocs tds = searcher.search(query, num);
			System.out.println("count:"+tds.totalHits);
			for(ScoreDoc sd:tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void searchByBoolean(int num) {
		try {

			//int hits = 0;
			IndexSearcher searcher = getSearcher();
			System.out.println("searchByBoolean:");
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(new TermQuery(new Term("content", "abc")),BooleanClause.Occur.SHOULD);
			bq1.add(new TermQuery(new Term("addr", "abc")),BooleanClause.Occur.SHOULD);
			//bq1.add(new TermQuery(new Term("email","lucy@us.ibm.com")), BooleanClause.Occur.SHOULD);
			//TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
			TopScoreDocCollector collector = TopScoreDocCollector.create(num);
			/*************
			Query q2 = new ConstantScoreQuery(bq1.build());
			searcher.search(q2, collector);
			System.out.println("Query-->  "+q2.toString());
			/************/
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			//searcher.search(bq1, 5);
			//TopDocs tds = searcher.search(bq1, num);
			//hits = collector.topDocs().scoreDocs.length;

			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("######################");
				System.out.println("socre:"+sd.score);	
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
				System.out.println("addr:"+doc.get("addr"));

			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void searchByBooleanSep1(int num) {
		try {

			//int hits = 0;
			IndexSearcher searcher = getSearcher();
			System.out.println("searchByBoolean:");
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(new TermQuery(new Term("name", "abc")),BooleanClause.Occur.SHOULD);
			//bq1.add(new TermQuery(new Term("addr", "abc")),BooleanClause.Occur.SHOULD);
			//bq1.add(new TermQuery(new Term("email","lucy@us.ibm.com")), BooleanClause.Occur.SHOULD);
			//TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
			TopScoreDocCollector collector = TopScoreDocCollector.create(num);
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			//searcher.search(bq1, 5);
			//TopDocs tds = searcher.search(bq1, num);
			//hits = collector.topDocs().scoreDocs.length;

			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("######################");
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
				System.out.println("addr:"+doc.get("addr"));

			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void searchByBooleanSep2(int num) {
		try {

			//int hits = 0;
			IndexSearcher searcher = getSearcher();
			System.out.println("searchByBoolean:");
			BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			//bq1.add(new TermQuery(new Term("name", "abc")),BooleanClause.Occur.SHOULD);
			bq1.add(new TermQuery(new Term("addr", "abc")),BooleanClause.Occur.SHOULD);
			//bq1.add(new TermQuery(new Term("email","lucy@us.ibm.com")), BooleanClause.Occur.SHOULD);
			//TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
			TopScoreDocCollector collector = TopScoreDocCollector.create(num);
			searcher.search(bq1.build(), collector);
			System.out.println("Query-->  "+bq1.build().toString());
			//searcher.search(bq1, 5);
			//TopDocs tds = searcher.search(bq1, num);
			//hits = collector.topDocs().scoreDocs.length;

			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("######################");
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
				System.out.println("addr:"+doc.get("addr"));

			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void searchByPhrase(int num){
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByPhrase:");
			PhraseQuery.Builder builder = new PhraseQuery.Builder();
			builder.add(new Term("content", "welcome"),2); //这里输入两个单词的相对位置就好 
			//相邻相差1, 隔一个相差2 (2,4)和（0,2）没有差别
			builder.add(new Term("content", "lucene"),4); //注意！输入大写的Lucene并不能搜到
			PhraseQuery pq = builder.build();
			System.out.println("Query-->  "+pq.toString());
			TopScoreDocCollector collector = TopScoreDocCollector.create(num);	 
			searcher.search(pq, collector);
			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("content:"+doc.get("content"));
				System.out.println("date:"+doc.get("date"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void searchByPhrase2(int num){
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByPhrase:");
			//PhraseQuery.Builder builder = new PhraseQuery.Builder();
			Query pq=new PhraseQuery(3, "content", "welcome","lucene");
			System.out.println("Query-->  "+pq.toString());
			TopScoreDocCollector collector = TopScoreDocCollector.create(num);	 
			searcher.search(pq, collector);
			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("content:"+doc.get("content"));
				System.out.println("date:"+doc.get("date"));
				System.out.println("getFields:"+doc.getFields());

			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}


	public void searchByQueryParse(String words){
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchByQueryParse:");
			QueryParser parser = new QueryParser("content",new MyEnglishAnalyzer()); //默认搜索域
			Query query = parser.parse(words);
			//query = parser.parse("name:ert");  可以这样改变搜索域
			System.out.println("Query-->  "+query.toString());
			TopDocs tds = searcher.search(query, 5);
			System.out.println("count:" + tds.totalHits);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("content"));
				System.out.println("score: "+sd.score);
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("content:"+doc.get("content"));
				System.out.println("date:"+doc.get("date"));
				//Thread.sleep(2000);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}


	public void byQueryParser(String queryString,String indexUrl) throws IOException, ParseException, InvalidTokenOffsetsException { 
		/**/ /* 这里放索引文件的位置 */ 
		//String[] fields = {"name","addr","email"};
		String[] fields = {"path","fileName","contents"};

		Path path = Paths.get(indexUrl);
		Directory indexDir = FSDirectory.open(path); 


		org.apache.lucene.search.Query query = null; 
		IndexReader reader = DirectoryReader.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(reader); 

		TopScoreDocCollector collector = TopScoreDocCollector.create(10);
		//Analyzer analyzer = new StandardAnalyzer(); 
		Analyzer analyzer = new MyEnglishAnalyzer();

		//	QueryScorer queryScorer = new QueryScorer(query);//如果有需要，可以传入评分
		//设置高亮标签



		try { 
			//QueryParser qp = new QueryParser( "addr", analyzer); 
			MultiFieldQueryParser mqp=new MultiFieldQueryParser(fields, analyzer);
			//query = qp.parse(queryString); 
			query = mqp.parse(queryString); 
		} catch (ParseException e) { 
		} 

		searcher.search(query, collector); 
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		//List<String> rstList = new ArrayList<String>();//结果列表（已高亮处理）

		System.out.println("Found " + hits.length + " hits.");
		for(int i=0;i<hits.length;i++) {

			int docId = hits[i].doc;
			Document d = searcher.doc(docId);

			String rst = "";

			String title = d.get("path");
			String content = d.get("fileName");



			rst = d.get("contents")+"\t"+title+"\t"+content;
			//rstList.add(rst);
			//System.out.println((i + 1) +". " + d.get("url") +" "+title+"\t"+content);
			System.out.println((i + 1) +". " + rst);

		}
	}
	
	
	public void searchByTermsQuery(int num) {
		try {

			//int hits = 0;
			IndexSearcher searcher = getSearcher();
			System.out.println("searchByTermsQuery:");
			Query q1 = new TermsQuery(new Term("content", "lucene"), new Term("content", "china"));
			/*BooleanQuery.Builder bq1 = new BooleanQuery.Builder();
			bq1.add(new TermQuery(new Term("name", "abc")),BooleanClause.Occur.SHOULD);
			bq1.add(new TermQuery(new Term("addr", "abc")),BooleanClause.Occur.SHOULD);*/
			//bq1.add(new TermQuery(new Term("email","lucy@us.ibm.com")), BooleanClause.Occur.SHOULD);
			//TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
			TopScoreDocCollector collector = TopScoreDocCollector.create(num);
			searcher.search(q1, collector);
			System.out.println("Query-->  "+q1.toString());
			//searcher.search(bq1, 5);
			//TopDocs tds = searcher.search(bq1, num);
			//hits = collector.topDocs().scoreDocs.length;

			System.out.println("count:" + collector.getTotalHits());
			for (ScoreDoc sd : collector.topDocs().scoreDocs) {
				
				
				Document doc = searcher.doc(sd.doc);
				System.out.println("######################");
				System.out.println("socre:"+sd.score);	
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
				System.out.println("addr:"+doc.get("addr"));
				
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void searchBySpanTermQuery(int num) {
		try {

			IndexSearcher searcher = getSearcher();
			System.out.println("searchBySpanTermQuery:");
			SpanTermQuery query = new SpanTermQuery(new Term("content", "abc"));
			System.out.println("Query-->  "+query.toString());
			TopDocs tds = searcher.search(query, num);
			System.out.println("count" + tds.totalHits);
			for (ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println("docId:"+doc.get("id"));
				System.out.println("name:"+doc.get("name"));
				System.out.println("email:"+doc.get("email"));
				System.out.println("date:"+doc.get("date"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void main(String args[]) throws Exception, ParseException, Throwable{
		int searchFre,times=10;
		//long[] time = new long[times];
		//for(searchFre=0;searchFre<times;searchFre++){
		Date start = new Date();
		SearchUtil searchutil = new SearchUtil();
		//searchutil.searchByTerm("name", "rock", 5);
		//searchutil.searchByTermRange("name", "a", "e", 5);
		//searchutil.searchByPrefix("name", "e", 5);
		searchutil.searchByWildcard("content", "valu?", 5);
		searchutil.searchByWildcard("content", "valu*", 5);
		//searchutil.searchByFuzzy(5);
		//searchutil.searchByBoolean(10);
		//searchutil.searchByBooleanSep1(5);
		//searchutil.searchByBooleanSep2(5);
		//searchutil.searchByPhrase(5);
		//searchutil.searchByPhrase2(5);
//		searchutil.searchByQueryParse("valu?"); 
		searchutil.searchByQueryParse("ip$127*");//默认域为content ,name:ert   这样可以改变搜索域
		//哇，暴露出一个问题，每次查询都要new一个analyzer，每new一个analyzer都要重新加载一次字典
		// 导致每次查询都会固定地多‘加载字典的时间’（180ms左右）

		//searchutil.byQueryParser("rustiness", "C:/Users/Administrator/Desktop/index/index1");
		//searchutil.searchByTermsQuery(5);
		//searchutil.searchBySpanTermQuery(5);
		

		Date searchEnd = new Date();
		long searchTime = searchEnd.getTime()-start.getTime();
		System.err.println(searchTime+" ms cost to search");
		//System.err.print(searchTime+" ");
		//time[searchFre]= searchTime;
		//}
		/*for(searchFre=0;searchFre<times;searchFre++){
			System.err.print(time[searchFre]+" ");
		}*/

	}



}
