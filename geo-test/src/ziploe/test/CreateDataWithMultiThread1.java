package ziploe.test;

public class CreateDataWithMultiThread1 {
	
	
	public static void main(String[] args) throws Exception{
		//int totalShip=20;
		int maxThreadNum =4;
		int threadNum=0;
		int totalShip=Integer.parseInt(args[0]);
		int totalSailTime=Integer.parseInt(args[1]);
		int assignJobs[]=new int[maxThreadNum+1];
		assignJobs[maxThreadNum]=totalShip+1;
		for(int i=0;i<maxThreadNum;i++){
			assignJobs[i]=totalShip*i/maxThreadNum;
		}
		
		for(;threadNum<4;threadNum++){
			CreateDataPerThread firstThread=new CreateDataPerThread();	
			firstThread.setShipIdRange(assignJobs[threadNum], assignJobs[threadNum+1]);
			firstThread.setThreadId(threadNum);
			firstThread.setTotalSailTime(totalSailTime);

			
			Thread mt =firstThread.new CreateDataPerThread1();
			mt.start();
		}
		
		
		
	}

}
