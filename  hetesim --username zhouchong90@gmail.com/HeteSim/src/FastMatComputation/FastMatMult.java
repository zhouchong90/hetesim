package FastMatComputation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.lang.Math;
import model.TransNode;
import model.TransitiveMatrix;

public class FastMatMult
{
	/**
	 * delete elements in mat;
	 * @param mat
	 * @return
	 */
	public TransitiveMatrix truncation(TransitiveMatrix mat)
	{
		LinkedList<TransNode> tmp = new LinkedList<TransNode>();
		for(Integer k : mat.getRows().keySet())
		{
			int n = deleteFunction(mat.getRows().get(k).size());
			if(n < mat.getRows().get(k).size())
			{
				bubbleSortData(mat.getRows().get(k));
				for(int i = 0;i < n;i++)
					tmp.add(mat.getRows().get(k).get(i));
				for(TransNode eachNode : mat.getRows().get(k))
					if(tmp.contains(eachNode))
						;
					else
						mat.getRows().get(k).remove(eachNode);
				bubbleSortPosY(mat.getRows().get(k));
			}
			else
				continue;
		}
		return mat;		
	}
	/**
	 * sort a row in rows by data ,big first
	 * @param al
	 * @return
	 */
	private LinkedList<TransNode> bubbleSortData(LinkedList<TransNode> al)
	{
		for(int i = 0;i < al.size()-1;i++)
			for(int j = 0;j < al.size()-1;j++)
				if(al.get(j).getData() < al.get(j+1).getData())
				{
					TransNode tmp = al.get(j+1);
					al.set(j+1,al.get(j));
					al.set(j,tmp);
				}
		return al;
	}
	/**
	 * sort a row in rows by posY,small first
	 * @param al
	 * @return
	 */
	private LinkedList<TransNode> bubbleSortPosY(LinkedList<TransNode> al)
	{
		for(int i = 0;i < al.size()-1;i++)
			for(int j = 0;j < al.size()-1;j++)
				if(al.get(j).getColIndex() > al.get(j+1).getColIndex())
				{
					TransNode tmp = al.get(j+1);
					al.set(j+1,al.get(j));
					al.set(j,tmp);
				}
		return al;
	}
	/**
	 * delete a node
	 * @param node
	 * @param mat
	 * @return
	 */
	private boolean deleteTransNode(TransNode node,TransitiveMatrix mat)
	{
		Integer posX,posY;
		HashMap<Integer,LinkedList<TransNode>> rows = new HashMap<Integer,LinkedList<TransNode>>();
		HashMap<Integer,LinkedList<TransNode>> cols = new HashMap<Integer,LinkedList<TransNode>>();
		rows = mat.getRows();
		cols = mat.getCols();
		posX = node.getRowIndex();
		posY = node.getColIndex();
		for(TransNode eachNode : rows.get(posX))
			if(eachNode == node)
				rows.get(posX).remove(node);
		for(TransNode eachNode : cols.get(posY))
			if(eachNode == node)
			{
				cols.get(posY).remove(node);
				return true;
			}
		return false;
	}
	/**
	 * calculate the number of remains
	 * @param n
	 * @return
	 */
	private int deleteFunction(int n)
	{
		int result = 0;
		double x = (double)n;
		result = (int) (10*x*Math.pow(0.618,Math.log(x)));
		if(result > n)
			return n;
		else
			return result;
	}
}
