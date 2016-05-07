import java.io.*;
import java.util.*;

public class sample {
    double [][] test;
    double [][] train;

    public sample(double[][] test, double[][] train) {
        this.test = test;
        this.train = train;
    }

    /**
     * Function: 	getDataSize()
     * Parameters: 	String csv --	the name of the CSV file
     * Description:	Finds the number of rows (data) in the CSV file by reading
     * 		each row.
     * Returns:	The data size of the CSV file
     */
    public int getDataSize(String csv) throws IOException {
        // Read in CSV file
        InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        // Get dataSize from CSV file
        int dataSize = 0;
        String row = reader.readLine();
        while(row != null) {
            dataSize++;
            row = reader.readLine();
        }

        reader.close();
        return dataSize;
    }


    /**
     * Function: 	findSample()
     * Parameters: 			rnd	-- random number generated from RNG
     * 				totalSample	-- 2D array that holds all data from
     * 							   csv files
     * 					percent -- percentages of what to sample (eg. 10)
     * Description:	Finds a specified percentage (eg. 10%) of the total
     * 		data in the CSV file and marks if the index has occurred
     * 		based on the random number being less than the threshold.
     * 		Updates the threshold value based on Nn (number needed to
     * 		complete sample) and Nr (Number of elements left to be
     * 		tested)
     * Returns:	the test and training sets of the dataset files
     */
    public sample findSample(Random rnd, double[][] totalSample, double percent) {

        // Set the sample size to 10%
        double size = totalSample.length;
        double sampleSize = size * percent;

        int cols = totalSample[0].length;

        test = new double[(int)sampleSize][cols];
        train = new double[(int)(size - sampleSize)][cols];

        // Number of selected items
        double count = 0;
        double thres;

        int trainI = 0;
        int testI = 0;

        for(int i = 0; i < size - 1; i++) {
            thres = (sampleSize - count)/(size - i);
            if(rnd.nextDouble() < thres){
                count++;
                for(int j = 0; j < totalSample[i].length; j++) {
                    test[testI][j] = totalSample[i][j];
                }
                testI++;
            }
            else {
                for(int j = 0; j < totalSample[i].length; j++) {
                    train[trainI][j] = totalSample[i][j];
                }
                trainI++;
            }
        }

        sample s = new sample(test, train);
        return s;
    }
}
