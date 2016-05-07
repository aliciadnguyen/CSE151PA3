import org.ejml.simple.SimpleMatrix;

import java.io.IOException;
import java.util.Random;

public class mainApplication {
    public static String[] csvFiles = {"regression-0.05.csv", "regression-A.csv", "regression-B.csv", "regression-C.csv"};

    public static void main(String[] args) throws IOException {
        // Used to find random 10% of data file
        double percent = 0.10;
        Random rnd = new Random();

        // Seed RNG
        rnd.setSeed(0);

        houseHolder house = new houseHolder();

        double[][] ex = { {1, -1, -1},
                          {1, 2, 3},
                          {2, 1, 1},
                          {2, -2, 1},
                          {3, 2, 1} };

        SimpleMatrix X = new SimpleMatrix(ex);
        house.houseHoldersOnX(X);

    }
}	
	