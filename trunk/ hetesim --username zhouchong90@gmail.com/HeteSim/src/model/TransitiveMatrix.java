package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * this matrix calculates and store the possibilityMatrix all constructing and
 * operating process is slow, and searching process is quick.
 * 
 * @author zhouchong90
 * 
 */
public class TransitiveMatrix implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String matrixName;

	public String getMatrixName()
	{
		return matrixName;
	}

	public void setMatrixName(String matrixName)
	{
		this.matrixName = matrixName;
	}

	/**
	 * it stores the Matrix in a row Packed way;
	 */
	private HashMap<Integer, LinkedList<TransNode>> rows;

	/**
	 * when the result data is smaller than this, it deletes the result
	 */
	private double deleteLimit = 0.0000;

	/**
	 * it stores the Matrix in a col Packed way;
	 */
	private HashMap<Integer, LinkedList<TransNode>> cols;

	private int rowAvgOutDegree, colAvgInDegree;

	public int getRowAvgOutDegree()
	{
		return rowAvgOutDegree;
	}

	public int getColAvgInDegree()
	{
		return colAvgInDegree;
	}

	public HashMap<Integer, LinkedList<TransNode>> getRows()
	{
		return rows;
	}

	public HashMap<Integer, LinkedList<TransNode>> getCols()
	{
		return cols;
	}

	private int colDim, rowDim;

	public int getColDim()
	{
		return colDim;
	}

	public int getRowDim()
	{
		return rowDim;
	}

	public TransitiveMatrix(int rowDim, int colDim)
	{
		this.rowDim = rowDim;
		this.colDim = colDim;
		rows = new HashMap<Integer, LinkedList<TransNode>>();
		cols = new HashMap<Integer, LinkedList<TransNode>>();
	}

	/**
	 * derived from an AdjMatrix directly as its original Matrix.
	 * 
	 * @param adjMat
	 */
	public TransitiveMatrix(AdjMatrix adjMat)
	{
		this.rowDim = adjMat.getRowDim();
		this.colDim = adjMat.getColDim();
		rows = new HashMap<Integer, LinkedList<TransNode>>();
		cols = new HashMap<Integer, LinkedList<TransNode>>();

		for (int i : adjMat.getRows().keySet())
		{
			LinkedList<AdjNode> adjRow = adjMat.getRows().get(i);
			double data = 1 / (double) adjRow.size();
			for (AdjNode a : adjRow)
			{
				TransNode newNode = new TransNode(a.getRowIndex(),
						a.getColIndex(), data);
				this.insertOneInstance(newNode);
			}
		}
	}

	public TransitiveMatrix()
	{
	}

	/**
	 * when providing rowIndex, it generates a row vector of PossibilityMatrix
	 * 
	 * @param adjMat
	 * @param rowIndex
	 */
	public TransitiveMatrix getRowTransMat(int rowIndex)
	{
		TransitiveMatrix rowMat = new TransitiveMatrix(1, this.getColDim());
		rowMat.setMatrixName(matrixName);

		for (TransNode node : rows.get(rowIndex))
			rowMat.insertOneInstance(node);

		return rowMat;
	}

	/**
	 * when providing colIndex, it generates a col vector of PossibilityMatrix
	 * 
	 * @param adjMat
	 * @param rowIndex
	 */
	public TransitiveMatrix getColTransMat(int colIndex)
	{
		TransitiveMatrix colMat = new TransitiveMatrix(this.getRowDim(), 1);
		colMat.setMatrixName(matrixName);

		for (TransNode node : cols.get(colIndex))
			colMat.insertOneInstance(node);

		return colMat;
	}

	/**
	 * add a new node to this Matrix
	 * 
	 * @param rowIndex
	 * @param colIndex
	 * @param data
	 */
	public void insertOneInstance(int rowIndex, int colIndex, double data)
	{
		TransNode newNode = new TransNode(rowIndex, colIndex, data);
		insertOneInstance(newNode);
	}

	public void insertOneInstance(TransNode node)
	{
		// 插入行
		if (rows.containsKey(node.getRowIndex()))// 旧行
		{
			ListIterator<TransNode> lit = rows.get(node.getRowIndex()).listIterator();

			TransNode currentNode = null;
			
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
		else// 新行
		{
			LinkedList<TransNode> newRow = new LinkedList<TransNode>();
			newRow.add(node);
			rows.put(node.getRowIndex(), newRow);
		}

		// 插入列
		if (cols.containsKey(node.getColIndex()))// 旧列
		{			
			ListIterator<TransNode> lit = cols.get(node.getColIndex()).listIterator();
			
			TransNode currentNode = null;
			
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
		else// 新列		
		{
			LinkedList<TransNode> newCol = new LinkedList<TransNode>();
			newCol.add(node);
			cols.put(node.getColIndex(), newCol);
		}
	}

	/**
	 * @return the transpose matrix of this matrix
	 */
	public TransitiveMatrix transpose()
	{
		TransitiveMatrix trans = new TransitiveMatrix(this.colDim, this.rowDim);
		String[] tmp = matrixName.split("-");
		trans.setMatrixName(tmp[1] + "-" + tmp[0]);

		// 重新构造整个矩阵
		for (int i : this.rows.keySet())
		{
			for (TransNode a : rows.get(i))
			{
				TransNode newNode = new TransNode(a.getColIndex(),
						a.getRowIndex(), a.getData());
				trans.insertOneInstance(newNode);
			}
		}
		return trans;
	}

	/**
	 * return the multiple of C=A*B
	 * 
	 * @param B
	 * @return
	 */
	public TransitiveMatrix times(TransitiveMatrix B)
	{
		if (this.colDim != B.getRowDim())
		{
			throw new IllegalArgumentException("times, dimension doesn't match");
		}

		TransitiveMatrix C = new TransitiveMatrix(this.rowDim, B.getColDim());
		C.setMatrixName(matrixName.split("-")[0] + "-"
				+ B.getMatrixName().split("-")[1]);

		// test
		double i = 1;
		int j = 1;

		for (LinkedList<TransNode> row : rows.values())
		{
			int rowIndex = row.getFirst().getRowIndex();
			for (LinkedList<TransNode> col : B.getCols().values())
			{
				int colIndex = col.getFirst().getColIndex();
				double data = arrayTimes(row, col);

				if (data > deleteLimit)
				{
					C.insertOneInstance(rowIndex, colIndex, data);
				}
			}
			

			while( Math.floor(i/(double)rows.size()*100) > j  )
			{
				System.out.println(j+"%");
				j++;
			}
			i++;
		}
		return C;
	}

	private double arrayTimes(LinkedList<TransNode> row,
			LinkedList<TransNode> col)
	{
		double data = 0;
		Iterator<TransNode> itA = row.iterator();
		Iterator<TransNode> itB = col.iterator();
		TransNode Anode = null, Bnode = null;
		Anode = itA.next();

		Bnode = itB.next();
		boolean changed = true;
		while (changed)
		{
			changed = false;
			if (Anode.getColIndex() < Bnode.getRowIndex())
			{
				if(itA.hasNext())
				{
					Anode = itA.next();
					changed = true;
				}
				else
					break;
			}
			else if (Anode.getColIndex() > Bnode.getRowIndex())
			{
				if(itB.hasNext())
				{
					Bnode = itB.next();
					changed = true;
				}
				else
					break;
			}
			else
			{
				data += Anode.getData() * Bnode.getData();
				if (itA.hasNext())
				{
					Anode = itA.next();
					changed = true;
				}
				else
					break;

				if (itB.hasNext())
				{
					Bnode = itB.next();
					changed = true;
				}
				else
					break;
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		return data;
	}

	public TransitiveMatrix normalizedTimes(TransitiveMatrix B)// 耗时！
	{
		long start = System.currentTimeMillis();
		if (this.colDim != B.getRowDim())
		{
			throw new IllegalArgumentException("times, dimension doesn't match");
		}

		HashMap<Integer, Double> leftRowLength = new HashMap<Integer, Double>();
		HashMap<Integer, Double> rightColLength = new HashMap<Integer, Double>();
		// 计算长度
		for (Integer rowIndex : this.getRows().keySet())
		{
			double length = 0;
			for (TransNode node : this.getRows().get(rowIndex))
			{
				length += node.getData() * node.getData();
			}
			leftRowLength.put(rowIndex, Math.sqrt(length));
		}

		for (Integer colIndex : B.getCols().keySet())
		{
			double length = 0;
			for (TransNode node : B.getCols().get(colIndex))
			{
				length += node.getData() * node.getData();
			}
			rightColLength.put(colIndex, Math.sqrt(length));
		}

		// 构造normalized matrix
		TransitiveMatrix tmpA = new TransitiveMatrix(this.rowDim, this.colDim);
		tmpA.setMatrixName(matrixName);
		TransitiveMatrix tmpB = new TransitiveMatrix(B.rowDim, B.colDim);
		tmpB.setMatrixName(B.getMatrixName());

		for (Integer rowIndex : this.getRows().keySet())
		{
			double length = leftRowLength.get(rowIndex);
			for (TransNode node : this.getRows().get(rowIndex))
			{
				TransNode newNode = new TransNode(node.getRowIndex(),
						node.getColIndex(), node.getData() / length);
				tmpA.insertOneInstance(newNode);
			}
		}

		for (Integer colIndex : B.getCols().keySet())
		{
			double length = rightColLength.get(colIndex);
			for (TransNode node : B.getCols().get(colIndex))
			{
				TransNode newNode = new TransNode(node.getRowIndex(),
						node.getColIndex(), node.getData() / length);
				tmpB.insertOneInstance(newNode);
			}
		}
		long mid = System.currentTimeMillis();
		System.out.println("construct norm in " + (mid - start) / 1000 + "s");

		return tmpA.times(tmpB);
	}

	public String toString()
	{
		return rows.toString();
	}

	public TransitiveMatrix trim(int size)// 应该有方法可以优化，现在实现方法：完全一个个TranNode重新添加
	{
		TransitiveMatrix resultMat = new TransitiveMatrix(this.rowDim,
				this.colDim);
		resultMat.setMatrixName(matrixName);

		Comparator<TransNode> c = new Comparator<TransNode>()
		{
			@Override
			public int compare(TransNode o1, TransNode o2)
			{
				TransNode n1 = (TransNode) o1;
				TransNode n2 = (TransNode) o2;
				if (n1.getData() > n2.getData())
					return -1;
				else if (n1.getData() < n2.getData())
					return 1;
				else
					return 0;
			}
		};

		LinkedList<TransNode> tmpRow = new LinkedList<TransNode>();

		for (LinkedList<TransNode> row : rows.values())
		{
			tmpRow.clear();
			tmpRow.addAll(row);
			Collections.sort(tmpRow, c);

			int maxIndex = Math.min(tmpRow.size(), size);
			int i = 0;
			for (TransNode tmpNode : tmpRow)
			{
				if (i < maxIndex)
					resultMat.insertOneInstance(tmpNode);
				else
					break;
				i++;
			}
		}

		return resultMat;
	}

	public void calAvgDegrees()
	{
		int rowTotalDegree = 0;
		int rowNumber = 0;
		for (LinkedList<TransNode> row : rows.values())
		{
			rowTotalDegree += row.size();
			rowNumber++;
		}
		rowAvgOutDegree = Math
				.round((float) rowTotalDegree / (float) rowNumber);

		int colTotalDegree = 0;
		int colNumber = 0;
		for (LinkedList<TransNode> col : cols.values())
		{
			colTotalDegree += col.size();
			colNumber++;
		}
		colAvgInDegree = Math.round((float) colTotalDegree / (float) colNumber);
	}

	private TransNode isExist(TransitiveMatrix mat, int row, int col)
	{
		Integer rowInt = new Integer(row);
		if (!mat.rows.containsKey(rowInt))
			return null;
		for (TransNode tNode : mat.rows.get(rowInt))
			if (tNode.getColIndex() == col)
				return tNode;
		return null;
	}

	public TransitiveMatrix weightedPlus(ArrayList<TransitiveMatrix> mats,
			ArrayList<Double> weights)
	{
		// 累加矩阵tmpAdd
		TransitiveMatrix tmpAdd = new TransitiveMatrix(mats.get(0).getRowDim(),
				mats.get(0).getColDim());
		tmpAdd.setMatrixName(mats.get(0).getMatrixName());

		for (int i = 0; i < mats.size(); i++)
		{
			TransitiveMatrix mat = mats.get(i);
			for (Integer rowIndex : mat.rows.keySet())
			{
				for (TransNode tNode : mat.rows.get(rowIndex))
				{
					TransNode tmpNode = isExist(tmpAdd, tNode.getRowIndex(),
							tNode.getColIndex());
					if (tmpNode == null)
					{
						tmpNode = new TransNode(tNode.getRowIndex(),
								tNode.getColIndex(), tNode.getData()
										* weights.get(i));
						tmpAdd.insertOneInstance(tmpNode);
					} else
						tmpNode.setData(tmpNode.getData() + tNode.getData()
								* weights.get(i));
				}
			}
		}
		return tmpAdd;
	}
}
