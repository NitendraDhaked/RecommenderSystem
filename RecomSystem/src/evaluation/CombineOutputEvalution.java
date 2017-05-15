package evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import Computations.CosineBasedPrediction;
import Computations.MeanItemRatingL1O;
import Computations.ResnickPrediction;
import DataStructures.UsersData;

public class CombineOutputEvalution {
	
	
	private UsersData userData;
	//const
	String OUTPUT_FILE_HEADER = "user_id,movie_id,actual_rating,predicted_rating,RMSE"+"\n";
	int neighbourSize=0;
	public CombineOutputEvalution(UsersData usersData, int neighbourSize)
	{
		this.userData=usersData;
		this.neighbourSize=neighbourSize;
	}
	
	/**
	 * This Method predict the all 3 methods
	 * 
	 * @param meanPredRat
	 * 					-- Neighborhood size for resnick and cosine cased prediction
	 * @param cosBaseRatPre
	 * 					-- Similarity threshold what is the minimum similarity needed to perform experiment
	 * @param resRatPre 
	 * @throws IOException
	 */
	public void combinePredictionTest(MeanItemRatingL1O meanPredRat, CosineBasedPrediction cosBaseRatPre, ResnickPrediction resRatPre)
	{
		
		double start_Time, end_Time;
		start_Time = System.currentTimeMillis();
		
		//constant for mean prediction
		double sumMeanBasedRMSE=0;
		int meanPredictCount=1;
		
		//constant for cos prediction
		double sumCosBasedRMSE=0;
		int cosPredictCount=1;
		
		//constant for resnick prediction
		double sumresNickBasedRMSE=0;
		int resnickPredictCount=1;
		
		//ouput writers for 3 different experiment
		Writer meanwrite=null;
		Writer coswrite=null;
		Writer reswrite=null;
		try {
			
			for(int i=0; i<10;i++)
			{
				//constant for mean prediction
				sumMeanBasedRMSE=0;
				meanPredictCount=1;
				//constant for cos prediction
				sumCosBasedRMSE=0;
				cosPredictCount=1;
				//constant for resnick prediction
				sumresNickBasedRMSE=0;
				resnickPredictCount=1;
				
				
			//output object with file name is initialized for all 3 test
			meanwrite = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("CombineTestOut"+File.separator+"task_2_Predictions_C.csv"), "utf-8"));
			coswrite = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("CombineTestOut"+File.separator+"task_3_Predictions_C_"+neighbourSize+".csv"), "utf-8"));
			reswrite = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("CombineTestOut"+File.separator+"task_4_Predictions_C_"+neighbourSize+".csv"), "utf-8"));
			
			//file header added
			meanwrite.write(OUTPUT_FILE_HEADER);
			coswrite.write(OUTPUT_FILE_HEADER);
			reswrite.write(OUTPUT_FILE_HEADER);
			
			//test started
			for (int userId : userData.getUserList()) {
				for (int movieId : userData.getUserMoviesData(userId).keySet())
				{
					/* Mean Based prediction and rmse */
					int actualRating=userData.getActualMovieRating(userId, movieId);
					Double meanBasedPredRat =meanPredRat.getPrediction(userId,movieId);
					if(meanBasedPredRat>=1)
					{
						double meanError=Math.abs(meanBasedPredRat-actualRating);
						double meanBasedPredRMSE=meanError*meanError;
						sumMeanBasedRMSE+=meanBasedPredRMSE;
						meanwrite.write(userId+","+movieId+","+actualRating+","+meanBasedPredRat+","+meanBasedPredRMSE+"\n");
						meanPredictCount++;
					}
					
					/* cosine based prediction and rmse */
					Double cosBasedPredRat= cosBaseRatPre.getPrediction(userId, movieId);
					if(cosBasedPredRat>=1 )
					{	
						double merror=Math.abs(cosBasedPredRat-actualRating);
						double cosBasePredRMSE=merror*merror;
						sumCosBasedRMSE+=cosBasePredRMSE;
						coswrite.write(userId+","+movieId+","+actualRating+","+cosBasedPredRat+","+cosBasePredRMSE+"\n");
						cosPredictCount++;
					}
					
					// rsnick pearson correlation based prediction and rmse 
					Double resNickpredRat =resRatPre.getResnickPrediction(userId, movieId);
					if(resNickpredRat>=1)
					{		
						double me =Math.abs(resNickpredRat-actualRating);
						double resNickRMSE=me*me;
						sumresNickBasedRMSE+=resNickRMSE;
						reswrite.write(userId+","+movieId+","+actualRating+","+resNickpredRat+","+resNickRMSE+"\n");
						resnickPredictCount++;
					}
				}
			}
			
			meanwrite.close();
			coswrite.close();
			reswrite.close();
			}
			//Mean Based Prediction outlines 
			System.out.println("Mean L1O Prediction Average RMSE: " + Math.sqrt(sumMeanBasedRMSE/meanPredictCount));
			System.out.println("Mean L1O Prediction coverage: " + ((double)(meanPredictCount)/userData.getTotalNumberOfRating())*100);
			
			//Cos Based Prediction outlines
			System.out.println("Cosine distance based Average RMSE: " + Math.sqrt(sumCosBasedRMSE/cosPredictCount));
			System.out.println("Cosine distance based coverage: " + ((double)(cosPredictCount)/userData.getTotalNumberOfRating())*100);
			
			//Resnick Based Prediction outlines
			System.out.println("Pearson, Resnick Average RMSE: " + Math.sqrt(sumresNickBasedRMSE/resnickPredictCount));
			System.out.println("Pearson, Resnick coverage: " +((double)(resnickPredictCount)/userData.getTotalNumberOfRating())*100);
			
			end_Time=System.currentTimeMillis();
			System.out.println("Average Run Time:"+((end_Time-start_Time)/10)+" Milli seconds");


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		


}
