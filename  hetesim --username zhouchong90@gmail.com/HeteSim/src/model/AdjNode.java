package model;

/**
 * this class defines the node of adjMatrix
 * @author zhouchong90
 *
 */
public class AdjNode {

	/**
	 * the row and col index of this node.
	 * because it is sparse, it don't need a boolean data to identify 
	 * whether it is a 1 or 0.
	 */
	private int rowIndex, colIndex;
	
	public int getRowIndex() {
		return rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public AdjNode(int rowIndex, int colIndex)
	{
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}
	
	public String toString()
	{
		return "("+rowIndex + "," + colIndex + ")";
	}
}
