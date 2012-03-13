package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import model.Data;
import model.TransitiveMatrix;
import io.LoadData;
import io.LoadTxt;
import calHeteSim.QuickHeteSim;
import calHeteSim.CalHeteSim;
import calHeteSim.Decompose;
import toolKit.PreCalculate;

public class Test {
	
	public static void main(String[] args) throws FileNotFoundException,IOException, ClassNotFoundException
	{
		System.out.println("start");
		long start = System.currentTimeMillis();
		
/*		LoadTxt ld = new LoadTxt("C:/HeteSim/data");
		Data data = ld.run();
		data.outAsStream("C:/HeteSim/newData.dat");*/
		
		Data data = Data.loadData("C:/HeteSim/newData.dat");
		
		PreCalculate preCal = new PreCalculate(data);
		
		System.out.println("data loaded!");
		
		
		long mid = System.currentTimeMillis();
		System.out.println("load done in:"+(mid-start)/1000 +"s");
		
		preCal.calAllPaths(1, 4, "C", "P", "C:/HeteSim/tmp");
		//QuickHeteSim qhs = new QuickHeteSim();
		//qhs.preCalPosMat(data, "C:/HeteSim/tmp", "A,P,C,P,A");
		
		System.out.println("all done in:"+(System.currentTimeMillis()-mid)/1000+"s");		
	}

}
