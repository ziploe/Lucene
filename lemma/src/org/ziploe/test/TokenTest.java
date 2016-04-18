package org.ziploe.test;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
/*
public class TokenTest {

	
	public void index(){
		
		String indexPath = "C:/Users/Administrator/Desktop/text3";
		String docsPath = "C:/Users/Administrator/Desktop/text1";
		//Directory directory = new RAMDirectory();
		final Path docDir = Paths.get(docsPath);
		
		//Path docDir = Paths.get(docsPath);
		try {
			//1.Create Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//Analyzer analyzer = new MyEnglishAnalyzer();
			Analyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			//iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			//2.Create IndexWriter
			IndexWriter writer = null;
			writer =new IndexWriter(directory,iwc );
			//3.Create Document
			Document doc =null;
			//indexDocs(writer, docDir);
			
			//4.add field for Document
		
			
			File f = new File(docsPath);
			for(File file:f.listFiles()) {
				Path file1 = Paths.get(file);
				InputStream stream = Files.newInputStream(file);
				doc=new Document();
				/********************************************
				
				FileReader r = new FileReader(file);
				TokenStream tokenStream=analyzer.tokenStream("content", r);*/
				
				/*WhitespaceTokenizer tokenizer =new WhitespaceTokenizer(new StringReader(testinput));
				WhitespaceTokenizer wst = new WhitespaceTokenizer(factory)
				TokenStream ts = factory.create(tokenizer);
				//ts =new LowerCaseFilter(ver,tokenizer);
				//ts =new StopFilter(ver, ts,
				//StopAnalyzer.ENGLISH_STOP_WORDS_SET);*/
				
				
				/*private static void displayTokens(TokenStream ts) throws IOException {
					CharTermAttribute termAttr = ts.addAttribute(CharTermAttribute.class);
					ts.reset();
					while(ts.incrementToken()) {
						String token = termAttr.toString();
						System.out.print("[" + token + "] ");
					}
					System.out.println();
					ts.end();
					ts.close();
				}*/
				
				/********************************************
				
				
				doc.add(new TextField("contents", new FileReader(file)));
				doc.add(new StringField("path", file.toString(), Field.Store.YES));
				doc.add(new StringField("fileName",file.getName(),Field.Store.YES));
				
				//5.add Document to index by IndexWriter
				writer.addDocument(doc);
				
			}
			writer.close();
			
		} catch (IOException e) {	
			e.printStackTrace();
		}
		
	}
	
	public void searcher(){
		try {
			String indexPath = "C:/Users/Administrator/Desktop/text3";
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(indexPath));
			//2.create IndexReader
			IndexReader reader = DirectoryReader.open(directory);
		
			//3.根据IndexReader 创建IndexSearcher
			IndexSearcher searcher =new IndexSearcher(reader);
			//4.创建搜索的Query
			QueryParser parser = new QueryParser("contents", new StandardAnalyzer());
			Query query = parser.parse("ziploe");
			//5.根据searcher搜索并返回TopDocs
			TopDocs tds = searcher.search(query, 10);
			//6.根据TopDocs获取ScoreDoc对象
			ScoreDoc[] sds =tds.scoreDocs;
			for(ScoreDoc sd:sds){
			//7.根据searcher和ScoreDoc对象获取Document对象
				Document d = searcher.doc(sd.doc);
			//8.根据Document对象获取需要的值
				System.out.println(d.get("fileName")+"\t["+d.get("path")+"]");
			}

			//9.关闭reader
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
		
	}

}
*/