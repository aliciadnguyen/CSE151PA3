import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alicia on 5/21/2016.
 */
public class KMeans {
    private double[][] points; // holds all the observations
    private List<Cluster> clusters; // holds the cluster for each centroid
    private double[][] centroids; // holds the random/newly calculated centroid observations
    boolean change = false;
    Cluster clusterCentroid = new Cluster();
    public double[][] oldCentroids;
    public double[][] newCentroids;


    public KMeans(double [][] arr, int k) {
        this.points = arr;
        this.clusters = new ArrayList<Cluster>();
        this.centroids = new double [k][arr[0].length];
        this.oldCentroids = new double [k][arr[0].length];
        this.newCentroids = new double [k][arr[0].length];
    }

    public List<Cluster> getCluster() {
        return clusters;
    }

    public void kmeans (double[][] observations, int k) {
        // Choose K centroid's from dataset by finding random points
        oldCentroids = getRandomCentroids(k);

        // Go through the rest of data and assign each obs to the cluster
        // that has the min distance from that pt to the centroid of the cluster
        assignClusters(k);

        while(true) {
            change = false;
            // Find new centroids of all of the clusters
            clearArray(centroids);
            clusters.clear();
            // Go back thru the pts and assign them to min distance to centroid clusters
            assignClusters(k);
            newCentroids = calculateCentroids(k);
            // Checks if the centroids are the same
            double distance = 0;
            for(int i = 0; i < newCentroids.length; i++) {
                distance += calculateDistance(newCentroids[i], oldCentroids[i], false);
            }
            if(distance == 0) break;
            oldCentroids = newCentroids;
        }
    }

    // Calculates the within cluster sum of squares
    public double wcss(double[][] data, double[] closestCentroid) {
        double value = 0;
        for(int i = 0; i < data.length; i++) {
            value += calculateDistance(data[i], closestCentroid, true);
        }
        return value;
    }

    // Pick random observations from the dataset and make them as centroids
    private double[][] getRandomCentroids(int k) {
        List<double[]> list = new ArrayList<double[]>(points.length);
        for (double[] i : points)
            list.add(i);
        Collections.shuffle(list);
        for(int i = 0; i < k; i++) {
            centroids[i] = list.get(i);
            clusterCentroid.setCentroid(centroids[i]);
            clusterCentroid.setId(i);
        }
        clusters.add(clusterCentroid);
        return centroids;
    }

    // Calculate the min distance between random centroids and all other observations
    private void assignClusters(int k) {
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
            clusterCentroid.setId(cluster_id);
            clusterCentroid.setCentroid(centroids[cluster_id]);
            clusterCentroid.points.add(points[i]);
            clusters.add(clusterCentroid);
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
        for(Cluster cluster: clusters) {
            List<double[]> list = cluster.getPoints();
            double convert [][] = convertListTo2D(list);
            double[][] swap = swapArr(convert);

            DescriptiveStatistics stats = new DescriptiveStatistics();
            // First find the sums of each column
            for(int b = 0; b < k_val; b++) {
                for(int i = 0; i < swap.length; i++) {
                    for(int j = 0; j < swap[0].length; j++) {
                        stats.addValue(swap[i][j]);
                    }
                    centroids[b][i] = stats.getMean();
                }
            }
        }
        return centroids;
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
