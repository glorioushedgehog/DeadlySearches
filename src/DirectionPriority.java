import java.util.ArrayList;

public enum DirectionPriority {
    LEFT_THEN_UP,
    LEFT_THEN_DOWN,
    RIGHT_THEN_UP,
    RIGHT_THEN_DOWN,
    UP_THEN_RIGHT,
    UP_THEN_LEFT,
    DOWN_THEN_RIGHT,
    DOWN_THEN_LEFT;

    ArrayList<Square> orderedNeighbors(Square currentSquare) {
        ArrayList<Square> neighbors = new ArrayList<>();
        Square candidate;
        switch (this) {
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
            case UP_THEN_RIGHT:
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            case UP_THEN_LEFT:
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            case DOWN_THEN_RIGHT:
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            case DOWN_THEN_LEFT:
                candidate = currentSquare.getBottomNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getLeftNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getRightNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                candidate = currentSquare.getTopNeighbor();
                if(candidate != null){
                    neighbors.add(candidate);
                }
                return neighbors;
            default:
                throw new AssertionError("Invalid direction priority: " + this);
        }
    }
}
