import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

class Search {
    Square currentPosition;
    private DirectionPriority directionPriority;
    SearchType searchType;
    Color visitedColor;
    private HashSet<Square> visited;
    private LinkedList<Square> queue;
    Search(SearchType searchType, Square startingPosition, DirectionPriority directionPriority){
        this.searchType = searchType;
        this.currentPosition = startingPosition;
        this.directionPriority = directionPriority;
        if(directionPriority == DirectionPriority.LEFT_THEN_UP){
            this.visitedColor = Parameters.leftThenUpColor;
        }else if(directionPriority == DirectionPriority.LEFT_THEN_DOWN){
            this.visitedColor = Parameters.leftThenDownColor;
        }else if(directionPriority == DirectionPriority.RIGHT_THEN_UP){
            this.visitedColor = Parameters.rightThenUpColor;
        }else if(directionPriority == DirectionPriority.RIGHT_THEN_DOWN){
            this.visitedColor = Parameters.rightThenDownColor;
        }
        this.queue = new LinkedList<>();
        this.queue.add(startingPosition);
        this.visited = new HashSet<>();
        this.visited.add(startingPosition);
    }
    void move(){
        if(queue.isEmpty()){
            return;
        }
        currentPosition = queue.remove();
        ArrayList<Square> neighbors = directionPriority.orderedNeighbors(currentPosition);
        if(searchType == SearchType.DFS){
            Collections.reverse(neighbors);
        }
        for(Square aNeighbor :  neighbors){
            if(!visited.contains(aNeighbor)){
                visited.add(aNeighbor);
                if(searchType == SearchType.BFS){
                    queue.addLast(aNeighbor);
                }else if(searchType == SearchType.DFS){
                    queue.addFirst(aNeighbor);
                }
            }
        }
    }
}
