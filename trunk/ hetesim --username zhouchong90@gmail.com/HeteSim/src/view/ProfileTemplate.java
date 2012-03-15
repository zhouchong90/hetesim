package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ProfileTemplate implements Serializable
{
	private static final long serialVersionUID = 7194759409409099301L;
	/**
	 * 
	 */
	public String type; 
	public ArrayList<String> ProfileList;
	//“‘@∑÷∏Ó, name@path@K
	
	public ProfileTemplate()
	{
		ProfileList = new ArrayList<String>();
	}
	
	public static ProfileTemplate InAsStream(String path) throws IOException, ClassNotFoundException
	{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(path));
		return (ProfileTemplate)in.readObject();
	}
	
	public void OutAsStream(String path) throws FileNotFoundException, IOException
	{
		ObjectOutputStream out;
		
		out = new ObjectOutputStream(new FileOutputStream(path));
		out.writeObject(this);
		out.close();
	}
}
