package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.AdjMatrix;

/**
 * using this class, user can specify all lists of inputPaths
 * 
 * @author zhouchong90
 */
public class LoadTxt extends LoadFile
{

	public LoadTxt(String inputDirPath)
	{
		super(inputDirPath);
	}

	/**
	 * implement LoadData, read txt, construct relations
	 */
	protected void loadRelations() throws IOException
	{
		for (String relationPath : relationPaths)
		{
			String relationName = getFileTypeName(relationPath);
			System.out.println("loading "+relationName);
			FileReader fr = new FileReader(relationPath);
			BufferedReader reader = new BufferedReader(fr);

			String line;
			while ((line = reader.readLine()) != null)
			{
				String[] ABnames = line.split(",");
				addRelationInstance(relationName, Integer.parseInt(ABnames[0].trim()),
						Integer.parseInt(ABnames[1].trim()));
			}
			reader.close();
			fr.close();
		}
	}

	/**
	 * implement LoadData, read txt, construct entities
	 */
	protected void loadEntities() throws IOException
	{
		// for entityPaths, 每个文件是一个Entity, 构造data.entities
		for (String entityPath : entityPaths)
		{
			String entityName = getFileTypeName(entityPath);
			System.out.println("loading "+entityName);
			FileReader fr = new FileReader(entityPath);
			BufferedReader reader = new BufferedReader(fr);

			/**
			 * the index of this entity
			 */
			int entityIndex = 1;
			String line;
			while ((line = reader.readLine()) != null)
			{
				addEntityInstance(entityName, line.trim(), entityIndex);
				entityIndex++;
			}

			reader.close();
			fr.close();
		}
	}
}
