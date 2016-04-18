
package org.ziploe.lemma;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**这个程序可以进行文本时态还原 需要首先在LexiconMap里 配置词典（Nupos）位置 词典在lib里
 * 
 * @author 蒲路/Ziploe 
 * @param  filePath 所要处理的文本的位置 
 */
public class TestDemo {
	public static void main(String[] args) throws Exception {
		System.out.println("program begin...");
		
		Analyzer analyzer=null;
		//		Analyzer analyzer=new ClassicAnalyzer();
		//		Analyzer analyzer=new KeywordAnalyzer();
		//		Analyzer analyzer=new EnglishAnalyzer();

		//		analyzer=new UAX29URLEmailAnalyzer(Version.LUCENE_44);

		//		analyzer =new StandardAnalyzer();
		
		// 需要进行处理的文件
		String filePath ="C:\\Users\\Administrator\\Desktop\\text1\\1.txt";
		//String filePath ="C:\\Users\\Administrator\\Desktop\\text\\whole.txt";

		//String filePath =" ";
		BufferedReader br=new BufferedReader(new FileReader(filePath));
		String line="";
		StringBuffer  buffer = new StringBuffer();
		while((line=br.readLine())!=null){
			buffer.append(line);
		}
		String fileContent = buffer.toString();
		

		analyzer=new MyEnglishAnalyzer();

		//StringReader r=new StringReader("wel come hh bj");
		StringReader r=new StringReader(fileContent);
		
		System.out.println("analyze begin...");
		Date start =new Date();
		TokenStream tokenStream=analyzer.tokenStream("content", r);


		// 添加单词属性 
		CharTermAttribute charTermAttribute = tokenStream  
				.addAttribute(CharTermAttribute.class);  
		// 添加偏移量属性 
		//OffsetAttribute offsetAttribute = tokenStream  .addAttribute(OffsetAttribute.class);  
		// 添加位置属性 
		//PositionIncrementAttribute positionIncrementAttribute = tokenStream  .addAttribute(PositionIncrementAttribute.class);  
		// 添加类型属性 
		//TypeAttribute typeAttribute = tokenStream  .addAttribute(TypeAttribute.class); 

	//	int position = 0;

		tokenStream.reset();
		
		while (tokenStream.incrementToken()) {  

 
			// 输出还原后的结果 
			System.out.println(charTermAttribute.toString() );
		}
		

		// 循环输出结果
		/*while (tokenStream.incrementToken()) {  

			int increment = positionIncrementAttribute.getPositionIncrement();  
			if (increment > 0) {  
				// 位置信息 
				System.out.println("position:" + (position += increment));  
			}  
			// 打印结果 
			System.out.println("单词原型:" + charTermAttribute.toString()  
			+ "初始偏移startOffset:" + offsetAttribute.startOffset()  
			+ "结束偏移endOffset:" + offsetAttribute.endOffset() + "单词类型type:"  
			+ typeAttribute.type()+"类");



		}  */
		Date end =new Date();
		System.err.println(end.getTime()-start.getTime() +"ms cost to lemma");


	}
}
