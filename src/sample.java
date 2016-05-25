import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.*;
import java.util.*;

public class sample {
    double [][] xtest;
    double [][] xtrain;
    double [][] ytest;
    double [][] ytrain;

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
    public void findSample(Random rnd, double[][] totalSample, double percent, boolean aba) {

        // Set the sample size to 10%
        double size = totalSample.length;
        double sampleSize = size * percent;

        int cols = totalSample[0].length;

        //System.out.println((int)(size - sampleSize));

        if(aba == true) {
            xtrain = new double[(int)(size - sampleSize) + 1][cols-1];
            ytrain = new double[(int)(size - sampleSize) + 1][1];
        } else {
            xtrain = new double[(int)(size - sampleSize)][cols-1];
            ytrain = new double[(int)(size - sampleSize)][1];
        }

        xtest = new double[(int)sampleSize][cols-1];
        ytest = new double[(int)sampleSize][1];

        // Number of selected items
        double count = 0;
        double thres;

        int trainI = 0;
        int testI = 0;

        for(int i = 0; i < size; i++) {
            thres = ((int)sampleSize - count)/(size - i);
            if(rnd.nextDouble() < thres){
                count++;
                for(int j = 0; j < cols-1; j++) {
                    xtest[testI][j] = totalSample[i][j];
                }
                ytest[testI][0] = totalSample[i][cols-1];
                testI++;
            }
            else {
                for(int j = 0; j < cols - 1; j++) {
                    xtrain[trainI][j] = totalSample[i][j];
                    //System.out.println(i + " "+  j);
                    //System.out.println("TrainI: " + trainI);
                }
                ytrain[trainI][0] = totalSample[i][cols-1];
                trainI++;
            }
        }
    }

    public void createChart(List<Double> list, int[] trials, String title) {
        XYSeriesCollection dataSet = new XYSeriesCollection();
        XYSeries series = new XYSeries("Plotted Points");

        for(int m = 0; m < list.size(); m++) {
            series.add(trials[m], list.get(m));
            //System.out.println("Trials: " + trials[m]);
            //System.out.println("Means: " + means[m]);
        }

        dataSet.addSeries(series);

        String chartTitle = title;
        String xAxisLabel = "K";
        String yAxisLabel = "WCSS";

        JFreeChart chart = ChartFactory.createXYLineChart(
                chartTitle, xAxisLabel,
                yAxisLabel, dataSet,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);

        try {
            ChartUtilities.saveChartAsJPEG(
                    new File("C:\\Users\\Alicia\\Desktop\\" + title + ".jpg"),
                    chart, 500, 300);
            System.out.println("Charted created!");
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart: " + e);
        }
    }
}
