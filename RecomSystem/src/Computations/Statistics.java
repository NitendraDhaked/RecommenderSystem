package Computations;

import java.util.Collections;
import java.util.List;

public class Statistics {
	
	/**
	 * return mean rating from list of ratings
	 * @param list
	 * @return
	 */

	
	public static double getMean(List<Integer> list)
	{
		double sum=0.0;
		for(double rating:list)
		{
			sum+=rating;
		}
		return sum/list.size();
	}
	
	/**
	 * return median from list of ratings
	 * @param ratings
	 * @return
	 */
	public static double getMedian(List<Double> ratings)
	{
		int midle=0;
		Collections.sort(ratings);
		if(ratings.size()%2!=0)
		{
			midle=(ratings.size()+1)/2;
			return ratings.get(midle-1);
		}
		else
		{
			midle=ratings.size()/2;
			return ((double)ratings.get(midle-1)+(double)ratings.get(midle))/2;
		}
	}
	
	/**
	 * return standard deviation from list of ratings
	 * @param ratings
	 * @return
	 */
	public static double getStdDeviation(List<Integer> ratings)
	{
		return Math.sqrt(getVariance(ratings));
	}
	
	/**
	 * calculate variance from list of ratings
	 * @param ratings
	 * @return
	 */
	public static double getVariance(List<Integer> ratings)
	{
		double mean = getMean(ratings);
		double temp = 0.0;
		for(double rating:ratings)
			temp += (rating-mean)*(rating-mean);
		return temp/ratings.size();
	}
	
	/**
	 * return minimum rating from list of ratings
	 * @param ratings
	 * @return
	 */
	public static double getMinRating(List<Double> ratings)
	{
		double minRating=5.0;
		for(double rating:ratings)
		{
			if(rating<minRating)
			{
				minRating=rating;
			}
		}
		return minRating;
	}
	
	/**
	 * 
	 * return maxRating from list of ratings
	 * @param ratings
	 * @return
	 */
	public static double getMaxRating(List<Double> ratings)
	{
		double maxRating=0.0;
		for(double rating:ratings)
		{
			if(rating>maxRating)
			{
				maxRating=rating;
			}
		}
		return maxRating;
	}

}
