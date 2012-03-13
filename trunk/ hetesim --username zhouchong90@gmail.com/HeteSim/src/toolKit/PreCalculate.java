package toolKit;

import model.AdjMatrix;
import model.Data;
import model.Entity;
import model.TransitiveMatrix;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import calHeteSim.QuickHeteSim;
/**
 * constructAdjMatrix(Data),construct a adjMatrix;
 * calPosiPath(int),get all path that length is less than k;
 * isPathRight(String),show whether a path exits;
 * calAllPath(),calculate all path;
 * @author liugang
 *
 */
public class PreCalculate {
	private boolean adjMatrix[][];
	private int size;
	private Data data;

	private ArrayList<String> nodeList;
	
	
	public PreCalculate(Data data)
	{
		this.data = data;

		this.size = data.getEntities().size();
		adjMatrix = new boolean[this.size][this.size];
		for(int i = 0;i < this.size;i++)
			for(int j = 0;j < this.size;j++)
				adjMatrix[i][j] = false;
		nodeList = new ArrayList();
		for(String node : data.getEntities().keySet())
		{
			if(!nodeList.contains(node))
				nodeList.add(node);
		}
		
		constructAdjMatrix();
	}
	
	private void constructAdjMatrix()
	{
		int row,col;
		String[] temp;
		for(String relation : data.getTransMats().keySet())
		{
			temp = relation.split("-");
			row = nodeList.indexOf(temp[0]);
			col = nodeList.indexOf(temp[1]);
			adjMatrix[row][col] = true;
		}
	}
	
	
	private ArrayList<String> calPosiPath(int low,int high)
	{
		ArrayList<String> posiPaths = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		for(int i = 0;i < this.size;i++)
		{
			path.add(0,nodeList.get(i));
			for(int j = low;j <= high;j ++)
				nextPath(i,1,j,path,posiPaths);
		}
		System.out.println("All posiPaths caled.");
		return posiPaths;
	}
	
	private ArrayList<String> calPosiPath(String start,String end, int low, int high)
	{
		ArrayList<String> posiPaths = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();

		path.add(0,start);
		for(int j = low;j <= high; j++)
			nextPath(nodeList.indexOf(start),1,j,path,posiPaths);
		
		ArrayList<String> posiPathsFilter = new ArrayList<String>();
		for(int i = 0;i < posiPaths.size();i++)
		{
			String ch = posiPaths.get(i);
			int len = ch.length();
			if(ch.charAt(len-1) == end.charAt(0))
				posiPathsFilter.add(ch);
		}
		
		System.out.println("All posiPaths caled.");
		System.out.print("They are:");
		System.out.println(posiPathsFilter);
		
		return posiPathsFilter;
	}
	
	private void nextPath(int cur,int i,int n,ArrayList<String> path,ArrayList<String> posiPaths)
	{
		if(i <= n)
		{
			for(int k=0;k < this.size;k++)
			{
				if(adjMatrix[cur][k])
				{
					path.add(i,nodeList.get(k));
					nextPath(k,i+1,n,path,posiPaths);
				}
			}
		}
		else
		{
			String temp;
			temp = path.get(0);
			for(int k = 1;k <= n;k ++)
				temp = temp + "," + path.get(k);
			posiPaths.add(temp);
		}
	}
	
	public boolean isPathRight(String path)
	{
		String[] temp = path.split(",");
		int i = 0;
		int row,col;
		while(i < temp.length-1)
		{
			row = nodeList.indexOf(temp[i]);
			col = nodeList.indexOf(temp[i+1]);
			if(adjMatrix[row][col])
				i++;
			else
				return false;
		}
		return true;
	}
	
	public void calAllPaths(int low, int high, String modelPath) throws FileNotFoundException, IOException
	{
		ArrayList<String> allPaths = calPosiPath(low,high);
		for(String path : allPaths)
		{
			System.out.println("Calculating:"+path);
			QuickHeteSim qhs = new QuickHeteSim();
			qhs.preCalPosMat(data, modelPath , path);
		}
	}
	
	public void calAllPaths(int low, int high, String modelPath, int top) throws FileNotFoundException, IOException
	{
		ArrayList<String> allPaths = calPosiPath(low,high);
		for(String path : allPaths)
		{
			System.out.println("Calculating:"+path);
			QuickHeteSim qhs = new QuickHeteSim();
			qhs.preCalPosMat(data, modelPath , path, top);
		}	
	}
	
	public void calAllPaths(int low, int high, String start, String end, String modelPath) throws FileNotFoundException, IOException
	{
		ArrayList<String> allPaths = calPosiPath(start,end,low,high);
		for(String path : allPaths)
		{
			System.out.println("Calculating:"+path);
			QuickHeteSim qhs = new QuickHeteSim();
			qhs.preCalPosMat(data, modelPath,path);
			System.out.println(path + " done in:"+System.currentTimeMillis());
		}	
	}
	
	public void calAllPaths(int low, int high, String start, String end, String modelPath, int top) throws FileNotFoundException, IOException
	{
		ArrayList<String> allPaths = calPosiPath(start,end,low,high);
		for(String path : allPaths)
		{
			System.out.println("Calculating:"+path);
			QuickHeteSim qhs = new QuickHeteSim();
			qhs.preCalPosMat(data, modelPath,path,top);
		}	
	}

}
