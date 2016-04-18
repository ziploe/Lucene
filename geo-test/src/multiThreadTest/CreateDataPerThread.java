package multiThreadTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

public class CreateDataPerThread implements Runnable {
	private int threadId=0;
	private int threadNum=0;
	private int id;		//船号
	private float lon ; //经度
	private float lat ; //纬度
	private double vx ; //x方向速度
	private double vy ; //y方向速度
	private int precision=5;
	private int sailingTime=0;		//开始航行的时间
	private int totalSailTime=0;	//总航行时间
	private String line=null;
	private String fatherPath="D:\\data\\multiThread"; //数据父目录
	private String dataPath=null;
	private int count = 0;
	Random random=new Random();
	
	
	public void run(){
		try {
			//threadNum++;
			//初始化数据存储地址
			dataPath=fatherPath+"\\data"+threadId+".txt";
			File dataFile = new File(dataPath);
			OutputStream out = new FileOutputStream(dataFile,true);
			byte[] b;
			lon=random.nextFloat()*10;
			lat=random.nextFloat()*10;
			for(;sailingTime<totalSailTime;sailingTime++){		
				//速度
				vx=(random.nextFloat()-0.4)/10000.0;
				vy=(random.nextFloat()-0.6)/100000.0;
				lon=(float)(lon+vx);
				lat=(float)(lon+vy);
				line=sailingTime+"\t"+id+"\t"+lon+"\t"+lat+"\n";
				b=line.getBytes();
				System.out.println(line);
				out.write(b);
			}
			out.close();
			//Thread.sleep(3000);
			//System.out.println(threadNum);
			//threadNum--;
			

		}catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
