package org.ziploe.lemma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 一个运用词典的词态还原程序 使用修改后的 nupos词典（提取自morphadorner）  提供
 * lemma(String rawWord)接口，可供其他程序调用
 * 字典位置需要重新配置 字典文件在 lib里
 * 名词 ：lemma:词态还原； lexicon:词典； rawWord:待还原单词； lemmaedWord:还原后单词
 * @author 蒲路/Ziploe
 * 
 */

public class LexiconMap {
	private static final Map<String,String> lexiconMap= new HashMap<String,String>();
	static{
		initialLexiconMap();
	}
	private static void initialLexiconMap() {
		Date start =new Date();
		System.out.println("loading lexicon...");
		//String lexPath ="C:\\Program Files\\lucene-5.3.1\\luceneTest\\66\\nupos.txt";
		//String lexPath ="C:/Users/Administrator/Desktop/novel/overCookedNupos.txt";
		String lexPath ="lib/overCookedNupos.txt";
		File lexFile = new File(lexPath);
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(lexFile));
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				String[] arrayStr = new String[] {};
				arrayStr = tempString.split("\t");
				lexiconMap.put(arrayStr[0].toString(),arrayStr[1].toString());
			}
			reader.close();

		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		Date end =new Date();
		Long lexiconLoadTime = end.getTime()-start.getTime();
		System.err.println("lexicon loaded successfully in "+lexiconLoadTime+" ms");
		
	}
	/*public static String lemma(String rawWord){
		String lemmaedWord = lexiconMap.get(rawWord);
		if(lemmaedWord==null){
			return rawWord;
		}else{
			return lemmaedWord;
		}
	}*/
	
	public static String lemma(String rawWord){
		String lemmaedWord = lexiconMap.get(rawWord);
		if(lemmaedWord!=null){
			return lemmaedWord;
		}else{
			return rawWord;
		}
	}

}
