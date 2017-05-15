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

public class CosineBasedPrediction {

	/** below is the data structures will be used in class*/
	private Map<Integer, Map<Integer,Double>> sim_Matrix ;
	private UsersData usersData;
	int neighbourSize=0;
	double simThreshold=0.0;

	/**
	 * This will take the single argument
	 * It is created for accessing other method of class 
	 * because it provides the method which doesn't need 
	 * Neighborhood size and threshold parameter
	 * @param userData
	 */
	public CosineBasedPrediction(UsersData userData)
	{
		usersData=userData;
	}

	/**
	 * The constructor takes three argument and initialize the other argument
	 * @param userData
	 * @param neighbourSize
	 * @param simThreshold
	 */
	public CosineBasedPrediction(UsersData userData, int neighbourSize, double simThreshold)
	{

		sim_Matrix= new HashMap<Integer, Map<Integer,Double>>();
		usersData=userData;
		this.neighbourSize=neighbourSize;
		this.simThreshold=simThreshold;
		if(sim_Matrix.isEmpty())
			getSim_Matrix();
	}


	/**
	 * this method returns the similarity matrix 
	 * which can be accessed outside the class
	 * @return
	 */
	public Map<Integer, Map<Integer, Double>> getSim_Matrix() 
	{	
		if(sim_Matrix.isEmpty())
			return getSimilarityMatrix();
		else
			return sim_Matrix;
	}

	/**
	 * compute the similarity between two users
	 * @param user1Id
	 * 				-- user id of first user
	 * @param user2Id
	 * 				--- user id of second user
	 * @return the similarity between two users
	 */
	public double getCosineSimilarity(int user1Id,int user2Id)
	{
		double numerator = 0;
		double denominator;
		double sum_user1_RatSq = 0;
		double sum_user2_RatSq = 0;
		Map<Integer, Integer> user1_MovieId_Rating = usersData.getUserMoviesData(user1Id);
		Map<Integer, Integer> user2_MovieId_Rating = usersData.getUserMoviesData(user2Id);

		Set<Integer> user1MovieSet= user1_MovieId_Rating.keySet();
		Set<Integer> user2MovieSet=user2_MovieId_Rating.keySet();
		//compute the cross product of moviedId for both the user
		Set <Integer> intersection = new HashSet<Integer>(user1MovieSet);
		intersection.retainAll(user2MovieSet);

		if(intersection.isEmpty())  //user are not similar
			return 0;

		for(Integer movieId:intersection)
			numerator+=user1_MovieId_Rating.get(movieId) * user2_MovieId_Rating.get(movieId);

		for(Integer movieId:user1MovieSet)
		{
			double rat1=user1_MovieId_Rating.get(movieId);
			sum_user1_RatSq+=rat1*rat1;
		}

		for(Integer movieId:user2MovieSet)
		{
			double rat2=user2_MovieId_Rating.get(movieId);
			sum_user2_RatSq+=rat2*rat2;
		}

		denominator=Math.sqrt(sum_user1_RatSq*sum_user2_RatSq);
		return numerator/denominator;

	}


	/**
	 * This method build the similarity matrix between users
	 * stores the information user with associated similar user, rating
	 * @return
	 */
	private Map<Integer, Map<Integer, Double>> getSimilarityMatrix()
	{

		double cosSim=0;
		Map <Integer, Double> userSimMatrix;

		for(int candUser: usersData.getUserList())
		{
			userSimMatrix= new HashMap<Integer, Double>();
			for(int tarUser:usersData.getUserList())
			{
				if(candUser!=tarUser)
				{
					cosSim=getCosineSimilarity(candUser,tarUser);
					userSimMatrix.put(tarUser, cosSim);	
				}

			}

			userSimMatrix=sortByValue(userSimMatrix);
			sim_Matrix.put(candUser, userSimMatrix);
		}

		return sim_Matrix;
	}


	/**
	 * this method return the top N users with their similarity value
	 * either they co-rated the same movie or not
	 * @param candidate Id
	 * 				-- this is the id of users which neighbors to be find
	 * @return 
	 */
	public Map<Integer, Double> getTopNSimilarUsers(int candidate,int topN)
	{
		Map <Integer, Double> targetUser_Sim = new LinkedHashMap<Integer, Double>();
		int count=0;
		if(sim_Matrix.containsKey(candidate))
		{
			for(int targetUser:sim_Matrix.get(candidate).keySet())
			{
				if(count==topN)
					break;
				if(count<topN &&targetUser!=candidate)
				{
					targetUser_Sim.put(targetUser, sim_Matrix.get(candidate).get(targetUser));
					count++;
				}
			}
		}
		return targetUser_Sim;
	}



	/**
	 * This method will return the top N users who co-rated the same movie item
	 * and they also satisfy the similarity threshold condition
	 * @param candidate
	 * @param movieId
	 * @param topN
	 * @return
	 */
	public Map<Integer, Double> getTopNCorrSimilarUsers(int candidate,int movieId)
	{
		Map <Integer, Double> targetUser_Sim = new LinkedHashMap<Integer, Double>();
		int count=0;
		
		if(neighbourSize<1 || simThreshold<0)
			return targetUser_Sim;
		
		if(sim_Matrix.containsKey(candidate))
		{
			for(int targetUser:sim_Matrix.get(candidate).keySet())
			{
				if(count==neighbourSize)
					break;
				if(count<neighbourSize &&targetUser!=candidate && 
						usersData.getUserMoviesData(targetUser).containsKey(movieId) 
						&& sim_Matrix.get(candidate).get(targetUser)>simThreshold)
				{
					targetUser_Sim.put(targetUser, sim_Matrix.get(candidate).get(targetUser));
					count++;
				}
			}
		}
		return targetUser_Sim;
	}

	/**
	 * Similarity of co-rated similar user considered as weight, which is multiplied
	 * with actual rating of movie and this is predicted sum which is divide by similarity 
	 * sum of co-rated users  
	 * @param userId
	 * @param movieId
	 * @return
	 */
	public double getPrediction(int userId, int movieId)
	{

		double weight=0;
		double predicted_sum=0;
		double sim_weight_Sum=0;
		Map <Integer, Double> corratedSimUsers = getTopNCorrSimilarUsers(userId, movieId);
		if(corratedSimUsers.isEmpty())
			return 0;

		for(int targetUser :corratedSimUsers.keySet())
		{
			weight= corratedSimUsers.get(targetUser);
			predicted_sum+=weight*usersData.getActualMovieRating(targetUser, movieId);
			sim_weight_Sum+=weight;
		}

		return predicted_sum/sim_weight_Sum;
	}

	//https://www.mkyong.com/java/how-to-sort-a-map-in-java/
	/**
	 * @param unsortMap
	 * @return this method will take unsorted values map 
	 * 			and returns the sorted map which is sorted
	 * 			by values in decreasing order
	 * 			the idea is taken from above link which is modified little bit. 
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
