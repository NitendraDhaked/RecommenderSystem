# Introduction: 
The aim of the project is to build Collaborating Filtering System which makes good predictions without having any detailed information about the products, items or services. This system can handle diverse collection of users and products/services and make predictions/ recommendation in real time. 
For building the collaborating filtering system, well known dataset (Movie Lens) is provided. This system will not only generate the predictions but also project design must be able to handle even larger data set. So, evaluating the performance and predictions with less error are the main goals of project. 
First task is to build the data structures that can be easily accessed throughout the project and it should not be bulky and give constant access as much as possible from performance point of view. 
Project is divided in four parts, first part which is design of useful data structures and basic calculation like, mean, median, standard deviation, variance, min, max value, density matrix etc. for any user id. 
Second, third and fourth parts are continuous development of Mean Prediction, Cosine Distance Based Prediction, Resnick Prediction techniques and testing of these prediction techniques respectively. 
All graphs, and experiment data and approaches are further explained in detail. 
Apart from that, I have read white paper by “Pro. Barry Smith” using which Fitem prediction approach is implemented in my solution.  Fitem uses similarity threshold for prediction which strictly restrict the user below threshold. All three implemented methods will also be tested combinedly for checking system robustness and performance. 

# Execution
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
