package io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.XMLReader;

public abstract class LoadXML extends LoadData
{

	protected List<String>	entityPaths;
	protected List<String>	relationPaths;
	private String			inputPath;
	
	public LoadXML(String XMLPath)
	{
		inputPath = XMLPath;
		entityPaths = new ArrayList<String>();
		relationPaths = new ArrayList<String>();
		getFiles();
	}
	
	private void getFiles()
	{
		XMLReader xmlReader = new XMLReader(inputPath);
		
	}	
}
