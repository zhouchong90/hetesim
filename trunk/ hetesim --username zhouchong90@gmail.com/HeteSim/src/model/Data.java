package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import calHeteSim.QuickHeteSim;


/**
 * this class stores the main data including relations and entities.
 * the transMats stores A-P as well as P-A
 * @author zhouchong90
 *
 */
public class Data implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * it stores all of the name-index table i.e. A
	 * tableName:Table
	 */
	
	private HashMap<String, Entity> entities;
	
	/**
	 * it stores all TransitiveMatrixs, like A-P, and P-A
	 * key is the name of this TransitiveMatrix.
	 */
	private HashMap<String, TransitiveMatrix> transMats;
	
	public Data()
	{
		entities = new HashMap<String, Entity>();
		transMats = new HashMap<String, TransitiveMatrix>();
	}
	
	public HashMap<String, Entity> getEntities()
	{
		return entities;
	}

	public HashMap<String, TransitiveMatrix> getTransMats()
	{
		return transMats;
	}

	/**
	 * returns the TransitiveMatrix related to this type, 
	 * if not exist, return null
	 * @param type  e.g. A-P
	 * @return
	 */
	public TransitiveMatrix getTransMat(String type)
	{
		if(transMats.containsKey(type))
			return transMats.get(type);
		else
			return null;
	}
	
	/**
	 * this method returns the name of a given index
	 * @param entityType
	 * @param index
	 * @return
	 */
	public String getInstanceName(String entityType, int index) {
		return entities.get(entityType).getName(index);
	}

	/**
	 * this method returns the entity index of a given name
	 * @param entityType
	 * @param entityName
	 * @return
	 */
	public int getInstanceIndex(String entityType, String entityName)
	{
		return entities.get(entityType).getIndex(entityName);
	}

	public void outAsStream(String filePath)
			throws FileNotFoundException, IOException
	{
		ObjectOutputStream out;
		out = new ObjectOutputStream(new FileOutputStream(filePath));
		out.writeObject(this);
		out.close();
	}

	public static Data loadData(String modelPath) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		File sFile = new File(modelPath);

		ObjectInputStream in = new ObjectInputStream(new FileInputStream(sFile));
		Data tmp =  (Data) in.readObject();
		in.close();
		return tmp;
	}
	
	public String toString()
	{
		return "entities:\n" + entities.toString() + "\ntransmats:\n" + transMats.toString();
	}
}
