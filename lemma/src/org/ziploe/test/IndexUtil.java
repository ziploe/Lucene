package org.ziploe.test;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.LogDocMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.ziploe.lemma.MyEnglishAnalyzer;

/**
 * Lucene 高级应用
 * 
 * @author 李 国涛, 软件工程师, IBM
 * @author 蒲路  修改者
 * @see http://www.ibm.com/developerworks/cn/java/j-lo-Lucene/index.html
 * {@link http://www.ibm.com/developerworks/cn/java/j-lo-Lucene/index.html}
 *
 */

public class IndexUtil {

	private String indexPath = "C:/Users/Administrator/Desktop/index/index0";

	private String[] idArr = {"1","2","3","4","5","6"};
	private String[] emailArr = {"abc@us.ibm.com","ert@cn.ibm.com","lucy@us.ibm.com",
			"rock@cn.ibm.com","test@126.com","deploy@163.com"};
	private String[] contentArr = {
			"welcome to baidu qiandu wandu hello hiiiiq Lucene,I am abc","This is ert,I am from China",
			"I'm Lucy,I am english","I work in IBM",
			"I am a tester","I like Lucene in action"
	};
	//"welcome to Lucene hello baidu youtube jack dkd kjkj dfes sdf ddds,I am abc","This is ert,I am from China",

	private String[] nameArr = {"abc","ert","lucy","rock","test","deploy"};
	private String[] addrArr = {"abcd","abc","lucyy","rocky","testy","deployy"};
	private Directory directory = null;
	

	public void index() {
		IndexWriter writer = null;
		Analyzer analyzer=null;
		//analyzer= new MyEnglishAnalyzer();
		analyzer= new StandardAnalyzer();
		try {
			
			directory = FSDirectory.open(Paths.get(indexPath));
			IndexWriterConfig conf = new IndexWriterConfig(analyzer);
			conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
			conf.setCodec(new SimpleTextCodec());
			LogMergePolicy mergePolicy = new LogDocMergePolicy();
			mergePolicy.setMergeFactor(10);
			mergePolicy.setMaxMergeDocs(10);
			conf.setMaxBufferedDocs(10);
			writer = new IndexWriter(directory, conf);
			Document doc = null;
			int date = 1;
			for(int i=0;i<idArr.length;i++) {
				doc = new Document();
				doc.add(new StringField("id",idArr[i],Field.Store.YES));
				doc.add(new StringField("email",emailArr[i],Field.Store.YES));
				//doc.add(new StringField("content",contentArr[i],Field.Store.YES));
				doc.add(new TextField("content",contentArr[i],Field.Store.YES));
				doc.add(new StringField("name",nameArr[i],Field.Store.YES));
				doc.add(new StringField("addr",addrArr[i],Field.Store.YES));
				doc.add(new StringField("date","2014-12-0"+date,Field.Store.YES));
				writer.addDocument(doc);
				date++;
			}

			//新的版本对 Field 进行了更改，StringField 索引但是不分词、StoreField 存储不索引、TextField 索引并分词
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer!=null)writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	//public IndexSearcher getSearcher()

	public static void main(String args[]){
		IndexUtil indexUtil = new IndexUtil();
		indexUtil.index();
		System.out.println("index created");
	}
}