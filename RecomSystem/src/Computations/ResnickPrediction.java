package Computations;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import DataStructures.UsersData;

public class ResnickPrediction {

	/**Data structures which is used for Resnick prediction*/
	Map<Integer, Map<Integer, Double>> pearsonMatrix;
	Map <Integer, Double> pearsonSimUsers;
	UsersData usersData;
	private int neighbourSize=0;
	private double simThreshold=0.0;


	/**
	 * class provides the functionality like Pearson correlation coefficient
	 * and top similar users which does not require other parameter like neighborhood 
	 * size and threshold value
	 * @param userData
	 */
	public ResnickPrediction(UsersData userData)
	{
		this.usersData=userData;
	}

	/**
	 * To initilize all the data structures
	 * @param userData
	 * @param neighbourSize
	 * @param similarityThreshold
	 */
	public ResnickPrediction(UsersData userData,int neighbourSize, double similarityThreshold)
	{
		pearsonMatrix= new HashMap<Integer, Map<Integer, Double>>();
		this.usersData=userData;
		this.neighbourSize=neighbourSize;
		this.simThreshold=similarityThreshold;
		if(pearsonMatrix.isEmpty())
			getPearsonSim_Matrix();
	}



	/**
	 * @return the Parson similarity matrix outside the class 
	 */
	public Map<Integer, Map<Integer, Double>> getPearsonSim_Matrix() {

		if(pearsonMatrix.isEmpty())
			return getPearsonMatrix();
		else
			return pearsonMatrix;
	}


	/**
	 * Reuire two users id for computation of pearson coefficient
	 * @param user1Id
	 * @param user2Id
	 * @return the pearson corelation coefficient between two users
	 */
	public double getPearCorrCoeffficient(int user1Id, int user2Id)
	{
		double numerator=0;
		double denominator=0;
		double sum_cr_r1Sq=0;
		double sum_cr_r2Sq=0;
		Map<Integer, Integer> user1_MovieId_Rating = usersData.getUserMoviesData(user1Id);
		Map<Integer, Integer> user2_MovieId_Rating = usersData.getUserMoviesData(user2Id);

		//take the cross product of two users
		Set<Integer> user1MovieSet= user1_MovieId_Rating.keySet();
		Set<Integer> user2MovieSet=user2_MovieId_Rating.keySet();
		//compute the cross product of moviedId for both the user
		Set <Integer> intersection = new HashSet<Integer>(user1MovieSet);
		intersection.retainAll(user2MovieSet);
		if(intersection.isEmpty())
		{
			return -1;
		}
		for(Integer movieId:intersection)
		{

			numerator+= (user1_MovieId_Rating.get(movieId)-usersData.getUserMeanRating(user1Id))*(user2_MovieId_Rating.get(movieId)-usersData.getUserMeanRating(user2Id));

		}
		if(numerator==0.0)
		{
			return 0;
		}

		for(Integer movieId:intersection)
		{
			double temp1=user1_MovieId_Rating.get(movieId)-usersData.getUserMeanRating(user1Id);
			sum_cr_r1Sq+=(temp1*temp1);
			double temp2=user2_MovieId_Rating.get(movieId)-usersData.getUserMeanRating(user2Id);
			sum_cr_r2Sq+=(temp2*temp2);
		}		
		denominator=Math.sqrt(sum_cr_r1Sq)*Math.sqrt(sum_cr_r2Sq);
		return numerator/denominator;	}


	/**
	 * this method is return the mapping of all the similar users
	 * like user x similar to the y, z, A, B...
	 * @return
	 */
	private Map<Integer, Map<Integer, Double>> getPearsonMatrix()
	{
		double pearSim=0;
		Map <Integer, Double> userSimMatrix;
		for(int candUser: usersData.getUserList())
		{
			userSimMatrix= new HashMap<Integer, Double>();
			for(int tarUser:usersData.getUserList())
			{
				pearSim=getPearCorrCoeffficient(candUser,tarUser);
				userSimMatrix.put(tarUser, pearSim);
			}
			pearsonMatrix.put(candUser,sortByValue(userSimMatrix));
		}

		return pearsonMatrix;

	}

	/**
	 * Calculate Top N similars users who may or may not be rated the same movie
	 * @param candidate
	 * 				-- user Id whose similar users to be find
	 * @param topN 
	 * 			--- neighbourhood size
	 * @return the similar userid with their similarity coefficient
	 */
	public Map<Integer, Double> getTopNSimilarUsers(int candidate,int topN)
	{
		Map <Integer, Double> targetUser_Sim = new LinkedHashMap<Integer, Double>();
		int count=0;
		if(pearsonMatrix.containsKey(candidate))
		{
			for(int targetUser:pearsonMatrix.get(candidate).keySet())
			{
				if(count==topN)
					break;	
				if(count<topN &&targetUser!=candidate )
				{
					targetUser_Sim.put(targetUser, pearsonMatrix.get(candidate).get(targetUser));
					count++;
				}
			}
		}
		return targetUser_Sim;
	}


	/**
	 * This method returns top N users who must rated the same movie
	 * and they must pass the similarity threshold cut off
	 * @param candidate
	 * @param movieId
	 * @param topN
	 * @return
	 */
	public Map<Integer, Double> getTopNCoratedSimUsers(int candidate,int movieId)
	{
		Map <Integer, Double> targetUser_Sim = new LinkedHashMap<Integer, Double>();
		int count=0;
		if(neighbourSize<1)
			return targetUser_Sim;
		if(pearsonMatrix.containsKey(candidate))
		{
			for(int targetUser:pearsonMatrix.get(candidate).keySet())
			{
				if(count==neighbourSize)
					break;
				if((count < neighbourSize) && (targetUser!=candidate) 
						&&pearsonMatrix.get(candidate).get(targetUser)>simThreshold 
						&& usersData.getUserMoviesData(targetUser).containsKey(movieId) )
				{
					targetUser_Sim.put(targetUser, pearsonMatrix.get(candidate).get(targetUser));
					count++;
				}
			}
		}
		return targetUser_Sim;
	}


	/**
	 * This is the implementation of resnick prediction formula
	 * @param userId
	 * @param movieId
	 * @return
	 * 		--predicted rating of user for movie id
	 */

	public double getResnickPrediction(int userId, int movieId )
	{

		double pearsonSim=0;
		double pearRat=0;
		double pearRatSum=0;
		double weightPearsonSim=0;
		Map <Integer, Double> corrSimUsers = getTopNCoratedSimUsers(userId, movieId);

		if(corrSimUsers.isEmpty())
			return 0;

		for(int simuserId: corrSimUsers.keySet())
		{
			pearsonSim=corrSimUsers.get(simuserId);
			pearRat= pearsonSim *(usersData.getActualMovieRating(simuserId, movieId) - usersData.getUserMeanRating(simuserId));	
			pearRatSum+=pearRat;
			weightPearsonSim+= Math.abs(pearsonSim);		
		}
		//System.out.println(userDetails.getUserMeanRating(userId)+(pearRatSum/weightPearsonSim)+"::"+pearRatSum+"::"+weightPearsonSim);
		return usersData.getUserMeanRating(userId)+(pearRatSum/weightPearsonSim);

	}

	//https://www.mkyong.com/java/how-to-sort-a-map-in-java/
	/**
	 * @param unsortMap
	 * @return this method will take unsorted values map 
	 * 			and returns the sorted map which is sorted
	 * 			by values in decreasing order
	 * 			the idea is taken from above link which is modified little bit. 
	 *
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> unsortMap) {

		List<Map.Entry<K, V>> list =
				new LinkedList<Map.Entry<K, V>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}

		return result;
	}

}
