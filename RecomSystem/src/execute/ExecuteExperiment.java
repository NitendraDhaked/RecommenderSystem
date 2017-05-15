package execute;

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
import evaluation.CombineOutputEvalution;
import evaluation.OutPutEvaluation;
import populateData.SrcReader;

public class ExecuteExperiment {

	//for testing purpose only edit two parameters
	private static final int NEIGHBOUR_HOOD_SIZE=100;
	private static final double SIMILARITY_THRESHOLD=0.0;
	private static Writer statwrite;
	
	public static void main(String ...args) throws IOException
	{	
		
		double start_Time, end_Time;
		
		System.out.println("Data Structure Loading Started...");
		//source file Reading
		SrcReader srcRead = new SrcReader();
		File file = new File("input"+File.separator+"100k.dat");
		
		//Data Structure Loading
		srcRead.loadDataStructure(file);
		
		//Data Structure most common used
		UsersData userData =srcRead.getUserData();
		int totalRatings =userData.getTotalNumberOfRating();			
		System.out.println("Data Structure Loading Done");
		
		//Density Metric
			System.out.println("Density Metric value of movie Lens Data Set is: "+userData.getDensityMetric());
		//statistics saving in file, this can be accessed throughout the database
		try {
			userData.loadStatistics();
			String STATISTICS_FILE_HEADER = "user_id,mean,median,standard Deviation, variance,Min Rating, Max Rating"+"\n";
			statwrite = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("task_1_Predictions.csv"), "utf-8"));
			statwrite.write(STATISTICS_FILE_HEADER);
			for(int userId:userData.getUserList() )
			{
				statwrite.write(userId+","+userData.getUserMeanRating(userId)+","
									+userData.getMedian(userId)+","
									+userData.getStandardDeviation(userId)+","
									+userData.getVariance(userId)+","
									+userData.getMinRating(userId)+","+userData.getMaxRating(userId)+"\n");
			}
			System.out.println("Statistics loaded in Statistics.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Three Classes of prediction
		System.out.println("Matrix Creation Started wait...");
		start_Time=System.currentTimeMillis();
		MeanItemRatingL1O meanPredRat= new MeanItemRatingL1O(userData);
		CosineBasedPrediction cosBaseRatPre= new CosineBasedPrediction(userData,NEIGHBOUR_HOOD_SIZE,SIMILARITY_THRESHOLD);
		ResnickPrediction resRatPre = new ResnickPrediction(userData,NEIGHBOUR_HOOD_SIZE,SIMILARITY_THRESHOLD);
		end_Time=System.currentTimeMillis();
		System.out.println("Total Matrix creation Time:"+((end_Time-start_Time)/10)+" Milli seconds");
		System.out.println("##########################******************########################################");
		System.out.println("Prediction Experiment Started");
		//output Indivdual Test
		OutPutEvaluation output = new OutPutEvaluation(userData,totalRatings);	
				
		//Mean Prediction Test
		start_Time = System.currentTimeMillis();
		output.meanL1OpredictionTest(meanPredRat);	
		end_Time=System.currentTimeMillis();
		System.out.println("Average Run Time:"+((end_Time-start_Time)/10)+" Milli seconds"); //divided by 10 because every test run 10 times.
		System.out.println("##########################******************########################################");
		
		//cosine Prediction Test
		start_Time = System.currentTimeMillis();
		output.cosDistBasedpredictionTest(cosBaseRatPre,NEIGHBOUR_HOOD_SIZE,SIMILARITY_THRESHOLD);
		end_Time=System.currentTimeMillis();
		System.out.println("Average Run Time:"+((end_Time-start_Time)/10)+" Milli seconds");
		System.out.println("##########################******************########################################");
		
		//resnick prediction Test
		start_Time = System.currentTimeMillis();
		output.resnickPredictionTest(resRatPre,NEIGHBOUR_HOOD_SIZE,SIMILARITY_THRESHOLD);
		end_Time=System.currentTimeMillis();
		System.out.println("Average Run Time:"+((end_Time-start_Time)/10)+" Milli seconds");
		//output Combine Test
		System.out.println("##########################******************########################################");
		
		//combine output result
		CombineOutputEvalution cmout= new CombineOutputEvalution(userData,NEIGHBOUR_HOOD_SIZE);	
		cmout.combinePredictionTest(meanPredRat,cosBaseRatPre,resRatPre);
		
	}
}
