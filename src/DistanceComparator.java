import java.util.Comparator;

/**
 * Class:		DistanceComparator
 * Description:	An object used to order an list of Results object based on
 * 				its distances. It will sort the list based on its distance
 * 				on ascending order. This function is used to find k nearest
 * 				neighbors.
 * Attributes:	compare		--  function to override in order to sort distances
 * 								in ascending order
 */
public class DistanceComparator implements Comparator<Result> {
    @Override
    public int compare(Result a, Result b) {
        return a.distance < b.distance ? -1 : a.distance == b.distance ? 0 : 1;
    }
}
