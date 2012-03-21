package toolKit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Data;
import model.TransitiveMatrix;

public class MatChainMult
{

	private Data data;
	private ArrayList<TransitiveMatrix> leftMat;
	private ArrayList<TransitiveMatrix> rightMat;
	private List<String> leftPath;
	private List<String> rightPath;
	
	public MatChainMult(Data data, List<String> leftPath, List<String> rightPath)
	{
		this.data = data;
		this.leftPath = leftPath;
		this.rightPath = rightPath;
	}
	
	
	public TransitiveMatrix computeMultiple(HashMap<String, TransitiveMatrix> tmpMat)
	{
		
		
		return null;
	}
	
	private computeFrequentMat()
	{
		
	}
	
	private int frequency(String regex, String mainString)
	{
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mainString);
		int i = 0;
		
		while(m.find())
			i++;
		return i;
	}
	
	private ArrayList<String> getAllSubStrings(List<String> path)
	{
		return null;
	}
}
