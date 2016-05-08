import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.util.Random;

public class mainApplication {
    public static String[] csvFiles = {"regression-0.05.csv", "regression-A.csv", "regression-B.csv", "regression-C.csv", "abalone.csv"};

    public static void main(String[] args) throws IOException {
        // Used to find random 80% of data file
        double percent = 0.80;
        Random rnd = new Random();

        // Seed RNG
        rnd.setSeed(0);

        boolean abalone = false;

        // Load csv data into array
        for(int i = 0; i < csvFiles.length; i++) {
            Observation obs = new Observation();
            double[][] obsArray;

            // Find sample data of X features and Y label
            sample s = new sample();

            // If abalone.csv, need to proxy variables with different function
            if(csvFiles[i].compareTo("abalone.csv") != 0)
                obsArray = obs.csvToArray(csvFiles[i]);
            else {
                obsArray = obs.abaloneData(csvFiles[i]);
                abalone = true;
            }

            s.findSample(rnd, obsArray, percent, abalone);

            // Perform qr_decomposition and back-solving to find RMSE
            LinearRegression LR = new LinearRegression();
            SimpleMatrix x_train_matrix = new SimpleMatrix(s.xtrain);
            SimpleMatrix y_train_matrix = new SimpleMatrix(s.ytrain);

            // 1. QR Decomposition
            LR.qr_decompose(x_train_matrix);

            SimpleMatrix QTY = LR.Qacc.transpose().mult(y_train_matrix);

            // 2. Back Solve
            LR.back_solve(QTY, LR.rDiag);

            // Print out RMSE of each csv file
            System.out.println("CSV FILE: " + csvFiles[i] + ", RMSE: " + LR.RMSE(s.xtest, s.ytest, LR.Beta));
        }


        /* -- example from PA3 guidelines checking if qr() and backsolve() works
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
        LR.back_solve(YY, UR);
        */
    }
}	
	