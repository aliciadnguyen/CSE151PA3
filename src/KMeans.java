import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alicia on 5/21/2016.
 */
public class KMeans {
    private double[][] points; // holds all the observations
    public List<double[]> clusters; // holds the clusters
    private double[][] centroids; // holds the random/newly calculated centroid observations
    boolean change = false;
    public double[][] oldCentroids;
    public double[][] newCentroids;


    public KMeans(double [][] arr, int k) {
        this.points = arr;
        this.clusters = new ArrayList<double[]>();
        this.centroids = new double [k][arr[0].length];
        this.oldCentroids = new double [k][arr[0].length];
        this.newCentroids = new double [k][arr[0].length];
    }

    public void kmeans (double[][] observations, int k) {
        // Choose K centroid's from dataset by finding random points
        getRandomCentroids(k);

        // Go through the rest of data and assign each obs to the cluster
        // that has the min distance from that pt to the centroid of the cluster
        assignClusters(oldCentroids, k);

        while(true) {
            change = false;
            // Find new centroids of all of the clusters
            clearArray(centroids);
            clusters.clear();
            // Go back thru the pts and assign them to min distance to centroid clusters
            assignClusters(oldCentroids, k);
            //printList(clusters);
            newCentroids = calculateCentroids(k);
            // Checks if the centroids are the same
            double distance = 0;
            //printArr(newCentroids);
            //printArr(oldCentroids);
            for(int i = 0; i < newCentroids.length; i++) {
                distance += calculateDistance(newCentroids[i], oldCentroids[i], false);
            }
            if(distance == 0) break;
            oldCentroids = newCentroids;
        }
    }

    // Calculates the within cluster sum of squares
    public double wcss(double[] data, double[] closestCentroid) {
        double value = 0;
        value += calculateDistance(data, closestCentroid, true);
        return value;
    }

    // Pick random observations from the dataset and make them as centroids
    private void getRandomCentroids(int k) {
        List<double[]> list = new ArrayList<double[]>(points.length);
        for (double[] i : points)
            list.add(i);
        Collections.shuffle(list);
        for(int i = 0; i < k; i++) {
            oldCentroids[i] = list.get(i);
        }
    }

    // Calculate the min distance between random centroids and all other observations
    private void assignClusters(double[][] centroids, int k) {
        double max = Double.MAX_VALUE;
        double dist = 0;
        int cluster_id = 0;
        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < k; j++){
                dist = calculateDistance(centroids[j], points[i], false);
                if(dist < max) {
                    max = dist;
                    cluster_id = j;
                }
            }
            clusters.add(points[i]);
        }
    }

    // Calculates the distance between two observations and their feature vectors
    private double calculateDistance(double[] randomCentroids, double[] obs, boolean wcss) {
        double max = Double.MAX_VALUE;
        double dist = 0;
        // Find min distance between random centroids and all of observations
        for(int i = 0; i < randomCentroids.length; i++) {
            dist += Math.pow(obs[i] - randomCentroids[i], 2);
            if(wcss == false)
                dist = Math.sqrt(dist);
        }
        return dist;
    }

    public double[][] convertListTo2D(List<double[]> l) {
        double [][] arr = new double[l.size()][l.get(0).length];
        for(int i = 0; i < l.size(); i++) {
            for(int j = 0; j < l.get(i).length; j++) {
                arr[i][j] = l.get(i)[j];
            }
        }
        return arr;
    }

    // Calculate new centroids
    private double [][] calculateCentroids(int k_val) {
        double convert [][] = convertListTo2D(clusters);
        double[][] swap = swapArr(convert);
        double[][] calCent = new double[k_val][clusters.get(0).length];

        DescriptiveStatistics stats = new DescriptiveStatistics();
        // First find the sums of each column
        for(int b = 0; b < k_val; b++) {
            for(int i = 0; i < swap.length; i++) {
                for(int j = 0; j < swap[0].length; j++) {
                    stats.addValue(swap[i][j]);
                }
                calCent[b][i] = stats.getMean();
            }
        }
        return calCent;
    }

    // Clears the cluster list
    private void clearArray(double[][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                arr[i][j] = 0;
            }
        }
    }

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

    public void printArr(double[][] arr){
        for(int r = 0; r < arr.length; r++) {
            for(int c = 0; c < arr[r].length; c++) {
                System.out.print(arr[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

    public void printList(List<double[]> arr){
        for(int r = 0; r < arr.size(); r++) {
            for(int c = 0; c < arr.get(r).length; c++) {
                System.out.print(arr.get(r)[c] + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }

}
