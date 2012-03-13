package calHeteSim;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.AdjMatrix;
import model.Data;
import model.TransNode;
import model.TransitiveMatrix;

/**
 * this class decompose a Path and return two path lists
 * @author zhouchong90
 *
 */
public class Decompose 
{
	/**
	 * for a path with even length, it decomposed into two list of Path sequence
	 * APC->A-P,C-P
	 * @param path
	 * @return
	 */
	public List<List<String>> decomposeEven(String HeteSimPath)
	{
		List<String> first = new ArrayList<String>();
		List<String> second = new ArrayList<String>();
		
		String[] splits = HeteSimPath.split(",");
		for(int i = 0; i <= splits.length/2-1; i++)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(splits[i]).append("-").append(splits[i+1]);
			first.add(sb.toString());
		}
		
		for(int j = splits.length-1; j >= splits.length/2+1; j--)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(splits[j]).append("-").append(splits[j-1]);
			second.add(sb.toString());
		}
		
		List<List<String>> result = new ArrayList<List<String>>();
		result.add(first);
		result.add(second);
		return result;
	}
	
	/**
	 * for a path with odd length, it decomposed into two list of Path sequence
	 * and it generates the middle AdjMatrix
	 * APVC->A-P,P-*; C-V, V-*
	 * @param path
	 * @param left
	 * @param right
	 * @return
	 */
	public List<List<String>> decomposeOdd(Data data, String HeteSimPath, HashMap<String, TransitiveMatrix> tmpMat)
	{	
		List<String> first = new ArrayList<String>();
		List<String> second = new ArrayList<String>();
		
		String[] splits = HeteSimPath.split(",");
		//生成中间矩阵
		String middle = splits[splits.length/2-1]+","+splits[splits.length/2];
		generateTmpMatrix(data, middle,tmpMat);
		
		//分路径
		for(int i = 0; i <= splits.length/2-2; i++)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(splits[i]).append("-").append(splits[i+1]);
			first.add(sb.toString());
		}
		StringBuffer firstTmp = new StringBuffer();
		firstTmp.append(splits[splits.length/2-1]).append("-").append("*");
		first.add(firstTmp.toString());
		
		for(int j = splits.length-1; j >= splits.length/2+1; j--)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(splits[j]).append("-").append(splits[j-1]);
			second.add(sb.toString());
		}
		StringBuffer secondTmp = new StringBuffer();
		secondTmp.append(splits[splits.length/2]).append("-").append("*");
		second.add(secondTmp.toString());
		
		List<List<String>> result = new ArrayList<List<String>>();
		result.add(first);
		result.add(second);
		
		return result;
	}
	
	/**
	 * generate the middle matrix.
	 * P,V--> P-*, V-* 
	 * @param middlePath
	 * @param left
	 * @param right
	 */
	private void generateTmpMatrix(Data data, String middlePath, HashMap<String, TransitiveMatrix> tmpMat)
	{
		String midMatName = middlePath.replace(",", "-");
		TransitiveMatrix midMat = data.getTransMat(midMatName);
		if (midMat == null)
		{
			throw new IllegalArgumentException(midMatName+" does not exist!");
		}
		String[] tmp = midMatName.split("-");
		String leftMatName = tmp[0]+"-"+"*";
		String rightMatName = tmp[1]+"-"+"*";
		
		//统计非0元素的个数
		int numOfOnes = 0;
		for(Integer row:midMat.getRows().keySet())
			numOfOnes+=midMat.getRows().get(row).size();
		
		TransitiveMatrix left = new TransitiveMatrix(midMat.getRowDim(),numOfOnes);//P-*
		tmpMat.put(leftMatName, left);
		
		AdjMatrix rightTrans = new AdjMatrix(midMat.getColDim(),numOfOnes);//V-*
	
		//midMat的没条连线的编号
		int i = 1;
		for(LinkedList<TransNode> row:midMat.getRows().values())
		{
			for(TransNode node:row)
			{
				left.insertOneInstance(node.getRowIndex(), i, node.getData());
				rightTrans.insertOneInstance(node.getColIndex(), i);
				i++;
			}
		}
		
		TransitiveMatrix right = new TransitiveMatrix(rightTrans);
		tmpMat.put(rightMatName, right);
	}
}
