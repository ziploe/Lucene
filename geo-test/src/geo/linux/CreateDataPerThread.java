package geo.linux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

public class CreateDataPerThread{
	private int threadId=0;
	private int threadNum=0;
	private int id;		//船号
	int startShipId=0;
	int stopShipId=0;
	private float lon ; //经度
	private float lat ; //纬度
	private double vx ; //x方向速度
	private double vy ; //y方向速度
	private int precision=5;
	private int sailingTime=0;		//开始航行的时间
	private int totalSailTime=0;	//总航行时间 即一条船的数据条数
	private String line=null;
	//private String fatherPath="D:\\data\\multiThread"; //数据父目录
	private String fatherPath="///home/ziploe/zip-geo/data"; //数据父目录
	private String dataPath=null;
	private int count = 0;
	Random random=new Random();
	//DecimalFormat df5  = new DecimalFormat("###.00000");

	public int getThreadNum(){
		return threadNum;
	}
	public void upThreadNum(){
		this.threadNum=threadNum+1;
	}
	public void downThreadNum(){
		this.threadNum=threadNum-1;
	}
	public void setId(int id){
		this.id=id;
	}
	public void setStartShipId(int startShipId){
		this.startShipId=startShipId;
	}
	public void setStopShipId(int stopShipId){
		this.stopShipId=stopShipId;
	}
	public void setShipIdRange(int startShipId,int stopShipId){
		this.startShipId=startShipId;
		this.stopShipId=stopShipId;
	}
	public void setThreadId(int threadId){
		this.threadId=threadId;
	}
	/**
	 * 
	 * @param time the start time
	 */
	public void setStartTime(int time){
		this.sailingTime =time;
	}
	/**
	 * 
	 * @param TotalTime the total sail time
	 */
	public void setTotalSailTime(int TotalTime){
		this.totalSailTime =TotalTime;
	}
	public double Baoliu(float dout){
		double p= Math.pow(10, precision); 
		return Math.round( dout * p ) / p;
	}
	
	public class CreateDataPerThread1 extends Thread{

		@Override
		public void run() {
			try {
				//threadNum++;
				//初始化数据存储地址
				Date start = new Date();
				//dataPath=fatherPath+"\\data"+threadId+".txt";
				dataPath=fatherPath+"/data"+threadId;
				File dataFile = new File(dataPath);
				OutputStream out = new FileOutputStream(dataFile,true);
				byte[] b;

				int currentShipId=startShipId;
				for(;currentShipId<stopShipId;currentShipId++){
					Date startIn = new Date();
					int startSailingTime=sailingTime;
					lon=random.nextFloat()*10;
					lat=random.nextFloat()*10;
					for(;startSailingTime<totalSailTime;startSailingTime++){		
						//速度
						vx=(random.nextFloat()-0.4)/10000.0;
						vy=(random.nextFloat()-0.6)/100000.0;
						lon=(float)(lon+vx);
						lat=(float)(lon+vy);
						line=startSailingTime+"\t"+currentShipId+"\t"+lon+"\t"+lat+"\n";
						b=line.getBytes();
						//System.out.println(line);
						out.write(b);
						
					}
					Date endIn = new Date();
					long timeCost = endIn.getTime()-startIn.getTime();
					System.out.println("thread "+threadId+" cost "+timeCost+" ms to creat ship "+currentShipId+"'s "+totalSailTime+" pieces' of data");
					
				}
				Date end = new Date();
				long timeCost = end.getTime()-start.getTime();
				System.err.println("thread "+threadId+" cost "+timeCost+" ms to creat "+totalSailTime*(stopShipId-startShipId)+"pieces' of data");
				

				out.close();
			}catch (Exception e) {
				e.printStackTrace();
			} 

		} 
	}
	

	
}