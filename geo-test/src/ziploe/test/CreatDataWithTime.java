package ziploe.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

public class CreatDataWithTime {
	
	public static void main(String[] args) throws IOException {
		Date start = new Date();
		//String dataPath="E:\\data\\data\\1million.txt";
		String dataPath="D:\\data\\1yiWithTime1.txt";
		File dataFile = new File(dataPath);
		OutputStream out = new FileOutputStream(dataFile,true);
		String line ;
		byte[] b;
		int id;
		float x ,y,xx=1,yy=1;
		Random randomx = new Random();
		Random randomy = new Random();
		for (int i=0;i<1000000;i++){
			//Date currenttime = new Date();
			//long currentTime=currenttime.getTime();
			id=randomx.nextInt(100000);
			x=(float) ((randomx.nextFloat()-0.496)/1000000.0);
			y=(float) ((randomx.nextFloat()-0.503)/1000000.0);
			xx=xx+x;
			yy=yy+y;
			line=i+"\t"+id+"\t"+xx+"\t"+yy+"\n";
			b=line.getBytes();
			//System.out.println(id+"\t"+x+"\t"+y);
			//System.out.println(line);
			if(i%1000000==0){
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
