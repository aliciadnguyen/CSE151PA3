import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Alicia on 5/21/2016.
 */
public class KMeans {
    private double[][] points; // holds all the observations
    private double [][] targets;
    // Need to rethink data structure for holding groups of clusters
//    public List<List<double[]>> xclusterList =  new ArrayList<List<double[]>>(); // holds the clusters of X
//    public List<List<double[]>> yclusterList =  new ArrayList<List<double[]>>(); // holds the clusters of Y
    public List<Cluster> xclusterList =  new ArrayList<Cluster>(); // holds the clusters of X
    public List<Cluster> yclusterList =  new ArrayList<Cluster>(); // holds the clusters of Y
    private double[][] centroids; // holds the random/newly calculated centroid observations
    boolean change = false;
    public double[][] oldCentroids;
    public double[][] newCentroids;
    public List<List<Double>> clusterMean;
    public List<List<Double>> clusterStd;

//    public List<List<double[]>> getClusterList() {
//        return xclusterList;
//    }

    public KMeans(double [][] arr, double[][] t,  int k) {
        this.points = arr;
        this.targets = t;
        for(int i = 0; i < k; i++) {
//            List<double[]> list = new ArrayList<double[]>();
            Cluster clusX = new Cluster(i);
            Cluster clusY = new Cluster(i);

//            xclusterList.add(list);
//            yclusterList.add(list);
            xclusterList.add(clusX);
            yclusterList.add(clusY);
        }
        this.centroids = new double [k][arr[0].length];
        this.oldCentroids = new double [k][arr[0].length];
        this.newCentroids = new double [k][arr[0].length];
        this.clusterMean = new ArrayList<List<Double>>();
        this.clusterStd = new ArrayList<List<Double>>();
    }

    public void kmeans (int k) {
        // Choose K centroid's from dataset by finding random points
        getRandomCentroids(k);

        // Go through the rest of data and assign each obs to the cluster
        // that has the min distance from that pt to the centroid of the cluster
        assignClusters(oldCentroids, k);

        while(true) {
            change = false;
            // Find new centroids of all of the clusters
            clearArray(centroids);
            clearList(k);
            // Go back thru the pts and assign them to min distance to centroid clusters
            assignClusters(oldCentroids, k);
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
    // TODO NEED TO FIX THE LOGIC OF ADDING POINTS TO A CERTAIN LIST.
    private void assignClusters(double[][] centroids, int k) {
        double max = Double.MAX_VALUE;
        double dist = 0;
        int cluster_id = 0;
        int [] remember = new int[k];
        for(int v = 0; v < remember.length; v++) {
            remember[v] = -1;
        }
        boolean made = false;
        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < k; j++){
                dist = calculateDistance(centroids[j], points[i], false);
                if(dist < max) {
                    max = dist;
                    cluster_id = j;
//                    for(int num = 0; num < remember.length; num++) {
//                        if(remember[num] == cluster_id){
//                            made = true;
//                            break;
//                        }
//                    }
//                    if(made != true) {
//                        xlist = new ArrayList();
//                        ylist = new ArrayList();
//                    }
                }
//                remember[cluster_id] = cluster_id;
//                List<double[]> xlist = new ArrayList<double[]>();
//                List <double[]> ylist = new ArrayList<double[]>();
//                xlist.add(i, points[i]);
//                ylist.add(i, targets[i]);
//                xclusterList.add(cluster_id, xlist);
//                yclusterList.add(cluster_id, ylist);
                xclusterList.get(cluster_id).pointList.add(points[i]);
                yclusterList.get(cluster_id).pointList.add(targets[i]);
            }
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
        //System.out.println("List size is : " + l.size());
        double [][] arr = new double[l.size()][l.get(0).length];
        for(int i = 0; i < l.size(); i++) {
            for(int j = 0; j < l.get(i).length; j++) {
                arr[i][j] = l.get(i)[j];
            }
        }
        return arr;
    }

    // Calculate new centroids of each cluster
    private double[][] calculateCentroids(int k_val) {
        double[][] calCent = new double[k_val][10];
        ;
        for(int b = 0; b < k_val; b++) {
            double convert [][] = convertListTo2D(xclusterList.get(b).pointList);
            double[][] swap = swapArr(convert);
            //alCent = new double[k_val][convert[0].length];

            DescriptiveStatistics stats = new DescriptiveStatistics();
            // First find the sums of each column
            for(int i = 0; i < swap.length; i++) {
                for(int j = 0; j < swap[0].length; j++) {
                    stats.addValue(swap[i][j]);
                }
                calCent[b][i] = stats.getMean();
            }
        }
        return calCent;
    }

    // Calculates the mean and std of each cluster's feature columns
    public void getMeanAndStd(List<double[]> list) {
        double [][] cluster = swapArr(convertListTo2D(list));
        List<Double> featureMean = new ArrayList<Double>();
        List<Double> featureStd = new ArrayList<Double>();
        for(int i = 0; i < cluster.length; i++) {
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (int j = 0; j < cluster[i].length; j++) {
                stats.addValue(cluster[i][j]);
            }
            featureMean.add(stats.getMean());
            featureStd.add(stats.getStandardDeviation());
        }
        clusterMean.add(featureMean);
        clusterStd.add(featureStd);
    }

    // Clears the cluster list
    private void clearArray(double[][] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                arr[i][j] = 0;
            }
        }
    }

    private void clearList (int k) {
        xclusterList.clear();
        yclusterList.clear();

        for(int i = 0; i < k; i++) {
//            List<double[]> list = new ArrayList<double[]>();
//            xclusterList.add(list);
//            yclusterList.add(list);
            Cluster clusX = new Cluster(i);
            Cluster clusY = new Cluster(i);
            xclusterList.add(clusX);
            yclusterList.add(clusY);
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
    }

}
