import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * store and manage breadth-first and depth-first
 * searches
 */
class Search {
    // the square the search most recently looked at.
    // this is what the player must avoid to win the game.
    Square currentPosition;
    // determine's how the search moves when the square it
    // is at has multiple neighbors
    private DirectionPriority directionPriority;
    SearchType searchType;
    // this color is applied to all squares that
    // the search looks at
    Color visitedColor;
    // all the squares this search has looked at
    private HashSet<Square> visited;
    // this is a queue for breadth-first searches, but
    // depth-first searches use it as a stack
    private LinkedList<Square> queue;

    /**
     * start the search
     * @param searchType BFS or DFS
     * @param startingPosition the first square the search looks at
     * @param directionPriority how the search moves
     */
    Search(SearchType searchType, Square startingPosition, DirectionPriority directionPriority){
        this.searchType = searchType;
        this.currentPosition = startingPosition;
        this.directionPriority = directionPriority;
        // set the search's color based on its direction priority
        // so that dedicated players can discern its direction priority
        // based on the color of its visited squares
        if(directionPriority == DirectionPriority.LEFT_THEN_UP){
            this.visitedColor = Parameters.leftThenUpColor;
        }else if(directionPriority == DirectionPriority.LEFT_THEN_DOWN){
            this.visitedColor = Parameters.leftThenDownColor;
        }else if(directionPriority == DirectionPriority.RIGHT_THEN_UP){
            this.visitedColor = Parameters.rightThenUpColor;
        }else if(directionPriority == DirectionPriority.RIGHT_THEN_DOWN){
            this.visitedColor = Parameters.rightThenDownColor;
        }
        // add the first square to the queue and mark it as visited
        this.queue = new LinkedList<>();
        this.queue.add(startingPosition);
        this.visited = new HashSet<>();
        this.visited.add(startingPosition);
    }

    /**
     * move the search to the next square
     */
    void move(){
        // do nothing if there is no square to move to
        if(queue.isEmpty()){
            return;
        }
        currentPosition = queue.remove();
        // get the neighbors of the current square in the
        // order determined by the direction priority
        ArrayList<Square> neighbors = directionPriority.orderedNeighbors(currentPosition);
        // since depth-searches read off the queue in opposite
        // order as breadth-first searches, their list of neighbors must
        // be reversed to produce the same effect in the map
        if(searchType == SearchType.DFS){
            Collections.reverse(neighbors);
        }
        // add unvisited neighbors to the queue
        for(Square aNeighbor : neighbors){
            if(!visited.contains(aNeighbor)){
                visited.add(aNeighbor);
                if(searchType == SearchType.BFS){
                    queue.addLast(aNeighbor);
                }else if(searchType == SearchType.DFS){
                    // if this is a DFS, use the queue as a stack
                    queue.addFirst(aNeighbor);
                }
            }
        }
    }
}
