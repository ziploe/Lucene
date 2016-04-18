package ziploe.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import org.apache.lucene.document.DateTools;


public  class CreatDate {

	
	public static void main(String[] args) throws IOException {
		//DateTools;
		Date start = new Date();
		//String dataPath="E:\\data\\data\\1millionWithoutTime1.txt";
		String dataPath="D:\\data\\1yiWithTime.txt";
		File dataFile = new File(dataPath);
		OutputStream out = new FileOutputStream(dataFile,true);
		String line ;
		byte[] b;
		int id;
		float x ,y,xx=0,yy=0;
		Random randomx = new Random();
		Random randomy = new Random();
		for (int i=0;i<1000000;i++){
			id=randomx.nextInt(100);
			x=randomx.nextFloat()*360-180;
			y=randomy.nextFloat()*180-90;
			line=id+"\t"+x/500+"\t"+y/500+"\n";
			b=line.getBytes();
			//System.out.println(id+"\t"+x+"\t"+y);
			if(i%100000==0){
			System.out.println(i);
			}
			out.write(b);			 
		}
		out.close();
		Date end = new Date();
		long timeCost = end.getTime()-start.getTime();
		System.err.print(timeCost+" ms costs to creat date");
	}

}
