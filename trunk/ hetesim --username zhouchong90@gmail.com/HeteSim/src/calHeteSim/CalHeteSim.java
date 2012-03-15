package calHeteSim;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.Data;
import model.TransitiveMatrix;

/**
 * this class calculate the HeteSim value with specifying a, b and path
 * 
 * @author zhouchong90
 * 
 */
public class CalHeteSim
{
	/**
	 * the middle matrix of A-* and P-*
	 */
	private HashMap<String, TransitiveMatrix>	tmpMat;
	/**
	 * it shows whether this path needs a middle matrix
	 */
	private boolean								isEven;

	private Data								data;

	private String								HeteSimPath;

	private List<String>						leftPath, rightPath;

	private int									PathLength;

	/**
	 * 
	 * @param data
	 * @param HeteSimPath
	 *            A,P,V,C
	 */
	public CalHeteSim(Data data, String HeteSimPath)
	{
		tmpMat = null;
		this.data = data;
		this.HeteSimPath = HeteSimPath;
		dealSelf();
		isEven = isEven();

		this.PathLength = HeteSimPath.split(",").length;

		// 分解路径
		if (PathLength > 2)
		{
			List<List<String>> decomposedPath = this.decompose(HeteSimPath);
			leftPath = decomposedPath.get(0);
			rightPath = decomposedPath.get(1);
		}
	}

	private boolean isEven()
	{
		return HeteSimPath.split(",").length % 2 != 0;
	}

	/**
	 * it calculates the HeteSim value from two instance of entities. i.e. APVC,
	 * HanJiaWei, KDD ; it returns 0.9
	 * 
	 * @param data
	 * @param path
	 * @param srcEntityName
	 * @param desEntityName
	 * @return
	 */
	public TransitiveMatrix getHeteSim(String srcEntityName,
			String desEntityName)
	{
		// 获取左右两个posMat(沿着path一路乘过来的乘积)
		String[] tmp = HeteSimPath.split(",");
		int leftIndex = data.getInstanceIndex(tmp[0], srcEntityName);

		int rightIndex = data.getInstanceIndex(tmp[tmp.length - 1],
				desEntityName);

		// 特殊情况处理： A,P
		if (PathLength == 2)
		{
			String tmpPath = tmp[0] + "-" + tmp[1];
			return data.getTransMat(tmpPath).getRowTransMat(leftIndex)
					.getColTransMat(rightIndex);
		}

		TransitiveMatrix left1TransMat = data.getTransMat(leftPath.get(0))
				.getRowTransMat(leftIndex);
		TransitiveMatrix right1TransMat = data.getTransMat(rightPath.get(0))
				.getRowTransMat(rightIndex);

		// 计算乘积
		TransitiveMatrix leftTransMat = computeMultiple(leftPath, left1TransMat);
		TransitiveMatrix rightTransMat = computeMultiple(rightPath,
				right1TransMat).transpose();

		// normalize, 计算cos值
		return leftTransMat.normalizedTimes(rightTransMat);
	}

	/**
	 * it calculates the HeteSim value from an instance and an entity i.e. AVPC,
	 * HanjiaWei; it returns all conferences that HanjiaWei attends and its
	 * HeteSim Values
	 * 
	 * @param data
	 * @param path
	 * @param srcEntityName
	 * @return
	 */
	public TransitiveMatrix getHeteSim(String srcEntityName)
	{
		// 获取左右两个posMat(沿着path一路乘过来的乘积)
		int leftIndex = data.getInstanceIndex(HeteSimPath.split(",")[0],
				srcEntityName);

		// 特殊情况处理： A,P
		if (PathLength == 2)
		{
			String[] tmp = HeteSimPath.split(",");
			String tmpPath = tmp[0] + "-" + tmp[1];
			return data.getTransMat(tmpPath).getRowTransMat(leftIndex);
		}

		TransitiveMatrix left1TransMat = data.getTransMat(leftPath.get(0))
				.getRowTransMat(leftIndex);
		TransitiveMatrix right1TransMat = data.getTransMat(rightPath.get(0));

		// 计算乘积
		TransitiveMatrix leftTransMat = computeMultiple(leftPath, left1TransMat);
		TransitiveMatrix rightTransMat = computeMultiple(rightPath,
				right1TransMat).transpose();

		// normalize, 计算cos值
		return leftTransMat.normalizedTimes(rightTransMat);
	}

