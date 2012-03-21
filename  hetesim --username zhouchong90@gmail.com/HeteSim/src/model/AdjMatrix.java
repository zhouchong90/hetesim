package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * this class defines sparse matrix AdjMatrix of two Types. 
 * i.e. PvsA
 * the construction process is quite slow, try not to construct very frequently.
 * @author zhouchong90
 *
 */
public class AdjMatrix {
	
	/**
	 * it stores the Matrix in a row Packed way;
	 * row index, row
	 */
	private HashMap<Integer, LinkedList<AdjNode>> rows;
	/**
	 * it stores the Matrix in a col Packed way;
	 * col index, col
	 */
	private HashMap<Integer, LinkedList<AdjNode>> cols;
	private int rowDim, colDim;
	
	public int getRowDim() {
		return rowDim;
	}

	public int getColDim() {
		return colDim;
	}

	/**
	 * constructor, row and col dimension is the max size of corresponding Entities
	 * @param rowDim
	 * @param colDim
	 */
	public AdjMatrix(int rowDim, int colDim)
	{
		this.rowDim = rowDim;
		this.colDim = colDim;
		rows = new HashMap<Integer, LinkedList<AdjNode>>();
		cols = new HashMap<Integer, LinkedList<AdjNode>>();
	}
	
	public HashMap<Integer, LinkedList<AdjNode>> getCols() {
		return cols;
	}

	public HashMap<Integer, LinkedList<AdjNode>> getRows() {
		return rows;
	}

	/**
	 * add a new node to this Matrix
	 * @param rowIndex
	 * @param colIndex
	 */
	public void insertOneInstance(int rowIndex, int colIndex)
	{
		AdjNode newNode = new AdjNode(rowIndex, colIndex);
		insertOneInstance(newNode);
	}
	
	/**
	 * add a specific node to this Matrix
	 * @param node node is specified with row and col index.
	 */
	public void insertOneInstance(AdjNode node)
	{
		//插入行
		if(rows.containsKey(node.getRowIndex()))//旧行
		{
			ListIterator<AdjNode> lit = rows.get(node.getRowIndex()).listIterator();

			AdjNode currentNode = null;
			
			if(lit.hasNext())
				currentNode = lit.next();
			
			boolean added = false;
			while(currentNode.getColIndex() < node.getColIndex())
			{
				if(lit.hasNext())
					currentNode = lit.next();
				else//加在末尾
				{
					lit.add(node);
					added = true;
					break;
				}
			}
			if(!added)
			{
				if(currentNode.getColIndex() == node.getColIndex())
					throw new IndexOutOfBoundsException("insertion has encountered a conflict.");
				currentNode = lit.previous();
				lit.add(node);
			}
		}
		else//新行
		{
			LinkedList<AdjNode> newRow = new LinkedList<AdjNode>();
			newRow.add(node);
			rows.put(node.getRowIndex(), newRow);
		}
		
		//插入列
		if(cols.containsKey(node.getColIndex()))//旧列
		{
ListIterator<AdjNode> lit = cols.get(node.getColIndex()).listIterator();
			
			AdjNode currentNode = null;
			
			if(lit.hasNext())
				currentNode = lit.next();
			
			boolean added = false;
			while(currentNode.getRowIndex() < node.getRowIndex())
			{
				if(lit.hasNext())
					currentNode = lit.next();
				else//加在末尾
				{
					lit.add(node);
					added = true;
					break;
				}
			}
			if(!added)
			{
				if(currentNode.getRowIndex() == node.getRowIndex())
					throw new IndexOutOfBoundsException("insertion has encountered a conflict.");
				currentNode = lit.previous();
				lit.add(node);
			}
		}
		else//新列
		{
			LinkedList<AdjNode> newCol = new LinkedList<AdjNode>();
			newCol.add(node);
			cols.put(node.getColIndex(), newCol);
		}
	}
	
	/**
	 * @return	the transpose matrix of this matrix
	 */
	public AdjMatrix transpose()
	{
		AdjMatrix trans = new AdjMatrix(this.colDim, this.rowDim);
		
		//重新构造整个矩阵
		for(LinkedList<AdjNode> row : this.rows.values())
		{
			for(AdjNode a : row)
			{
				AdjNode newNode = new AdjNode(a.getColIndex(), a.getRowIndex());
				trans.insertOneInstance(newNode);
			}
		}
		return trans;
	}
	
	public String toString()
	{
		return rows.toString();
	}
}
