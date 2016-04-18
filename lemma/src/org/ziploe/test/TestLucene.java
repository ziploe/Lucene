package org.ziploe.test;

import java.io.IOException;
import java.util.Date;

import org.apache.lucene.queryparser.classic.ParseException;

public class TestLucene {
	public static void main(String[] args)throws IOException, ParseException{
		HelloLucene hl = new HelloLucene();
		Date start = new Date();
		int i=0;
		/*while(i<250){
		hl.index();;
		i++;
		System.out.println(i);
		}*/
		//hl.index();
		Date indexEnd = new Date();
		hl.searcher("baidu");
		//hl.searcher1("ziploe");

		Date searchEnd = new Date();
		long indexTime = indexEnd.getTime()-start.getTime();
		long searchTime = searchEnd.getTime()-indexEnd.getTime();
		System.out.println(indexTime+" ms cost to index files\n"+searchTime+" ms cost to search");
	

		//	TestScore ld=new TestScore();                        
			//ld.index();
			//ld.search();
	}
}