	private void dealSelf()
	{
		if (HeteSimPath.split(",").length <= 1)
			throw new IllegalArgumentException("HeteSimPath is too short");
	}

	/**
	 * it calculates the HeteSim value from an all instances of both entities
	 * i.e. AVPC; it returns all conferences that all authors attends and its
	 * HeteSim Values
	 * 
	 * @param data
	 * @param path
	 * @return
	 */
	public TransitiveMatrix getHeteSim()
	{
		// 特殊情况处理： A,P
		if (PathLength == 2)
		{
			String[] tmp = HeteSimPath.split(",");
			String tmpPath = tmp[0] + "-" + tmp[1];
			return data.getTransMat(tmpPath);
		}

		// 获取左右两个posMat(沿着path一路乘过来的乘积)
		TransitiveMatrix left1TransMat = data.getTransMat(leftPath.get(0));
		TransitiveMatrix right1TransMat = data.getTransMat(rightPath.get(0));

		long start = System.currentTimeMillis();
		// 计算乘积
		TransitiveMatrix leftTransMat = computeMultiple(leftPath, left1TransMat);

		long mid = System.currentTimeMillis();
		System.out.println("left done in:" + (mid - start) / 1000 + "s");

		TransitiveMatrix rightTransMat;
		if (leftPath.equals(rightPath))
		{
			rightTransMat = leftTransMat.transpose();
		}
		else
		{

			rightTransMat = computeMultiple(rightPath, right1TransMat)
					.transpose();
		}
		System.out.println("right done in:"
				+ (System.currentTimeMillis() - mid) / 1000 + "s");

		// normalize, 计算cos值
		return leftTransMat.normalizedTimes(rightTransMat);
	}

	/**
	 * it will judge whether the path is even or odd, and assigned values to
	 * hasMiddle, isSelf, and assign values to the middle matrix. And it will
	 * DecomPose the it is called by getHeteSim
	 * 
	 * @param path
	 */
	private List<List<String>> decompose(String path)
	{
		Decompose decom = new Decompose();

		if (isEven)
			return decom.decomposeEven(HeteSimPath);
		else
		{
			tmpMat = new HashMap<String, TransitiveMatrix>();
			return decom.decomposeOdd(data, path, tmpMat);
		}
	}

	/**
	 * it times the Possibility matrix all alone the path the first item on
	 * paths is useless.
	 * 
	 * @param paths
	 * @param firstMat
	 * @return
	 */
	private TransitiveMatrix computeMultiple(List<String> paths,
			TransitiveMatrix firstMat)
	{
		// construct a linked list of TranstiveMatrix on the path
		LinkedList<TransitiveMatrix> list = new LinkedList<TransitiveMatrix>();
		list.addFirst(firstMat);
		for (int i = 1; i < paths.size(); i++)
		{
			TransitiveMatrix nextMat;
			if (data.getTransMat(paths.get(i)) == null)
				nextMat = tmpMat.get(paths.get(i));
			else
				nextMat = data.getTransMat(paths.get(i));
			list.add(nextMat);
		}

		while (list.size() > 1)// 只有一个，不用乘
		{
			TransitiveMatrix result;
			int minSize = Integer.MAX_VALUE;
			int i = 0;
			int minIndex = 0;// the best choice is at minIndex and minIndex+1

			for (; i < list.size() - 1; i++)// 找到最小矩阵的位置
			{
				int size = list.get(i).getRows().size()
						* list.get(i).getCols().size()
						* list.get(i + 1).getCols().size();

				if (size < minSize)
					;
				{
					minSize = size;
					minIndex = i;
				}
			}

			System.out.println("Calculating " + list.get(minIndex).getMatrixName()+"*"+list.get(minIndex + 1).getMatrixName());
			long start = System.currentTimeMillis();
			
			result = list.get(minIndex).times(list.get(minIndex + 1));// 计算结果
			
			System.out.println("Multiplication done in:"
					+ (System.currentTimeMillis()-start)/1000);

			// 调整链表

			list.remove(minIndex + 1);
			list.remove(minIndex);
			list.add(minIndex, result);
		}
		return list.get(0);
	}
}
