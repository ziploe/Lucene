package org.ziploe.test;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
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

public class TestScore {

	Directory d;
	Analyzer analyzer;
	/*public TestScore() throws IOException{
		String indexPath = "C:/Users/Administrator/Desktop/text3";
		Directory d = FSDirectory.open(Paths.get(indexPath));
		//d=new FSDirectory(new File("D:/lucene_test"));
		analyzer=new WhitespaceAnalyzer();
	}*/
	public void index() throws IOException{
		String indexPath = "C:/Users/Administrator/Desktop/text3";
		Directory d = FSDirectory.open(Paths.get(indexPath));
		//d=new FSDirectory(new File("D:/lucene_test"));
		analyzer=new WhitespaceAnalyzer();
		IndexWriterConfig conf=new IndexWriterConfig(analyzer);
		IndexWriter iw=new IndexWriter(d, conf);
		Document doc=new Document();
		/*doc=new Document();
		doc.add(new TextField("content", "common common common common",Store.YES));
		iw.addDocument(doc);*/
		doc=new Document();
		doc.add(new TextField("content", "common common common term",Store.YES));
		iw.addDocument(doc);
		doc=new Document();
		doc.add(new TextField("content", "common common term term",Store.YES));
		iw.addDocument(doc);
		doc=new Document();
		doc.add(new TextField("content", "common term term term",Store.YES));
		iw.addDocument(doc);
		doc=new Document();
		doc.add(new TextField("content", "term term term term",Store.YES));
		iw.addDocument(doc);
		//iw.commit();
		iw.close();
		System.out.println("index ends");
	}
	public void search() throws IOException, ParseException{
		String indexPath = "C:/Users/Administrator/Desktop/text3";
		Directory d = FSDirectory.open(Paths.get(indexPath));
		IndexReader r=DirectoryReader.open(d);
		IndexSearcher is=new IndexSearcher(r);
		//System.out.println("here");
		//   TermQuery query=new TermQuery(new Term("content", "common"));
		Query query=new QueryParser( "content", new WhitespaceAnalyzer()).parse("common term");
		TopDocs td=is.search(query, 10);
		ScoreDoc[] hits=td.scoreDocs;
		System.out.println("hits "+hits.length+" docs!");
		Document doc;
		for (int i = 0; i < hits.length; i++) {
			doc=is.doc(hits[i].doc);
			System.out.println(hits[i].score);
			System.out.println(doc.get("content"));
		}
		r.close();
	}


}
