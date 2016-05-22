import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class mainApplication {
    public static String[] csvFiles = {"abalone.csv"};
    public static int[] k_vals = {1, 2, 4, 8, 16};

    public static void main(String[] args) throws IOException {
        // Used to find random 80% of data file
        double percent = 0.90;
        Random rnd = new Random();
        int k = 3;

        // Seed RNG
        rnd.setSeed(0);

        Observation obs = new Observation();
        double[][] obsArray;

        // Z-scale and convert csv to 2D Array
        sample s = new sample();
        obsArray = obs.abaloneData(csvFiles[0]);

        // Gather the training and test dataset
        s.findSample(rnd, obsArray, percent, true);

        for(int j = 0; j < k_vals.length; j++) {
            // Calculate the cluster's on the training set
            KMeans XTrain_km = new KMeans(s.xtrain, k_vals[j]);
            KMeans YTrain_km = new KMeans(s.ytrain, k_vals[j]);
            KMeans XTest_km = new KMeans(s.xtest, k_vals[j]);
            KMeans YTest_km = new KMeans(s.ytest, k_vals[j]);

            XTrain_km.kmeans(s.xtrain, k_vals[j]);
            YTrain_km.kmeans(s.ytrain, k_vals[j]);
            XTest_km.kmeans(s.xtest, k_vals[j]);
            YTest_km.kmeans(s.ytest, k_vals[j]);

            double [][] xtrain_clusters;
            double [][] ytrain_clusters;
            double [][] xtest_clusters = null;
            double [][] ytest_clusters;

            List<Cluster> clusters_XTrain;
            List<Cluster> clusters_YTrain;
            List<Cluster> clusters_XTest = null;
            List<Cluster> clusters_YTest;

            double totalRMSE = 0.0;
            System.out.println("K VALUE IS " + k_vals[j] + " : ");

            for(int i = 0; i < k_vals[j]; i++) {
                clusters_XTrain = XTrain_km.getCluster();
                clusters_YTrain = YTrain_km.getCluster();
                clusters_XTest = XTest_km.getCluster();
                clusters_YTest = YTest_km.getCluster();

                xtrain_clusters = XTrain_km.convertListTo2D(clusters_XTrain.get(i).points);
                ytrain_clusters = YTrain_km.convertListTo2D(clusters_YTrain.get(i).points);
                xtest_clusters = XTest_km.convertListTo2D(clusters_XTest.get(i).points);
                ytest_clusters = YTest_km.convertListTo2D(clusters_YTest.get(i).points);

                // For each cluster, calculate a QR decomposition and associate
                // it with the cluster
                LinearRegression LRTrain = new LinearRegression();
                SimpleMatrix x_train_matrix = new SimpleMatrix(xtrain_clusters);
                SimpleMatrix y_train_matrix = new SimpleMatrix(ytrain_clusters);
                SimpleMatrix x_test_matrix = new SimpleMatrix(xtest_clusters);
                SimpleMatrix y_test_matrix = new SimpleMatrix(ytest_clusters);

                // 1. QR Decomposition
                LRTrain.qr_decompose(x_train_matrix);

                SimpleMatrix QTY = LRTrain.Qacc.transpose().mult(y_train_matrix);

                // 2. Back Solve
                LRTrain.back_solve(QTY, LRTrain.rDiag);

                double rmse = LRTrain.RMSE(xtest_clusters, ytest_clusters, LRTrain.Beta);
                totalRMSE += rmse;

                System.out.println("RMSE for each cluster " + i + " : " + rmse);
                System.out.print("Centroids are: ");
                double [] centroids = clusters_XTest.get(i).getCentroid();
                for(int cent = 0; cent < centroids.length; cent++) {
                    System.out.print(centroids[cent] + " ");
                }
                System.out.println();
                System.out.println("WCSS: " + XTest_km.wcss(xtest_clusters, clusters_XTest.get(i).getCentroid()));
                clusters_XTest.get(i).calMeanAndStd();
                System.out.println("MEAN: " + clusters_XTest.get(i).getMean());
                System.out.println("STD: " + clusters_XTest.get(i).getStd());
            }

            System.out.println("Total RMSE: " + totalRMSE + "\n\n");
        }
    }
}	
	