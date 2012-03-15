package io;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import model.AdjMatrix;
import model.Data;
import model.Entity;
import model.TransitiveMatrix;

/**
 * using all functions defined in this class, user can use interpreted data to
 * construct elements in Data class
 * 
 * @author zhouchong90
 * 
 */
public abstract class LoadData
{
	private Data						data;
	private HashMap<String, AdjMatrix>	relations;

	public HashMap<String, AdjMatrix> getRelations()
	{
		return relations;
	}

	public LoadData()
	{
		relations = new HashMap<String, AdjMatrix>();
	}
	
	/**
	 * load in the Data from a directory of txt files. the name of the txt file
	 * should be uniformed to the Type Name.
	 * 
	 * @param list1View
	 * @param listView
	 * @param progressBar
	 * 
	 * @return
	 * @throws Exception 
	 */
	public Data run() throws IOException
	{
		data = new Data();
		
		loadEntities();

		loadRelations();
		
		constructTransMats();
		
		return data;
	}

	/**
	 * add an instance(1,KDD)to the Entity C
	 * 
	 * @param entityName
	 * @param instanceName
	 * @param index
	 * @return
	 */
	protected void addEntityInstance(String entityName, String instanceName, int index)
	{
		// 新实体
		if (!data.getEntities().containsKey(entityName))
		{
			Entity newEntity = new Entity();
			newEntity.addOneInstance(index, instanceName);
			data.getEntities().put(entityName, newEntity);
		}
		else
		// 旧实体
		{
			data.getEntities().get(entityName).addOneInstance(index, instanceName);
		}
	}

	/**
	 * add instance to relations
	 * 
	 * @param relationName
	 * @param AName
	 * @param BName
	 */
	protected void addRelationInstance(String relationName, String AName,
			String BName)
	{
		// 新实体
		if (!relations.containsKey(relationName))
		{
			String subType[] = relationName.split("-");
			AdjMatrix newAdjMat = new AdjMatrix(data.getEntities()
					.get(subType[0]).size(), data.getEntities().get(subType[1])
					.size());
			newAdjMat.insertOneInstance(data.getEntities().get(subType[0])
					.getIndex(AName), data.getEntities().get(subType[1])
					.getIndex(BName));
			relations.put(relationName, newAdjMat);
		}
		else
		// 旧实体
		{
			String subType[] = relationName.split("-");
			relations.get(relationName).insertOneInstance(
					data.getEntities().get(subType[0]).getIndex(AName),
					data.getEntities().get(subType[1]).getIndex(BName));
		}
	}

	protected void addRelationInstance(String relationName, int AIndex, int BIndex)
	{
		// 新实体
		if (!relations.containsKey(relationName))
		{
			String subType[] = relationName.split("-");
			AdjMatrix newAdjMat = new AdjMatrix(data.getEntities()
					.get(subType[0]).size(), data.getEntities().get(subType[1])
					.size());
			newAdjMat.insertOneInstance(AIndex, BIndex);
			relations.put(relationName, newAdjMat);
		}
		else
		// 旧实体
		{
			relations.get(relationName).insertOneInstance(AIndex,BIndex);
		}
	}

	/**
	 * construct transMats in data, including A-P and P-A
	 */
	protected void constructTransMats()
	{
		for (String relationName : relations.keySet())
		{
			// 对于每个relation,要添加两个transMat
			System.out.println("transforming " + relationName);
			
			TransitiveMatrix newTransMat = new TransitiveMatrix(relations.get(relationName));
			newTransMat.setMatrixName(relationName);
			newTransMat.calAvgDegrees();
			
			data.getTransMats().put(relationName, newTransMat);
			
			String[] tmp = relationName.split("-");
			String transposeName = tmp[1].concat("-").concat(tmp[0]);

			TransitiveMatrix transposeMat = new TransitiveMatrix(relations.get(relationName).transpose());
			transposeMat.setMatrixName(transposeName);
			transposeMat.calAvgDegrees();
			
			data.getTransMats().put(transposeName, transposeMat);
		}
	}

	/**
	 * children should implement these functions.
	 * in both functions, read instances out, and call addEntityInstance or addRelationInstance to construct data.
	 * @throws Exception
	 */
	abstract protected void loadEntities() throws IOException;
	abstract protected void loadRelations() throws IOException;
}
