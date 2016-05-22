import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class mainApplication {
    public static String[] csvFiles = {"abalone.csv"};

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

        // Calculate the cluster's on the training set
        KMeans X_km = new KMeans(s.xtrain, k);
        KMeans Y_km = new KMeans(s.ytrain, k);

        X_km.kmeans(s.xtrain, k);
        Y_km.kmeans(s.ytrain, k);

        List<Cluster> clusters_X = X_km.getCluster();
        List<Cluster> clusters_Y = Y_km.getCluster();

        double [][] x_clusters = X_km.convertListTo2D(clusters_X.get(0).points);
        double [][] y_clusters = Y_km.convertListTo2D(clusters_Y.get(0).points);

        // For each cluster, calculate a QR decomposition and associate
        // it with the cluster
        LinearRegression LR = new LinearRegression();
        SimpleMatrix x_train_matrix = new SimpleMatrix(x_clusters);
        SimpleMatrix y_train_matrix = new SimpleMatrix(y_clusters);

        // 1. QR Decomposition
        LR.qr_decompose(x_train_matrix);

        SimpleMatrix QTY = LR.Qacc.transpose().mult(y_train_matrix);
        //System.out.println(LR.rDiag);
        //System.out.println(QTY);

        // 2. Back Solve
        LR.back_solve(QTY, LR.rDiag);

    }
}	
	