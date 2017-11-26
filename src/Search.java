import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

class Search {
    Square currentPosition;
    private DirectionPriority directionPriority;
    SearchType searchType;
    Color visitedColor;
    private HashSet<Square> visited;
    private LinkedList<Square> queue;
    Search(SearchType searchType, Square startingPosition, DirectionPriority directionPriority, Color visitedColor){
        this.searchType = searchType;
        this.currentPosition = startingPosition;
        this.directionPriority = directionPriority;
        this.visitedColor = visitedColor;
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
