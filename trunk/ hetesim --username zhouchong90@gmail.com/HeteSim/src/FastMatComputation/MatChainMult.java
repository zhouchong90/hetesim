package FastMatComputation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import model.Data;
import model.TransitiveMatrix;

public class MatChainMult
{
	private LinkedList<TransitiveMatrix> leftMat;
	private LinkedList<TransitiveMatrix> rightMat;
	
	public MatChainMult(Data data, List<String> leftPath, List<String> rightPath, HashMap<String, TransitiveMatrix> tmpMat)
	{
		//this.data = data;
		this.leftMat = new LinkedList<TransitiveMatrix>();
		this.rightMat = new LinkedList<TransitiveMatrix>();
		
		for(String str:leftPath)
		{
			TransitiveMatrix tm;
			if((tm = data.getTransMat(str))!=null)
			{
				leftMat.add(tm);
			}
			else
			{
				leftMat.add(tmpMat.get(str));
			}
		}
		
		for(String str:rightPath)
		{
			TransitiveMatrix tm;
			if((tm = data.getTransMat(str))!=null)
			{
				rightMat.add(tm);
			}
			else
			{
				rightMat.add(tmpMat.get(str));
			}
		}
	}
	
	public TransitiveMatrix quickMult()
	{
		LinkedList<TransitiveMatrix> pattern;
		while((pattern = findMostFrequentPattern())!=null)
		{
			TransitiveMatrix p = computeMultiple(pattern);
			
			modifyMatrixChain(pattern,p);
		}
		
		TransitiveMatrix left = computeMultiple(leftMat);
		TransitiveMatrix rightTmp = computeMultiple(rightMat);
		TransitiveMatrix right = rightTmp.transpose();
		return left.normalizedTimes(right);
	}
	
	private void modifyMatrixChain(LinkedList<TransitiveMatrix> pattern, TransitiveMatrix p)
	{
		for(int i = 0; i <= leftMat.size() - pattern.size(); i++)
		{
			int k=0;
			while(leftMat.get(i+k)==pattern.get(k))
			{
				k++;
				if(k==pattern.size())
				{
					for(k--; k>=0; k--)
					{
						leftMat.remove(i+k);
					}
					leftMat.add(i, p);
					
					break;
				}
			}
		}
		
		for(int i = 0; i <= rightMat.size() - pattern.size(); i++)
		{
			int k=0;
			while(rightMat.get(i+k)==pattern.get(k))
			{
				k++;
				if(k==pattern.size())
				{
					for(k--; k>=0; k--)
					{
						rightMat.remove(i+k);
					}
					rightMat.add(i, p);
					
					break;
				}
			}
		}
	}

	private LinkedList<TransitiveMatrix> findMostFrequentPattern()
	{		
		HashSet<LinkedList<TransitiveMatrix>> patternSet = new HashSet<>();		
		findAllMatrixPairs(patternSet);
		
		ArrayList<LinkedList<TransitiveMatrix>> patterns = new ArrayList<>();
		patterns.addAll(patternSet);
		
		ArrayList<Integer> freq = calFrequency(patterns);
		
		int index = -1;
		int maxFreq = 1;
		for(int i = 0; i < freq.size(); i++)
		{
			if(freq.get(i) > maxFreq)
			{
				index = i;
				maxFreq = freq.get(i);
			}
		}
		
		if(index == -1)
			return null;
		else
			return patterns.get(index);
	}
	
	private ArrayList<Integer> calFrequency(
			ArrayList<LinkedList<TransitiveMatrix>> patterns)
	{
		ArrayList<Integer> freq = new ArrayList<Integer>();
		
		for(LinkedList<TransitiveMatrix> pattern : patterns)
		{
			freq.add(stringFrequency(pattern));
		}
		
		return freq;
	}

	private void findAllMatrixPairs(
			HashSet<LinkedList<TransitiveMatrix>> patternSet)
	{
		for(int i = 0; i < leftMat.size()-1 ; i++)
		{
			LinkedList<TransitiveMatrix> tmp = new LinkedList<TransitiveMatrix>();
			tmp.add(leftMat.get(i));
			tmp.add(leftMat.get(i+1));
			patternSet.add(tmp);//可能有bug,set判断是不是相同..
		}
		
		for(int i = 0; i < rightMat.size()-1 ; i++)
		{
			LinkedList<TransitiveMatrix> tmp = new LinkedList<TransitiveMatrix>();
			tmp.add(rightMat.get(i));
			tmp.add(rightMat.get(i+1));
			patternSet.add(tmp);//可能有bug,set判断是不是相同..
		}
	}

	private TransitiveMatrix computeMultiple(LinkedList<TransitiveMatrix> matChain)
	{
		TransitiveMatrix result = matChain.get(0);
		
		for(int i = 0; i < matChain.size() - 1; i++)
		{
			String secondName = matChain.get(i+1).getMatrixName();
			String matrixName = result.getMatrixName() + secondName.substring(secondName.indexOf("-"));
			
			result = result.times(matChain.get(i+1));
			result.setMatrixName(matrixName);
		}
		
		return result;
	}

	
	private int stringFrequency(LinkedList<TransitiveMatrix> pattern)
	{
		int f = 0;
		for(int i = 0; i <= leftMat.size() - pattern.size(); i++)
		{
			int k=0;
			while(leftMat.get(i+k)==pattern.get(k))
			{
				k++;
				if(k==pattern.size())
				{
					f++;
					i+=k-1;
					break;
				}
			}
		}
		
		for(int i = 0; i <= rightMat.size() - pattern.size(); i++)
		{
			int k=0;
			while(rightMat.get(i+k)==pattern.get(k))
			{
				k++;
				if(k==pattern.size())
				{
					f++;
					i+=k-1;
					break;
				}
			}
		}
		
		return f;
	}
}
