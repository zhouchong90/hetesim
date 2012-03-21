package toolKit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import calHeteSim.QuickHeteSim;

import model.Data;

public class AllWeightedTransMats
{
	private Data data;
	public AllWeightedTransMats(Data data)
	{
		this.data = data;
	}
	
	public void computeAllWeitedTransMats(int low,int high, QuickHeteSim qhs, String outputDir) throws FileNotFoundException, IOException
	{
		for(String sName:data.getEntities().keySet())
		{	
			WeightedTransMats wtm = new WeightedTransMats(data);
			for(String eName:data.getEntities().keySet())
			{
				PreCalculate preCal = new PreCalculate(data);
				ArrayList<String> paths = preCal.calPosiPath(sName, eName, low, high);
				
				wtm.weightedMats.put(sName+"-"+eName, wtm.calWeightedMat(paths,qhs));
			}
			wtm.outAsStream(outputDir+"/"+sName+".wtm");
			System.out.println(sName+" Done");
		}	
	}
}
