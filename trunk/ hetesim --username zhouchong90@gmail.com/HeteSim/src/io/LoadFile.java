package io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件类型输入的抽象类
 * @author zhouchong90
 *
 */
public abstract class LoadFile extends LoadData
{
	protected List<String>	entityPaths;
	protected List<String>	relationPaths;
	private String			inputPath;
	
	public LoadFile(String inputDirPath)
	{
		inputPath = inputDirPath;
		entityPaths = new ArrayList<String>();
		relationPaths = new ArrayList<String>();
		getFiles();
	}
	
	/**
	 * extract entity files and relation files from the given directory
	 * 
	 * @param inputPath
	 */
	private void getFiles()
	{
		File dir = new File(inputPath);
		if (!dir.isDirectory())
			throw new IllegalArgumentException("this is not a directory");
		else
		{
			String[] filelist = dir.list();

			for (String file : filelist)
			{
				File singlefile = new File(inputPath + "/" + file);
				if (!singlefile.isDirectory())
				{
					if (getFileTypeName(file).contains("-"))
						relationPaths.add(singlefile.getAbsolutePath());
					else
						entityPaths.add(singlefile.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * a file path is read in, the Type of this file is returned. between the
	 * last "/" and "."
	 * 
	 * @param path
	 * @return
	 */
	protected String getFileTypeName(String path)
	{
		int endIndex = path.lastIndexOf(".");
		int startIndex = Math.max(path.lastIndexOf("\\"), path.lastIndexOf("/"));
		startIndex++;
		return path.substring(startIndex,endIndex);
	}
}
