package toolKit;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Pre-process Strings
 * @author zhouchong90
 *
 */
public class ToolKit
{
	/**
	 * divide a sentence into a buffer of words.
	 * @param sentance
	 * @return
	 */
	public static ArrayList<String> sentence2Words(String sentence)
	{
		String[] tmp = sentence.split("\\W");
		ArrayList<String> result = new ArrayList<String>();
		for(String str:tmp)
		{
			if(str.length()>0)
			{
				result.add(str);
			}
		}
		return result;
	}
	
	public static HashSet<Integer> intersection (HashSet<Integer> set1, HashSet<Integer> set2)
	{
		return set2;
		
	}
}
