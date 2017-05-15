Follow the below steps two run this recomender system

1>. import the project in eclipse
Project Explorer > Import > Existing Project Into Workspace

2>. Please chnage only two parameters in ExecuteExperiment.java available in execute java

private static final int NEIGHBOUR_HOOD_SIZE=5;
private static final double SIMILARITY_THRESHOLD=0.0;

3> right click on ExecuteExperiment.java and choose Run on Application to run this project

4>.First parameter is related to neighbour size and second is similarity threshold.

5> Test can be done by changing both parameters.

6> All Test output will present in output directory of project. //After every run refresh the project explorer to find output files.

7> All three approaches also running together, there output is genrated inside CombineTestOut folder

task_1_Predictions.csv is outsite the ouput folder which contains, mean median , standard deviation, min , max etc.

?File Representations for Individual Method test
task_2_Predictions.csv (Mean Prediction Leave One Out)
task_3_Predictions_100.csv  (Cosine Distance based Filtering Approach with 100 neighbourhood Size)
task_4_Predictions_100.csv (Resnick Prediction with 100 neighbourhood size)

?File Representations for combine test
task_2_Predictions.csv (Mean Prediction Leave One Out)
task_2_Predictions_C_100.csv  (Cosine Distance based Filtering Approach with 100 neighbourhood Size)
task_2_Predictions_C_100.csv (Resnick Prediction with 100 neighbourhood size)

8> Output statistics will also display on console.

Package Description:
/Recom
/Recom/src
Computations  //contains all 3 approaches implementation and basic statistics method
			/Recom/src/Computations/CosineBasedPrediction.java
			/Recom/src/Computations/MeanItemRatingL1O.java
			/Recom/src/Computations/ResnickPrediction.java
			/Recom/src/Computations/Statistics.java
DataStructures  //Only one DataStructures will be used throughout the project
			/Recom/src/DataStructures/UsersData.java
evaluation	//prediction evaluation method is present
			/Recom/src/evaluation/CombineOutputEvalution.java
			/Recom/src/evaluation/OutPutEvaluation.java
execute		//System will execute from this package
			/Recom/src/execute/ExecuteExperiment.java
populateData // populate the data structure
			/Recom/populateData/SrcReader.java
			
CombineTestOut //combine test output
			/Recom/CombineTestOut/task_2_Predictions_C.csv
			/Recom/CombineTestOut/task_3_Predictions_C_100.csv
			/Recom/CombineTestOut/task_4_Predictions_C_100.csv
/Recom/input  //input file folder
			/Recom/input/100k.dat
/Recom/output //output of the approach which runs individually
			/Recom/output/task_2_Predictions.csv
			/Recom/output/task_3_predictions_100.csv
			/Recom/output/task_4_predictions_100.csv
/Recom/task_1_Predictions.csv   //task1 basic calculation like mean median std deviation
Contains Implemntation of all three Prediction Approach and statistics
