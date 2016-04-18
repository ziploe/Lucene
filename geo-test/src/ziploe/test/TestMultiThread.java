package ziploe.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class TestMultiThread {
	public static void main(String[] args) throws IOException, Exception{
		/*BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("请输入一串字符串");
		String text = buffer.readLine();
		System.out.println("您输入的字符串是:" + text);
		*/
		Date time =new Date();
		System.out.println("time:" + time);
		//Thread.sleep(5000);
		Date time1 =new Date();
		System.out.println("ceil:" + Math.ceil(1.0));
		System.out.println("time:" + time1.toString());
		System.out.println("time:" + time1.getTime());
		}
	

}
