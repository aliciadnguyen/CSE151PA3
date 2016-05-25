import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alicia on 5/23/2016.
 */
public class Cluster {
    public List<double[]> pointList;
    public double [] centroid;
    public int id;

    public Cluster(int id) {
        this.pointList = new ArrayList<double[]>();
        this.centroid = null;
        this.id = id;
    }

//    public void setCluster(int n) {
//        this.id = n;
//    }

    public int getCluster() {
        return this.id;
    }
}
