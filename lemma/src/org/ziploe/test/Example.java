package org.ziploe.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class Example {  
	  
    public static void main(String[] args) throws Exception {  
  
        testIndexAndSearchold();  
    }  
  
    public static void testIndexAndSearchold() throws Exception {  
  
        Analyzer analyzer = new StandardAnalyzer();  
          
        Directory directory = new RAMDirectory();  
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
          
        // Directory directory = FSDirectory.getDirectory("/tmp/testindex");  
        IndexWriter writer =new IndexWriter(directory,iwc ); 
        Document doc = new Document();  
        //搜索引擎支持包含字符的检索，如用户输入“Add”，可检索出包含“Add”内容的词条；用户输入“dd”，也可检索出包含“Add”内容的词条。  
        doc.add(new StringField("name","text 谁啊 addd  dd",Field.Store.YES));
        doc.add(new StringField("content","我是内容1号 content text 什么 addddd 你是 ",Field.Store.YES));
     
        writer.addDocument(doc);  
          
        Document doc2 = new Document();  
        String text = "This is the text add   to be indexed. 你好啊 呵呵 内存索引";  
        doc2.add(new StringField("name",text,Field.Store.YES));
        doc2.add(new StringField("content","我是内容2号 content 不知道 addddd 的都是 ",Field.Store.YES));    
       
        writer.addDocument(doc2);  
          
        Document doc3 = new Document();  
        doc3.add(new StringField("name","add hello 测试的数据",Field.Store.YES));
        doc3.add(new StringField("name","我是内容3号 content  addddd 乌瑟尔为对方 ",Field.Store.YES));
           
        writer.addDocument(doc3);  
        //writer.optimize();  
        writer.close();  
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher isearcher = new IndexSearcher(reader);  
          
        //新增MultiFieldQueryParser等，修复bug  
        //修改QueryParser支持？通配符  
        //新增CachingWrapperFilter和PerFieldAnalyzerWrapper等  
        //新增ParallelMultiSearcher等  
        //新增MMapDirectory等  
        //新增FieldSelector等  
        //新增BoostingTermQuery等  
        //新增SpanQueryFilter等  
        //新增QueryAutoStopWordAnalyzer等  
        //新增FieldCacheRangeFilter等  
        //新增AttributeFactory等  
        //新增ReusableAnalyzerBase等,新增FieldValueFilter等  
        //新增RegexpQuery等,新增BloomFilteringPostingsFormat等  
        //新增AnalyzingSuggester和FuzzySuggester等  
        //新增AutomatonQuery ，用来返回所有Term匹配有限状态机文章列表  
        //当前版本Lucene 4.1已经实现了所有这些主流的检索模型。支持TFIDF相似度度量，最佳匹配Okapi BM25相似度度量，随机分歧DFR相似度度量，Dirichlet和JelinekMercer相似度度量，IB相似度度量。  
          
        //通配符查询,继承自MultiTermQuery 支持通配符：* ? ~  说明：* 匹配任何字符, ? 匹配单一字符  
        //WildcardQuery query = new WildcardQuery(new Term("name","*dd*"));  
          
        //模糊查询,参数：项+匹配度,增加"~"后缀实现模糊查询  
        //FuzzyQuery query=new FuzzyQuery(new Term("name", "add"),0.9f);  
        /* 
        SpanQuery按照词在文章中的距离或者查询几个相邻词的查询 
           SpanQuery包括以下几种 
           SpanTermQuery：词距查询的基础，结果和TermQuery相似，只不过是增加了查询结果中单词的距离信息。 
           SpanFirstQuery：在指定距离可以找到第一个单词的查询。 
           SpanNearQuery：查询的几个语句之间保持者一定的距离。 
           SpanOrQuery：同时查询几个词句查询。 
           SpanNotQuery：从一个词距查询结果中，去除一个词距查询。*/  
        //跨度查询，范围查询  
        //SpanTermQuery query=new SpanTermQuery(new Term("name", "add*"));  
        //权重值查询？  
        //BoostingTermQuery query=new BoostingTermQuery(new Term("name", "dd"));  
              
        //项匹配查询=精确查询  
        //TermQuery query=new TermQuery(new Term("name", "add"));  
        //多字段查询  
        //QueryParser parser=new MultiFieldQueryParser(new String[]{"content","name"}, analyzer);  
        //Query query = parser.parse("add");  
        //QueryParser parser = new QueryParser("name",analyzer);      
        //Query query = parser.parse("add");  
          
        //词组查询  
        //PhraseQuery phraseQuery=new PhraseQuery();  
        //phraseQuery.add(new Term("name", "hello"));  
        //多短语查询？  
        //MultiPhraseQuery query=new MultiPhraseQuery();  
        //query.add(new Term("name", "hello"));  
          
        //二层过滤包装  
        //FilteredQuery filteredQuery=new FilteredQuery(query,);  
        //RangeQuery query=new RangeQuery(new Term("name", "dd"), new Term("name", "add"), false);  
        //多条件  结合查询  
        /* 
        BooleanQuery booleanQuery=new BooleanQuery();        
        //BooleanClause clause=new BooleanClause();      
        booleanQuery.add(phraseQuery, BooleanClause.Occur.MUST); 
        booleanQuery.add(query,BooleanClause.Occur.MUST); 
        System.out.println(booleanQuery.toString()); 
        */  
        //前缀搜索，相当于add*  
        PrefixQuery query=new PrefixQuery(new Term("name", "dd"));  
          
        System.out.println(query.toString());  
          
      /*    
        TopDocs tds = isearcher.search(query, 10);
      //  TopDocsCollector hits = new TopDocsCollector(100);  

       // isearcher.search(query, tds);
        
        //Hits hits2=isearcher.search(query);  
        //hits2.doc(0).get("content");  
     //   System.out.println("TotalHits:"+hits.getTotalHits());  
          
        System.out.println("结果:");  
          
     //   ScoreDoc[] scoreDocs=hits.topDocs().scoreDocs;//该方法会影响hits.topDocs().scoreDocs[i].length  
        for (int i = 0; i < scoreDocs.length; i++) {  
              
           // int doc_id=scoreDocs[i].doc;  
            System.out.println("命中的文档编号id="+doc_id);  
            Document hitDoc = isearcher.doc(doc_id);  
            System.out.println("name="+hitDoc.get("name")+"-content="+hitDoc.get("content"));  
        }  
  
     //   isearcher.close();  
        directory.close();  
    }  */
    }
}  