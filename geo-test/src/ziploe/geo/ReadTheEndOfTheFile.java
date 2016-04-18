package ziploe.geo;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadTheEndOfTheFile {
	public static void main(String args[]){
		RandomAccessFile rf=null;
		int lastNLine=3000;
		String dataPath="E:\\data\\data\\1millionWithTime.txt";
		if(args.length>0){
			lastNLine=Integer.parseInt(args[0]);
			dataPath=args[1];
		}
		try {
			rf=new RandomAccessFile(dataPath,"r");
			long len=rf.length();
			long start=rf.getFilePointer();
			long nextend=start+len-1,end=nextend;
			String line;
			rf.seek(nextend);
			int c=-1;
			while(nextend>(end-lastNLine)){
				c=rf.read();
				//System.out.println("hello world");
				if(c=='\n'||c=='\r'){
					line=rf.readLine();
					if(line==null||line.length()==0){//处理文件末尾是空行这种情况
						nextend--;
						rf.seek(nextend);
						continue;}
					System.out.println(line);
					nextend--;
				}
				nextend--;
				rf.seek(nextend);
				if(nextend==0){//当文件指针退至文件开始处，输出第一行
					System.out.println(rf.readLine());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
