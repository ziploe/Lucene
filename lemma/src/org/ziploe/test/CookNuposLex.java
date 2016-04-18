package org.ziploe.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CookNuposLex {
	public static void main(String[] args){
		String lexPath ="C:\\Users\\Administrator\\Desktop\\text1\\cookedNupos.txt";
		File lexFile = new File(lexPath);
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(lexFile));
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				String[] arrayStr = new String[] {};
				arrayStr = tempString.split("\t");
				if(!arrayStr[0].toString().equals(arrayStr[1].toString())){
				System.out.println(arrayStr[0].toString()+"\t"+arrayStr[1].toString());
				//lexiconMap.put(arrayStr[0].toString(),arrayStr[1].toString());
				}
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
		
	}


}
