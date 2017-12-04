import java.util.ArrayList;

/**
 * defines how searches traverse the map.
 * since squares often have multiple neighbors,
 * the order in which a search visits the neighbors
 * must be determined. a search's direction priority
 * determines this order. the first direction in the
 * priority is the opposite of the last direction.
 * for example: for LEFT_THEN_UP, the search will
 * go left, up, down, then right.
 */
public enum DirectionPriority {
    LEFT_THEN_UP,
    LEFT_THEN_DOWN,
    RIGHT_THEN_UP,
    RIGHT_THEN_DOWN;

    /**
     * return the search's neighbors in the correct
     * order for the search's direction priority
     * @param currentSquare the search's current location
     * @return the ordered list of neighbors
     */
    ArrayList<Square> orderedNeighbors(Square currentSquare) {
        ArrayList<Square> neighbors = new ArrayList<>();
        Square candidate;
        switch (this) {
            // for each ordering, only return non-null
            // neighbors
            case LEFT_THEN_UP:
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            case LEFT_THEN_DOWN:
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            case RIGHT_THEN_UP:
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            case RIGHT_THEN_DOWN:
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            default:
                // in case an invalid direction priority occurs
                throw new AssertionError("Invalid direction priority: " + this);
        }
    }
}
