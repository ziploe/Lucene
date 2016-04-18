package ziploe.geotest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.apache.lucene.spatial.util.*;
import org.apache.lucene.util.SloppyMath;


public class GeoUtilRandomTest {
	public static void main(String[] args){
		double centerLat,  centerLon,  ptLon,  ptLat,radius;
		centerLat=11.73421;
		centerLon=11.2;
		ptLon=12.1;
		ptLat=12.2;
		radius=1000;
		BigDecimal b  = new BigDecimal(centerLat);
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
		final long hashedCntr = GeoEncodingUtils.mortonHash(centerLon, centerLat);
		centerLon = GeoEncodingUtils.mortonUnhashLon(hashedCntr);
		centerLat = GeoEncodingUtils.mortonUnhashLat(hashedCntr);
		final long hashedPt = GeoEncodingUtils.mortonHash(ptLon, ptLat);
		ptLon = GeoEncodingUtils.mortonUnhashLon(hashedPt);
		ptLat = GeoEncodingUtils.mortonUnhashLat(hashedPt);

		double ptDistance = SloppyMath.haversin(centerLat, centerLon, ptLat, ptLon)*1000.0;
		double delta = StrictMath.abs(ptDistance - radius);

		System.out.println("hashedCntr : "+hashedCntr);
		System.out.println("centerLat : "+centerLat);
		System.out.println("centerLon : "+centerLon);
		System.out.println("hashedPt : "+hashedPt);
		System.out.println("ptLon : "+ptLon);
		System.out.println("ptLat : "+ptLat);


	}

}
