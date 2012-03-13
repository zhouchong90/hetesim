package calHeteSim;

import model.TransitiveMatrix;
import model.Data;
import model.TransNode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class QuickHeteSim implements Serializable
{

	/**
	 * 
	 */
	private static final long					serialVersionUID	= 1L;
	private HashMap<String, TransitiveMatrix>	preCaledMatrix;

	public QuickHeteSim()
	{
		this.preCaledMatrix = new HashMap<String, TransitiveMatrix>();
	}

	public void preCalPosMat(Data data, String modelPath, String hetePath)
			throws FileNotFoundException, IOException
	{
		CalHeteSim calHete = new CalHeteSim(data, hetePath);
		TransitiveMatrix posMat = calHete.getHeteSim();
		outAsStream(modelPath + "/" + hetePath + ".pm", posMat);
	}
	
	public void preCalPosMat(Data data, String modelPath, String hetePath, int trimSize) throws FileNotFoundException, IOException
	{
		CalHeteSim calHete = new CalHeteSim(data, hetePath);
		TransitiveMatrix posMat = calHete.getHeteSim();
		outAsStream(modelPath + "/" + hetePath + ".pm", posMat.trim(trimSize));
	}

	public static QuickHeteSim loadPosMats(String modelPath)
			throws FileNotFoundException, IOException, ClassNotFoundException
	{
		File dir = new File(modelPath);
		QuickHeteSim tmp = new QuickHeteSim();

		if (!dir.isDirectory())
			throw new IllegalArgumentException("this is not a directory");
		else
		{
			String[] filelist = dir.list();

			for (String file : filelist)
			{
				File singlefile = new File(modelPath + "/" + file);
				if (!singlefile.isDirectory())
				{
					String hetePath = getFileTypeName(file);
					if (!tmp.containsTransitiveMatrix(hetePath))
					{
						ObjectInputStream in = new ObjectInputStream(
								new FileInputStream(singlefile));
						TransitiveMatrix tm = (TransitiveMatrix) in
								.readObject();
						tmp.preCaledMatrix.put(hetePath, tm);
						in.close();
					}
				}
			}
		}

		return tmp;
	}

	private static String getFileTypeName(String path)
	{
		int endIndex = path.lastIndexOf(".");
		int startIndex = Math
				.max(path.lastIndexOf("\\"), path.lastIndexOf("/"));
		startIndex++;
		return path.substring(startIndex, endIndex);
	}

	public TransitiveMatrix getTransitiveMatrix(String heteSimPath)
	{
		if (preCaledMatrix.containsKey(heteSimPath))
			return preCaledMatrix.get(heteSimPath);
		else
			return null;
	}

	public boolean containsTransitiveMatrix(String heteSimpath)
	{
		return preCaledMatrix.containsKey(heteSimpath);
	}

	private void outAsStream(String filePath, TransitiveMatrix transitiveMatrix)
			throws FileNotFoundException, IOException
	{
		ObjectOutputStream out;
		out = new ObjectOutputStream(new FileOutputStream(filePath));
		out.writeObject(transitiveMatrix);
		out.close();
	}
}
