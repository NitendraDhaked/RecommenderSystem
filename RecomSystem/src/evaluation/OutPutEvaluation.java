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

public class OutPutEvaluation {

	private UsersData userData;
	//const
	String OUTPUT_FILE_HEADER = "user_id,movie_id,actual_rating,predicted_rating,RMSE"+"\n";
	public OutPutEvaluation(UsersData usersData, int totalRatings)
	{
		this.userData=usersData;
	}


	/**
	 * @param meanRat
	 * 			-- this predicts the mean item ratiing using leave One out concept
	 */
	public void meanL1OpredictionTest(MeanItemRatingL1O meanRat)
	{
		double sumMeanBasedRMSE=0;
		int meanPredictCount=1;
		Writer meanwrite=null;
		try {

			for(int i=0; i<10;i++)
			{
				sumMeanBasedRMSE=0;
				meanPredictCount=1;

				meanwrite = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("output"+File.separator+"task_2_Predictions.csv"), "utf-8"));
				meanwrite.write(OUTPUT_FILE_HEADER);
				for (int userId : userData.getUserList()) {
					for (int movieId : userData.getUserMoviesData(userId).keySet())
					{
						/* Mean Based prediction and rmse */
						int actualRating=userData.getActualMovieRating(userId, movieId);
						Double meanBasedPredRat =meanRat.getPrediction(userId,movieId);
						if(meanBasedPredRat>0)
						{
							double meanError=Math.abs(meanBasedPredRat-actualRating);
							double meanBasedPredRMSE=meanError*meanError;
							sumMeanBasedRMSE+=meanBasedPredRMSE;
							meanwrite.write(userId+","+movieId+","+actualRating+","+meanBasedPredRat+","+meanBasedPredRMSE+"\n");
							if(meanBasedPredRat>=1)
							meanPredictCount++;
						}
					}
				}
				meanwrite.close();
			}
			//Mean Based Prediction outlines 
			System.out.println("Mean L1O Prediction Average RMSE: " + Math.sqrt(sumMeanBasedRMSE/meanPredictCount));
			System.out.println("Mean L1O Prediction coverage: " + ((double)(meanPredictCount)/userData.getTotalNumberOfRating())*100);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}		


	/**
	 * Calculate prediction using cosine similarity
	 * @param cosBaseRatPre
	 * 					-- Cosinebased prediction class object to get access of prediction formula
	 * @param neighbourSize
	 * @param threshold
	 */
	public void cosDistBasedpredictionTest(CosineBasedPrediction cosBaseRatPre,int neighbourSize, double threshold)
	{

		double sumCosBasedRMSE=0;

		int cosPredictCount=1;
		Writer coswrite=null;
		try {

			for(int i=0;i<10;i++)
			{
				sumCosBasedRMSE=0;
				cosPredictCount=1;

				coswrite = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("output"+File.separator+"task_3_predictions_"+neighbourSize+".csv"), "utf-8"));
				coswrite.write(OUTPUT_FILE_HEADER);
				for (int userId : userData.getUserList()) {
					for (int movieId : userData.getUserMoviesData(userId).keySet())
					{
						int actualRating=userData.getActualMovieRating(userId, movieId);
						/* cosine based prediction and rmse */
						Double cosBasedPredRat= cosBaseRatPre.getPrediction(userId, movieId);
						if(cosBasedPredRat>0 )
						{	
							double merror=Math.abs(cosBasedPredRat-actualRating);
							double cosBasePredRMSE=merror*merror;
							sumCosBasedRMSE+=cosBasePredRMSE;
							coswrite.write(userId+","+movieId+","+actualRating+","+cosBasedPredRat+","+cosBasePredRMSE+"\n");
							if(cosBasedPredRat>=1)
							cosPredictCount++;
						}
					}
				}
				coswrite.close();
			}
			//Cos Based Prediction outlines
			System.out.println("Cosine distance based Average RMSE: " + Math.sqrt(sumCosBasedRMSE/cosPredictCount));
			System.out.println("Cosine distance based coverage: " + ((double)(cosPredictCount)/userData.getTotalNumberOfRating())*100);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
	/**
	 * This method takes three parameter 
	 * @param resRatPre
	 * 				-- resnickPrediction class object, neighbourhood size, similarity threshold
	 * @param neighbourSize
	 * @param threshold
	 */
	public void resnickPredictionTest(ResnickPrediction resRatPre,int neighbourSize, double threshold)
	{
		double sumresNickBasedRMSE=0;
		int resnickPredictCount=1;
		Writer reswrite=null;
		try {
			for(int i=0; i<10;i++)
			{
				sumresNickBasedRMSE=0;
				resnickPredictCount=1;

				reswrite = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("output"+File.separator+"task_4_predictions_"+neighbourSize+".csv"), "utf-8"));
				reswrite.write(OUTPUT_FILE_HEADER);
				for (int userId : userData.getUserList()) {
					for (int movieId : userData.getUserMoviesData(userId).keySet())
					{
						int actualRating=userData.getActualMovieRating(userId, movieId);

						// rsnick pearson correlation based prediction and rmse 
						Double resNickpredRat =resRatPre.getResnickPrediction(userId, movieId);
						if(resNickpredRat>0)
						{		
							double me =Math.abs(resNickpredRat-actualRating);
							double resNickRMSE=me*me;
							sumresNickBasedRMSE+=resNickRMSE;
							reswrite.write(userId+","+movieId+","+actualRating+","+resNickpredRat+","+resNickRMSE+"\n");
							if(resNickpredRat>=1)
							resnickPredictCount++;
						}
					}
				}
				reswrite.close();			
			}
			//Resnick Based Prediction outlines
			System.out.println("Pearson, Resnick Average RMSE: " + Math.sqrt(sumresNickBasedRMSE/resnickPredictCount));
			System.out.println("Pearson, Resnick coverage: " +((double)(resnickPredictCount)/userData.getTotalNumberOfRating())*100);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}
