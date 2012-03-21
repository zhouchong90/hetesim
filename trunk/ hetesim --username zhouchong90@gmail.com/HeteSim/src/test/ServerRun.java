package test;

import java.io.IOException;

import calHeteSim.QuickHeteSim;

import toolKit.AllWeightedTransMats;
import toolKit.PreCalculate;

import model.Data;
import io.LoadTxtFromFile;

public class ServerRun
{

	/**
	 * DataInputPath(dir) tmpMatPath(dir) weightedTransMats(dir)
	 * @param args
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		// TODO Auto-generated method stub
		System.out.println("start");
		long start = System.currentTimeMillis();
		
		LoadTxtFromFile ld = new LoadTxtFromFile(args[0]);
		Data data = ld.run();		
		
		long dataloaded = System.currentTimeMillis();
		System.out.println("load done in:"+(dataloaded-start)/1000 +"s");
		
		//算出所有可能路径
		PreCalculate preCal = new PreCalculate(data);
		preCal.calAllPaths(1, 4, args[1]);
		
		long pathCaled = System.currentTimeMillis();
		System.out.println("calMat done in:"+(pathCaled-dataloaded)/1000 +"s");
		
		
		QuickHeteSim qhs = QuickHeteSim.loadPosMats(args[1]);		
		AllWeightedTransMats awtm = new AllWeightedTransMats(data);
		awtm.computeAllWeitedTransMats(1, 4, qhs, args[2]);
		
		System.out.println("weightedMat done in:"+(System.currentTimeMillis()-pathCaled)/1000 +"s");
		
		System.out.println("all Done in:"+ (System.currentTimeMillis()-start)/1000 +"s");
	}
}