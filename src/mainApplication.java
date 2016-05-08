import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.util.Random;

public class mainApplication {
    public static String[] csvFiles = {"regression-0.05.csv", "regression-A.csv", "regression-B.csv", "regression-C.csv"};

    public static void main(String[] args) throws IOException {
        // Used to find random 10% of data file
        double percent = 0.90;
        Random rnd = new Random();

        // Seed RNG
        rnd.setSeed(0);

        // Load csv data into array
        Observation obs = new Observation();
        double[][] obsArray = obs.csvToArray(csvFiles[0]);
        //obs.printArr(obsArray);

        double[][] X_train = null;
        double[][] X_test = null;
        double[][] Y_train = null;
        double[][] Y_test = null;

        sample sampleX = new sample(X_train, X_test);
        sample sampleY = new sample(Y_train, Y_test);
        sampleY.getYData(obsArray);
        double [][] yData = sampleY.ySet;

        sampleX.findSample(rnd, obsArray, percent, true);
        sampleY.findSample(rnd, yData, percent, false);

        LinearRegression LR = new LinearRegression();
        SimpleMatrix x_train_matrix = new SimpleMatrix(sampleX.train);
        SimpleMatrix y_train_matrix = new SimpleMatrix(sampleY.train);
        LR.qr_decompose(x_train_matrix);
        LR.back_solve(y_train_matrix, LR.rDiag);
        System.out.println(LR.RMSE(sampleX.test, sampleY.test, LR.Beta));
/*
        double[][] ex = { {1, -1, -1},
                          {1, 2, 3},
                          {2, 1, 1},
                          {2, -2, 1},
                          {3, 2, 1} };

        SimpleMatrix X = new SimpleMatrix(ex);
        LR.qr_decompose(X);

        SimpleMatrix y = new SimpleMatrix(X);

        double[][] upperR = { {1, 2, 1}, {0, 1, 2}, {0, 0, 1} };
        double[][] Y = {{1}, {2}, {3}};

        SimpleMatrix UR = new SimpleMatrix(upperR);
        SimpleMatrix YY = new SimpleMatrix(Y);
        //System.out.println("Qacc is " + LR.Qacc);
        LR.back_solve(YY, UR);
        //System.out.println(LR.RMSE(upperR, Y, LR.Beta));

        //LR.back_solve(LR.Qacc.transpose().mult(YY), LR.rDiag); */
    }
}	
	