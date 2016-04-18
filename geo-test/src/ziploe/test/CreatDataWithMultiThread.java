package ziploe.test;

public class CreatDataWithMultiThread {

	public static void main(String[] args) throws Exception{
		int totalShip=Integer.parseInt(args[0]);
		int totalSailTime=Integer.parseInt(args[1]);
		while(totalShip>0){
			for(int i=0;i<4;i++){
				if(totalShip<0){
					break;
				}
				int shipId=totalShip;
				CreateDataPerThread firstThread=new CreateDataPerThread();
				firstThread.setThreadId(i);
				firstThread.setId(shipId);
				firstThread.setTotalSailTime(totalSailTime);

				Thread mt =firstThread.new CreateDataPerThread1();
				mt.start();
				totalShip--;
			}
			
		}

	}



}
