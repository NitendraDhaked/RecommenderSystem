package DataStructures;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class UsersData {

	/** Movie range 1 to 5 ex 1/2/3/4/5 */
	private static final int RATING_RANGE=5;
	
	/* total 5 types of ratings and total rating */
	public int [] typeOfRatings;
	
	/**
	 * Integer user Id is associated with key value pair of
	 * MovieId Rating
	 * here userId associated with movieId and rating
	 */
	private Map<Integer, Map<Integer, Integer>> usersDetails ;
	
	/** store movie Id to get total number of movies */
	private HashSet<Integer> movieSet;
	
	/** user mean , standard deviation, , min, max, median, variance stored for constant access */
	private Map<Integer, Double> user_Mean;
	private Map<Integer, List<Integer>> userMinMax;
	private Map<Integer, List<Double>> userVarianceStdDev;
	private Map<Integer, Integer> userMedian;

	/**
	 * This is the constructor which initialize all data structures
	 * and this data structures stores information in small chunks but 
	 * are relevant and can be access in constant time
	 */
	public UsersData()
	{
		usersDetails=new HashMap<Integer, Map<Integer, Integer>>();
		user_Mean= new HashMap<Integer, Double>(); 
		userMinMax =new HashMap<Integer, List<Integer>>();
		userVarianceStdDev = new HashMap<Integer, List<Double>>();
		userMedian =new HashMap<Integer, Integer>();
		movieSet= new HashSet<Integer>();
		typeOfRatings= new int[RATING_RANGE+1];
		loadStatistics();
	}

	/**
	 * This method store user specific details
	 * All movie and associated rating stored for userID as a key
	 * @param userId
	 * @param movieId
	 * @param rating
	 */
	public void addMovieRating(int userId, int movieId, int rating)
	{
		HashMap<Integer, Integer> movieId_Rating = usersDetails.containsKey(userId)?(HashMap<Integer, Integer>) usersDetails.get(userId):new HashMap<Integer, Integer>();

		if(user_Mean.containsKey(userId))
		{
			double oldMean=user_Mean.get(userId);
			int size=usersDetails.get(userId).size();
			user_Mean.put(userId, ((oldMean*size)+(rating))/(size+1));
		}
		else
		{
			user_Mean.put(userId, (double) rating);
		}
		movieId_Rating.put(movieId, rating);
		usersDetails.put(userId, movieId_Rating);

	}

	/**
	 * this method is used to add the movieId in data structures
	 * @param movieId
	 * 
	 */
	public void addMovieSet(int movieId)
	{
		movieSet.add(movieId);
	}

	
	/**
	 * @return
	 * 		-- returns the complete set of movie Id
	 */
	public HashSet<Integer> getMovieSet()
	{
		return movieSet;
	}

	
	/**
	 * for accessing complete set of user details 
	 * outside the class
	 * @return
	 */
	public Map<Integer, Map<Integer, Integer>> getUserDetails()
	{
		return usersDetails;
	}


	/**
	 * @return complete set of users 
	 */
	public Set<Integer> getUserList()
	{
		return usersDetails.keySet();
	}


	/**
	 * return key value pair of movieIds and ratings for userId 
	 * @param userId
	 * @return
	 */
	public Map<Integer, Integer> getUserMoviesData(int userId)
	{
		return usersDetails.get(userId);
	}

	/**
	 * return actual rating for specific movie given by user
	 * @param userId
	 * @param movieId
	 * @return
	 */
	public int getActualMovieRating(int userId,int movieId)
	{
		Integer actualMovRat =usersDetails.get(userId).get(movieId);
		if(actualMovRat!=null)
		{
			return actualMovRat;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * add the ratings in array 
	 * like rating 1 on first place and 2nd second place .... 5th on 5th place
	 * for new added rating is incremented by plus one
	 * ratingType means, it is one star(1) or two star(2) or five star (5)
	 * @param rating
	 */
	public void addTyperating(int ratingType)
	{
		typeOfRatings[ratingType]++;
	}

	/**
	 * return the total number of type rating 
	 * example one star, two star etc.
	 * @param ratingType
	 * @return
	 */
	public int getTotalOfTypeRating(int ratingType)
	{
		return typeOfRatings[ratingType];
	}

	/**
	 * 
	 * calculate total number of ratings
	 * @param typeOfRatings
	 * @return
	 */
	public int getTotalNumberOfRating()
	{
		int totRatings=0;
		for(int i=1; i<typeOfRatings.length;i++)
		{
			totRatings+=typeOfRatings[i];
		}
		return totRatings;

	}
	

	/**
	 * this method is used to load all the useful data structures
	 * which provides useful information, like min, max, deviation 
	 * median etc.
	 */
	public void loadStatistics()
	{
		int minRating=5;
		int maxRating=0;
		double variance=0.0;
		List <Integer> ratList = new ArrayList<Integer>();
		List <Integer>tempMinMax;
		List <Double>tempVarStd;
		for(Integer userId : usersDetails.keySet())
		{
			double temp = 0.0;
			for(int rating:usersDetails.get(userId).values())
			{
				ratList.add(rating);
				temp += (rating-user_Mean.get(userId))*(rating-user_Mean.get(userId));
				if(rating<minRating)
					minRating=rating;
				else
					maxRating=rating;
			}
			variance=temp/usersDetails.get(userId).values().size();
			tempMinMax=new ArrayList<Integer>();
			tempVarStd=new ArrayList<Double>();
			tempMinMax.add(minRating);
			tempMinMax.add(maxRating);
			tempVarStd.add(variance);
			tempVarStd.add(Math.sqrt(variance));
			userMinMax.put(userId,tempMinMax);
			userVarianceStdDev.put(userId, tempVarStd);
			userMedian.put(userId, getMedian(ratList));
		}
	}

	
	
	/**
	 * calculate mean rating of user
	 * @param userId
	 * @return
	 */
	public double getUserMeanRating(int userId)
	{
		return user_Mean.get(userId);
	}
	
	/**
	 * @param userId
	 * @return the minimum rating given by user 
	 */
	public Integer getMinRating(int userId)
	{
		
		return userMinMax.get(userId).get(0);
	}
	
	/**
	 * @param userId
	 * @return the maximum rating given by user
	 */
	public Integer getMaxRating(int userId)
	{
		
		return userMinMax.get(userId).get(1);
	}
	
	/**
	 * @param userId
	 * @return the variance of rating by user
	 */
	public double getVariance(int userId)
	{
		return userVarianceStdDev.get(userId).get(0);
		
	}
	
	/**
	 * @param userId
	 * @return the standard deviation of rating by user
	 */
	public double getStandardDeviation(int userId)
	{
		return userVarianceStdDev.get(userId).get(1);
	}
	
	/**
	 * @param userId
	 * @return the median of rating by specific user 
	 */
	public Integer getMedian(int userId)
	{
		return userMedian.get(userId);
	}
	
	/**
	 * median from list of ratings
	 * @param ratings
	 * @return
	 */
	public Integer getMedian(List<Integer> ratings)
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
			return (ratings.get(midle-1)+ratings.get(midle))/2;
		}
	}
	
	/**
	 * This method returns the density matric of data
	 * @return
	 */
	public double getDensityMetric()
	{	
		return ((double)getTotalNumberOfRating()/(usersDetails.size()*movieSet.size()))*100;
	}


}
