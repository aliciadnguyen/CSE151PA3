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


    public KMeans(double [][] arr, int k) {
        this.points = new double [arr.length][arr[0].length];
        this.clusters = new ArrayList<Cluster>();
        this.centroids = new double [k][arr[0].length];
    }

    public List<Cluster> getCluster() {
        return clusters;
    }

    public void kmeans (double[][] observations, int k) {
        // Choose K centroid's from dataset by finding random points
        addObsPoints(observations);
        getCentroids(observations, k);

        // Go through the rest of data and assign each obs to the cluster
        // that has the min distance from that pt to the centroid of the cluster
        assignClusters(k);

        while(!change) {
            change = false;
            // Find new centroids of all of the clusters
            clearArray(centroids);
            calculateCentroids();
            // Go back thru the pts and assign them to min distance to centroid clusters
            assignClusters(k);
        }
    }

    // Calculates the within cluster sum of squares
    public double wcss(double[][] data, double[] closestCentroid) {
        double value = 0;
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[i].length; j++) {
                value += Math.abs(data[i][j] - closestCentroid[j]);
            }
        }
        return value;
    }

    // Pick random observations from the dataset and make them as centroids
    private void getCentroids(double[][] obs, int k) {
        List<double[]> list = new ArrayList<double[]>(obs.length);
        //System.out.println(obs.length);
        for (double[] i : obs)
            list.add(i);
        Collections.shuffle(list);
        for(int i = 0; i < k; i++) {
            centroids[i] = list.get(i);
        }
    }

    private void addObsPoints(double[][] obs) {
        for(int i = 0; i < obs.length; i++) {
            points[i] = obs[i];
        }
    }

    // Calculate the min distance between random centroids and all other observations
    private void assignClusters(int k) {
        double max = Double.MAX_VALUE;
        double dist = 0;
        int cluster_id = 0;
        int num = 0;
        //System.out.println(points.length);
        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < k; j++){
                dist = calculateDistance(centroids[j], points[i]);
                //System.out.println(dist);
                if(dist < max) {
                    max = dist;
                    cluster_id = j;
                }
                if(dist == 0){
                    change = true;
                    break;
                }
            }
            clusterCentroid.setId(cluster_id);
            clusterCentroid.setCentroid(centroids[cluster_id]);
            clusterCentroid.points.add(points[i]);
            clusters.add(clusterCentroid);
            //printArr(clusters.get(0).points);
            /*for(int l = 0; l < clusterCentroid.points.size(); l++) {
                for(int m = 0; m < clusterCentroid.points.get(l).length; m++)
                    System.out.print(clusterCentroid.points.get(l)[m] + " ");
            }
            System.out.println();*/
        }
        //System.out.println(clusters.get(1).points.size());
    }

    // Calculates the distance between two observations and their feature vectors
    private double calculateDistance(double[] randomCentroids, double[] obs) {
        double max = Double.MAX_VALUE;
        double dist = 0;
        //System.out.println(randomCentroids.length);
        // Find min distance between random centroids and all of observations
        for(int i = 0; i < randomCentroids.length; i++) {
            dist += Math.pow(obs[i] - randomCentroids[i], 2);
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
    private void calculateCentroids() {
        for(Cluster cluster: clusters) {
            List<double[]> list = cluster.getPoints();
            int n_points = list.size();
            double [] sumFeatures = new double[n_points];
            double [] newCentroids = new double[n_points];

            double convert [][] = convertListTo2D(list);

            double[][] swap = swapArr(convert);
            // First find the sums of each column
            for(int i = 0; i < swap.length; i++) {
                for(int j = 0; j < swap[0].length; j++) {
                    sumFeatures[i] += swap[i][j];
                }
            }

            // Find the average area of the points
            if(n_points > 0) {
                for(int k = 0; k < swap[0].length; k++) {
                    newCentroids[k] = sumFeatures[k]/n_points;
                    centroids[k] = newCentroids;
                }
            }
        }
    }

    // Clears the cluster list
    private void clearArray(double[][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                arr[i][j] = 0;
            }
        }
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
     * Function:	printArr
     * Description:	Print the array in better format
     * Parameters:	2D Array
     * Returns:		Void
     */
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

    /**
     * Function:	printArr
     * Description:	Print the array in better format
     * Parameters:	2D Array
     * Returns:		Void
     */
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
