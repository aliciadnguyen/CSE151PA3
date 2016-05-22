/**
 * Created by Alicia on 5/21/2016.
 */
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Cluster {

    public List<double[]> points;
    public double[] centroid;
    public int id;
    public double mean;
    public double std;

    //Creates a new Cluster
    public Cluster() {
        this.centroid = null;
        this.points = new ArrayList<double[]>();
    }

    public void calMeanAndStd() {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for(int i = 0; i < points.size(); i++) {
            for(int j = 0; j < points.get(i).length; j++) {
                stats.addValue(points.get(i)[j]);
            }
        }
        this.mean = stats.getMean();
        this.std = stats.getStandardDeviation();
    }

    public double getMean() {
        return this.mean;
    }

    public double getStd() {
        return this.std;
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

    public List<double[]> getPoints() {
        return points;
    }

    public void setPoints(List<double[]>points) {
        this.points = points;
    }

    public double[] getCentroid() {
        return centroid;
    }

    public void setCentroid(double[] centroid) {
        this.centroid = centroid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
