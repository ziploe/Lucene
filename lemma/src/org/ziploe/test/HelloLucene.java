package org.ziploe.test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.ziploe.lemma.MyEnglishAnalyzer;

public class HelloLucene {

	
	public void index(){
		//test git
		String indexPath = "C:/Users/Administrator/Desktop/index/index7";
		String docsPath = "C:/Users/Administrator/Desktop/text1";
		//Directory directory = new RAMDirectory();
		IndexWriter writer = null;
		
		//Path docDir = Paths.get(docsPath);
		try {
			//1.Create Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new MyEnglishAnalyzer();
			//Analyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			/********/
			iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwc.setCodec(new SimpleTextCodec());
			
			LogMergePolicy mergePolicy = new LogDocMergePolicy();
			mergePolicy.setMergeFactor(10);
			mergePolicy.setMaxMergeDocs(10);
			iwc.setMaxBufferedDocs(10);
			/******/
			//iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			//2.Create IndexWriter
			//IndexWriter writer = null;
			writer =new IndexWriter(directory,iwc );
			
			//3.Create Document
			Document doc =null;
			//indexDocs(writer, docDir);

			//4.add field for Document
			File f = new File(docsPath);
			for(File file:f.listFiles()) {
				doc=new Document();
				/********************************************/
				
			//	FileReader r = new FileReader(file);
			//	TokenStream tokenStream=analyzer.tokenStream("content", r);
				
				/********************************************/
				
				
				doc.add(new MyTextField("contents", new FileReader(file)));
				doc.add(new StringField("path", file.toString(), Field.Store.YES));
				doc.add(new StringField("fileName",file.getName(),Field.Store.YES));
				
				//5.add Document to index by IndexWriter
				writer.addDocument(doc);
				//writer.commit();
				//新的版本对 Field 进行了更改，StringField 索引但是不分词、StoreField 至存储不索引、TextField 索引并分词
				
			}
			//writer.close();
			
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
	
	public void searcher(String word){
		try {
			String indexPath = "C:/Users/Administrator/Desktop/index/index7";
			//String indexPath = "C:/Users/Administrator/Desktop/index/text3";
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
			System.out.println("totalTerm-one:"+reader.docFreq(new Term("contents",word))+"\nDocCount: "+reader.getDocCount("path"));
			System.out.println("totalTerm:"+reader.totalTermFreq(new Term("contents",word))+"\nSumDocFreq: "+reader.getSumDocFreq("path"));
			System.out.println("getContext:"+reader.getContext());
			/***********************************/
			Document doc = reader.document(4);         
		 //   System.out.println("Processing file: "+doc.get("fileName"));
			
			/*************************************/
			
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
			QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
			Query query = parser.parse(word);
			
			//5.根据searcher搜索并返回TopDocs
			TopDocs tds = searcher.search(query, 10);
			System.out.println("totalHits:\t"+tds.totalHits);
			//6.根据TopDocs获取ScoreDoc对象
			ScoreDoc[] sds =tds.scoreDocs;
			for(ScoreDoc sd:sds){
				System.out.println("----------------------------");
			//7.根据searcher和ScoreDoc对象获取Document对象
				Document d = searcher.doc(sd.doc);
				//System.out.println(searcher.explain(query, sd.doc));
				System.out.println("dddddd:"+searcher.doc(sd.doc).get("contents"));
				System.out.println("score: "+sd.score+"\tdocID:"+sd.doc);
				System.out.println("scoreDoc: "+sd.toString());
			//8.根据Document对象获取需要的值
				System.out.println(d.get("fileName")+"\t["+d.get("path")+"]"+"\t["+d.toString()+"]");
				System.out.println("getFields: "+d.getFields());
			}

			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void searcher1(String queryString){
		try {
			Analyzer analyzer = new StandardAnalyzer();
			Query query = null;
			String indexPath = "C:/Users/Administrator/Desktop/index/index1";
			String[] fields = {"path","fileName","contents"};
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(10);

			try { 
				//QueryParser qp = new QueryParser( "addr", analyzer); 
				MultiFieldQueryParser mqp=new MultiFieldQueryParser(fields, analyzer);
				//query = qp.parse(queryString); 
				query = mqp.parse(queryString); 
			} catch (ParseException e) { 
			} 

			searcher.search(query, collector); 
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			
			//4.创建搜索的Query
			QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
			Query query1 = parser.parse(queryString);
			//5.根据searcher搜索并返回TopDocs
			TopDocs tds = searcher.search(query1, 10);
			//6.根据TopDocs获取ScoreDoc对象
			ScoreDoc[] sds =tds.scoreDocs;
			for(ScoreDoc sd:sds){
			//7.根据searcher和ScoreDoc对象获取Document对象
				Document d = searcher.doc(sd.doc);
				System.out.println("score: "+sd.score);
			//8.根据Document对象获取需要的值
				System.out.println(d.get("fileName")+"\t["+d.get("path")+"]");
			}

			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

}
