package test;

import model.TransitiveMatrix;

public class MatrixTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		TransitiveMatrix A = new TransitiveMatrix(3,3);
		A.insertOneInstance(3, 3, 6);
		A.insertOneInstance(3, 2, 5);
		A.insertOneInstance(3, 1, 4);
		A.insertOneInstance(2, 2, 4);
		A.insertOneInstance(1, 3, 4);
		A.insertOneInstance(2, 3, 5);
		A.insertOneInstance(2, 1, 3);
		A.insertOneInstance(1, 1, 2);
		A.insertOneInstance(1, 2, 2);
		System.out.println(A);
	}

}
