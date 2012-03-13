package model;

import java.io.Serializable;

/**
 * this class specifies the node in Possibility Matrix
 * @author zhouchong90
 *
 */
public class TransNode implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the row and col index of this node.
	 */
	private int rowIndex, colIndex;
	
	/**
	 * the transitive possibility.
	 */
	private double data;
	
	public int getRowIndex() {
		return rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public double getData() {
		return data;
	}
	
	public void setData(double data)
	{
		this.data = data;
	}
	
	public TransNode(int rowIndex, int colIndex, double data)
	{
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.data = data;
	}
	
	public String toString()
	{
		return "("+rowIndex + "," + colIndex + "," + data + ")";
	}
}
