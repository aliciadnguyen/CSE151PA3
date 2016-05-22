/**
 * Created by Alicia on 5/21/2016.
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Cluster {

    public List<double[]> points;
    public double[] centroid;
    public int id;

    //Creates a new Cluster
    public Cluster() {
        this.centroid = null;
        this.points = new ArrayList<double[]>();
    }

    public List<double[]> getPoints() {
        return points;
    }

    /*public void addPoint(double [] point) {
        points = new double[1][point.length];
        int rows = points.length;
        int cols = points[0].length;
        double[][] tmpPoints = new double[rows+1][cols];
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                tmpPoints[i][j] = points[i][j];
                System.out.println(points[i][j] + " ");
            }
            System.out.println();
        }


        tmpPoints[rows] = point;
        points = tmpPoints;
    }
*/
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
