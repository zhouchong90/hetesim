package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import model.AdjMatrix;
import model.Data;
import model.TransitiveMatrix;
import io.LoadData;
import io.LoadTxt;
import calHeteSim.QuickHeteSim;
import calHeteSim.CalHeteSim;
import calHeteSim.Decompose;
import toolKit.PreCalculate;
import toolKit.WeightedTransMats;

public class Test {
	
	public static void main(String[] args) throws FileNotFoundException,IOException, ClassNotFoundException
	{
		System.out.println("start");
		long start = System.currentTimeMillis();
		
		LoadTxt ld = new LoadTxt("C:/HeteSim/MovieData");
		Data data = ld.run();		
		
//		Data data = Data.loadData("C:/HeteSim/MovieData.dat");
		
		
		
		long mid = System.currentTimeMillis();
		System.out.println("load done in:"+(mid-start)/1000 +"s");
		
		
		
//		QuickHeteSim qhs = QuickHeteSim.loadPosMats("C:/HeteSim/tmp");
//		
//		WeightedTransMats wtm = new WeightedTransMats(data);
//		
//		PreCalculate preCal = new PreCalculate(data);
//		ArrayList<String> paths = preCal.calPosiPath("C", "A", 1, 4);
//		wtm.weightedMats.put("C-A", wtm.calWeightedMat(paths,qhs));
//		
//		
//		paths = preCal.calPosiPath("C", "T", 1, 4);
//		wtm.weightedMats.put("C-T", wtm.calWeightedMat(paths,qhs));
//		
//		paths = preCal.calPosiPath("C", "L", 1, 4);
//		wtm.weightedMats.put("C-L", wtm.calWeightedMat(paths,qhs));
//		
//		paths = preCal.calPosiPath("C", "C", 1, 4);
//		wtm.weightedMats.put("C-C", wtm.calWeightedMat(paths,qhs));
//		
//		try {
//			wtm.outAsStream("C:/HeteSim/weightedMats/C.wtm");
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		
		//System.out.println(wtm.getWeightedMat("C-A"));		
		//preCal.calAllPaths(1, 4, "C", "P", "C:/HeteSim/tmp");
		//qhs.preCalPosMat(data, "C:/HeteSim/tmp", "A,P,C,P,A");			
		//WeightedTransMats wtm = new WeightedTransMats(data);
		//ArrayList<String> paths = preCal.calPosiPath("C", "A", 1, 4);
		//wtm.weightedMats.put("C-A", wtm.calWeightedMat(paths,qhs));		
		//System.out.println("all done in:"+(System.currentTimeMillis()-mid)/1000+"s");
		
		CalHeteSim chs = new CalHeteSim(data, "M,C,M,T");
		chs.getHeteSim();
		System.out.println("done in:"+(System.currentTimeMillis()-mid)/1000 +"s");
	}
}
