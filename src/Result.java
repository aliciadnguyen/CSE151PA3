/**
 * Class:		Result
 * Description:	This object holds the values of distance and its classifier
 * 				label. Result is used as an object when finding the
 * 				k nearest neighbor.
 * Attributes:	distance 	-- holds the euclidean distance from two points
 * 				classifier	-- the number it is labeled as
 */
public class Result {
    double distance;
    double classifier;
    public Result(double distance, double c) {
        this.distance = distance;
        this.classifier = c;
    }
}
