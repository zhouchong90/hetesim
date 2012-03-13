package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * it stores an invertedIndex of a type, i.e. A/P
 * @author zhouchong90
 *
 */
public class InvertedIndex
{
	/**
	 * the invertedIndex of a type.
	 */
	private HashMap<String,HashSet<Integer>> invertedIndex; 
	
	public InvertedIndex()
	{
		
	}
	
	public void insertOneSentence(String sentence, int index)
	{
		
	}
	
	private void insertOneWord(String word, int index)
	{
		
	}
	
	public HashSet<Integer> invertedSearch(ArrayList<String> keyWords)
	{
		return null;
	}
}
