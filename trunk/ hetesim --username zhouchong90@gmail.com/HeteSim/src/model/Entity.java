package model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * it store an entity, and its index
 * i.e. KDD 1
 * @author zhouchong90
 *
 */
public class Entity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, String> index2Name;
	private HashMap<String, Integer> name2Index;
	
	public Entity()
	{
		index2Name = new HashMap<Integer, String>();
		name2Index = new HashMap<String, Integer>();
	}
	
	/**
	 * add a new instance to this entity
	 * @param index
	 * @param name
	 */
	public void addOneInstance(int index, String name)
	{
		if(!index2Name.containsKey(index))
			index2Name.put(index, name);
		if(!name2Index.containsKey(name))
			name2Index.put(name, index);
	}	
	
	/**
	 * giving an index, returns its name
	 * @param entityIndex
	 * @return
	 */
	public String getName(int entityIndex) 
	{
		return index2Name.get(entityIndex);
	}
	
	/**
	 * giving the name , returns its index
	 * @param entityName
	 * @return
	 */
	public int getIndex(String entityName)
	{
		//考虑名字相似, 比如Han jia wei=Jiawei Han
		return name2Index.get(entityName)==null?-1:name2Index.get(entityName);
	}
	
	public boolean containsName(String name)
	{
		return name2Index.containsKey(name);
	}
	
	public int size()
	{
		return index2Name.size();
	}
	
	public String toString()
	{
		return index2Name.toString();
	}
}
