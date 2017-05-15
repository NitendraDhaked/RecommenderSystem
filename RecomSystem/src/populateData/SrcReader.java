package populateData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import DataStructures.UsersData;

public class SrcReader {
	
	private UsersData userData;
	private Scanner scnr;
	
	/**
	 * loading of data is not done inside the constructor
	 * otherwise you have to pass the file name every time
	 * for loading the userData
	 */
	public SrcReader()
	{
		userData =new UsersData();
	}
	
	
	/**
	 * The purpose of this method to create here 
	 * because UsersData object is loaded by this class
	 * so you don't have to create object again and data will be already
	 * loaded in it
	 * @return the user data object 
	 */
	public UsersData getUserData() {
		return userData;
	}



	/**
	 * this method will load the user data object
	 * @param file 
	 */
	public void loadDataStructure(File file)
	{
		try {
			scnr = new Scanner(file);

			while(scnr.hasNextLine()){
				String line = scnr.nextLine();
				String [] as=line.split(",");
				
				Integer userId=Integer.parseInt(as[0]);
				Integer movieId=Integer.parseInt(as[1]);
				Integer rating=Integer.parseInt(as[2]);
				userData.addMovieRating(userId, movieId, rating);
				userData.addMovieSet(movieId);
				userData.addTyperating(rating);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
