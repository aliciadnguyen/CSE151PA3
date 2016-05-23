import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Alicia on 5/4/2016.
 */
public class Observation {

    /**
     * Function:	abaloneData()
     * Description:	Reads in the abalone.csv file and converts the gender files
     * 				making proxy variables (M, F, I). Stores all the data from
     * 				csv file into a 2D array. Then swaps the 2D array's rows and
     * 				columns in order to perform z-scaling on each features (by
     * 				finding the mean and std of each column and normalizing the
     * 				values). Then it swaps back the 2D array again to be in its
     * 				original format with updated z-scaled numeric values.
     * Parameters:	csv 	-- file to gather data from
     * 				count 	-- number of observations in datafile
     * Returns:		2D array of updated z-scaled values
     */
    public double[][] abaloneData(String csv) throws IOException {
        double[][] abaObser = null;
        double[] obArray;

        // Read in CSV file
        InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String csvRow = null;
        int j = 0;

        sample s = new sample();
        int count = s.getDataSize(csv);

        abaObser = new double[count][11];

        // Put all the data of csv file into a 2D array
        while((csvRow = reader.readLine()) != null) {
            int i = 3;
            obArray = new double[11];
            String[] observation = csvRow.split(",");
            for(String feature : observation) {
                if(feature.equals("M"))
                    obArray[0] = 1;
                else if(feature.equals("F"))
                    obArray[1] = 1;
                else if(feature.equals("I"))
                    obArray[2] = 1;
                else
                    obArray[i++] = Double.parseDouble(feature);
            }
            abaObser[j] = obArray;
            j++;
        }

        // Swapping columns and rows
        double swap[][] = swapArr(abaObser);
        double scaleObs[][] = new double [abaObser[0].length][abaObser.length];

        // Perform z-scaling on swap array on index 3 to preserve M, F, I cols
        int sR = 3;
        for(; sR < swap.length-1; sR++){
            scaleObs[sR] = zScale(swap[sR]);
        }

        scaleObs[0] = swap[0];
        scaleObs[1] = swap[1];
        scaleObs[2] = swap[2];
        scaleObs[sR] = swap[sR];

        double[][] scaleArr = swapArr(scaleObs);

        reader.close();
        return scaleArr;
    }

    public double[][] abaloneDataUnzscale(String csv) throws IOException {
        double[][] abaObser = null;
        double[] obArray;

        // Read in CSV file
        InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String csvRow = null;
        int j = 0;

        sample s = new sample();
        int count = s.getDataSize(csv);

        abaObser = new double[count][11];

        // Put all the data of csv file into a 2D array
        while((csvRow = reader.readLine()) != null) {
            int i = 3;
            obArray = new double[11];
            String[] observation = csvRow.split(",");
            for(String feature : observation) {
                if(feature.equals("M"))
                    obArray[0] = 1;
                else if(feature.equals("F"))
                    obArray[1] = 1;
                else if(feature.equals("I"))
                    obArray[2] = 1;
                else
                    obArray[i++] = Double.parseDouble(feature);
            }
            abaObser[j] = obArray;
            j++;
        }

        reader.close();
        return abaObser;
    }


    /**
     * Function:	csvToArray()
     * Description:	Reads in the error rates files. Stores all the data from
     * 				csv file into a 2D array. Then swaps the 2D array's rows and
     * 				columns in order to perform z-scaling on each features (by
     * 				finding the mean and std of each column and normalizing the
     * 				values). Then it swaps back the 2D array again to be in its
     * 				original format with updated z-scaled numeric values.
     * Parameters:	csv 	-- file to gather data from
     * Returns:		2D array of updated z-scaled values
     */
    public double[][] csvToArray(String csv) throws IOException {
        double[][] obserVectors = null;
        double[] obArray;

        // Sample - 10% Test data and 90% Training Data
        double [][] testData = null;
        double [][] trainData = null;

        sample s = new sample();
        int count = s.getDataSize(csv);

        // Read in CSV file
        InputStream in = sample.class.getClassLoader().getResourceAsStream(csv);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String csvRow = null;
        int j = 0;

        obserVectors = new double[count][31];

        // Put all the data of csv file into a 2D array
        while((csvRow = reader.readLine()) != null) {
            int i = 0;
            obArray = new double[31];
            String[] observation = csvRow.split(",");
            for(String feature : observation) {
                obArray[i++] = Double.parseDouble(feature);
            }
            obserVectors[j] = obArray;
            j++;
        }

        reader.close();
        return obserVectors;
    }


    /**
     * Function:	printArr
     * Description:	Print the array in better format
     * Parameters:	2D Array
     * Returns:		Void
     */
    public void printArr(double [][] arr){
        for(int r = 0; r < arr.length; r++) {
            for(int c = 0; c < arr[r].length; c++) {
                System.out.print(arr[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    /**
     * Function:	swapArr
     * Description:	Swaps the rows and columns of the array
     * Parameters:	2D Array
     * Returns:		2D Array with rows and columns switched
     */
    public double[][] swapArr(double[][] arr) {
        // Swapping columns and rows
        double swap[][] = new double[arr[0].length][arr.length];
        for(int row = 0; row < arr.length; row++) {
            for(int col = 0; col < arr[row].length; col++) {
                swap[col][row] = arr[row][col];
            }
        }
        return swap;
    }

    /**
     * Function:	zScale()
     * Parameters:	featureCol	-- the column of each specified feature
     * Description:	For each column, find the mean and std
     * 				Then perform z-scale: (vector - mean)/std
     * Returns: 	A new column with z-scale numbers (an array)
     */
    public double[] zScale(double[] featureCol) {
        double [] scale = new double [featureCol.length];

        DescriptiveStatistics stats = new DescriptiveStatistics();

        for(int i = 0; i < featureCol.length; i++) {
            stats.addValue(featureCol[i]);
        }

        double mean = stats.getMean();
        double sd = stats.getStandardDeviation();

        for(int i = 0; i < featureCol.length; i++) {
            scale[i] = (featureCol[i] - mean) / sd;
        }

        return scale;
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
}
