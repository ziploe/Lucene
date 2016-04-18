package geo.linux;

public class CreateDataWithMultiThread {
	
	
	public static void main(String[] args) throws Exception{
		
		//int totalShip=20;
		int maxThreadNum =4;
		int threadNum=0;
		int totalShip=Integer.parseInt(args[0]);	//总船数
		int totalSailTime=Integer.parseInt(args[1]);	//每条船多少条数据
		int assignJobs[]=new int[maxThreadNum+1];
		assignJobs[maxThreadNum]=totalShip+1;
		System.out.println("progarm start : creating data of"+totalShip+" ships with perRecord "+totalSailTime);
		for(int i=0;i<maxThreadNum;i++){
			assignJobs[i]=totalShip*i/maxThreadNum;
		}
		
		for(;threadNum<maxThreadNum;threadNum++){
			CreateDataPerThread firstThread=new CreateDataPerThread();	
			firstThread.setShipIdRange(assignJobs[threadNum], assignJobs[threadNum+1]);
			firstThread.setThreadId(threadNum);
			firstThread.setTotalSailTime(totalSailTime);

			
			Thread mt =firstThread.new CreateDataPerThread1();
			mt.start();
		}
		
		
		
	}

}
