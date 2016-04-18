package ziploe.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Random;

public class TestRandom {

	String dataPath="D:\\data\\multiThread\\1.txt";
	public  void data(){
		String line;
		Random rm =new Random();
		int num=100;
		int count=0;
		double total=0;
		try {
			for(;num>0;num--){
				
				double db=rm.nextDouble()-0.6;
				double db1=rm.nextDouble()-0.6;
				if(db>0){
					count++;
				}
				total+=db;
				System.out.println(db);
				//System.out.println(rm.nextDouble()-0.6);
				//System.out.println(rm.nextFloat()-0.6);
				//System.out.println(rm.nextInt());
				//System.out.println(rm.nextLong());
				//System.out.println(rm.nextGaussian());
				
				
				File dataFile = new File(dataPath);
				OutputStream out = new FileOutputStream(dataFile,true);
				byte[] b;
				line=db+"\t"+db1+"\n";
				b=line.getBytes();
				//System.out.println(line);
				out.write(b);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("possitive num :	"+count);
		System.out.println("add all :	"+total);
	}
	
	
	public void testdata(){
		try {
			InputStreamReader reader = new InputStreamReader( new FileInputStream(dataPath));
			BufferedReader br = new BufferedReader(reader); 
			String line = "";  	
			int id, time,num=0;
			float x,y,lon=0,lat=0;
			while ((line=br.readLine())!=null) { 
				String[] dataArr=line.split("\t");
				x=Float.parseFloat(dataArr[0]);
				y=Float.parseFloat(dataArr[1]);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		TestRandom tt = new TestRandom();
		tt.data();
	}
}
