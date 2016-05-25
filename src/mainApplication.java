import org.ejml.simple.SimpleMatrix;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class mainApplication {
    public static String[] csvFiles = {"abalone.csv"};
    public static int[] k_vals = {1, 2, 4};

    public static void main(String[] args) throws IOException {
        // Used to find random 80% of data file
        double percent = 0.90;
        Random rnd = new Random();
        List<Double> wcss = new ArrayList<Double>();
        List<Double> rmse = new ArrayList<Double>();

        // Seed RNG
        rnd.setSeed(0);
        Observation obs = new Observation();

        // Z-scaled and UnZcaled observation arrays
        sample scaledSample = new sample();
        sample notScaledSample = new sample();
        double [][] scaledObser = obs.abaloneData(csvFiles[0]);
        double [][] notScaledObser = obs.abaloneDataUnzscale(csvFiles[0]);

        // Gather the training and test dataset
        scaledSample.findSample(rnd, scaledObser, percent, true);
        notScaledSample.findSample(rnd, notScaledObser, percent, true);

        List<List<double[]>> prediction = new ArrayList<List<double[]>>();

        for(int j = 0; j < k_vals.length; j++) {
            // Perform KMeans on test and training set
            KMeans XTrain_km = new KMeans(scaledSample.xtrain, scaledSample.ytrain, k_vals[j]);
            XTrain_km.kmeans(k_vals[j]);

//            KMeans XTest_km = new KMeans(scaledSample.xtest, scaledSample.ytest, k_vals[j]);
//            XTest_km.kmeans(k_vals[j]);

            double totalRMSE = 0.0;
            double totalWCSS = 0.0;
            System.out.println("K VALUE IS " + k_vals[j] + " : ");
            System.out.println("Centroids are: ");
            XTrain_km.printArr(XTrain_km.oldCentroids);

            for(int i = 0; i < k_vals[j]; i++) {
//                List<List<double[]>> clusters_XTrain = XTrain_km.xclusterList;
//                double[][] xtrainClus = XTrain_km.convertListTo2D(clusters_XTrain.get(i));
//                List<List<double[]>> clusters_YTrain = XTrain_km.yclusterList;
//                double[][] ytrainClus = XTrain_km.convertListTo2D(clusters_YTrain.get(i));

                List<Cluster> clusters_XTrain = XTrain_km.xclusterList;
                double[][] xtrainClus = XTrain_km.convertListTo2D(clusters_XTrain.get(i).pointList);
                List<Cluster> clusters_YTrain = XTrain_km.yclusterList;
                double[][] ytrainClus = XTrain_km.convertListTo2D(clusters_YTrain.get(i).pointList);

//                List<List<double[]>> clusters_XTest = XTest_km.xclusterList;
//                double[][] xtestClus = XTest_km.convertListTo2D(clusters_XTest.get(i));
//                List<List<double[]>> clusters_YTest = XTest_km.yclusterList;
//                double[][] ytestClus = XTest_km.convertListTo2D(clusters_YTest.get(i));

                // Take the observation and use KNN with k = 1 to find correct cluster and model
//                for(int testPt = 0; testPt < scaledSample.xtest.length; testPt++){
//                    prediction.add(testPt, obs.kNearestNeighbor(scaledSample.xtest[testPt], clusters_XTrain, 1));
//                }

                // For each cluster, calculate a QR decomposition and associate
                // it with the cluster on unscaled data
                LinearRegression LRTrain = new LinearRegression();
                SimpleMatrix x_train_matrix = new SimpleMatrix(xtrainClus);
                SimpleMatrix y_train_matrix = new SimpleMatrix(ytrainClus);

                // 1. QR Decomposition
                LRTrain.qr_decompose(x_train_matrix);

                SimpleMatrix QTY = LRTrain.Qacc.transpose().mult(y_train_matrix);

                // 2. Back Solve
                LRTrain.back_solve(QTY, LRTrain.rDiag);

                double rmse_val = LRTrain.RMSE(notScaledSample.xtest, notScaledSample.ytest, LRTrain.Beta);
                totalRMSE += rmse_val;

                System.out.println("RMSE for each cluster " + i + " : " + rmse_val);
//                List<double[]> centroids = clusters_XTrain.get(i);
//                List<double[]> closeCentroids = prediction.get(i);
                System.out.println();
//                totalWCSS += XTrain_km.wcss(notScaledSample.xtest[i], closeCentroids.get(0));
            }
            wcss.add(totalWCSS);
            rmse.add(totalRMSE);
            System.out.println("Total WCSS: " + totalWCSS);
            System.out.println("Total RMSE: " + totalRMSE + "\n\n");
        }

        // Plot WCSS vs K
        //scaledSample.createChart(wcss, k_vals, "WCSS vs. K");
        //scaledSample.createChart(rmse, k_vals, "RMSE vs. K");
    }
}	
	