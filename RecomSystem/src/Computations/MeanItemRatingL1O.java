package Computations;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import DataStructures.UsersData;

public class MeanItemRatingL1O {


	/** data structures which is used in prediction*/
	UsersData userData;
	private Map<Integer, Map<Integer,Double>> mean_Rat_Matrix;
	
	/**
	 * This is the constructor which initializes all
	 * the data structures
	 * @param userData
	 */
	public MeanItemRatingL1O(UsersData  userData)
	{
		this.userData=userData;
		mean_Rat_Matrix=new HashMap<Integer, Map<Integer,Double>>();
		buid_Predicted_Mean_Matrix();
	}

	/**
	 * calculate mean item rating of user for specific item
	 * this method works on the principal of leave one out strategy
	 * @param userId
	 * @param movieId
	 * @param usersData
	 * @return
	 */
	private double getMeanItemRating(int userId, int movieId)
	{
		//Set<Integer> userList=userData.getUserList();

		double sum=0.0;
		int count=1;
		for(Entry<Integer, Map<Integer, Integer>> userDetails:userData.getUserDetails().entrySet())
		{

			if(userDetails.getKey()!=userId)
			{
				if(userDetails.getValue().containsKey(movieId))
				{
					sum+=userDetails.getValue().get(movieId);
					count++;
				}

			}

		}
		return sum/count;
	}

	
	/**
	 * This methods creates the matrix which stores the predicted value
	 * which is associated to specific user and the movie which is rated by 
	 * this user
	 * @return
	 */
	private Map<Integer, Map<Integer, Double>> buid_Predicted_Mean_Matrix()
	{
		Map <Integer, Double> userMovieMeanMatrix;
		for(int userId: userData.getUserList()) 
		{
			userMovieMeanMatrix = new HashMap<Integer, Double>();
			for (Integer movieId :userData.getUserMoviesData(userId).keySet()) 
			{
					userMovieMeanMatrix.put(movieId, getMeanItemRating(userId, movieId));
			}
			mean_Rat_Matrix.put(userId, userMovieMeanMatrix);
		}
		return mean_Rat_Matrix;
	}

	
	/**
	 * This method returns the predicted mean matrix which 
	 * is accessible outside the class
	 * @return
	 */
	public Map<Integer, Map<Integer, Double>> getMeanRatMatrix()
	{
		return buid_Predicted_Mean_Matrix();
	}
	
	
	/**
	 * To get the prediction of any movie outside the class
	 * @param userId
	 * @param movieId
	 * @return
	 */
	public double getPrediction(int userId, int movieId)
	{
		return mean_Rat_Matrix.get(userId).get(movieId);
	}
}
