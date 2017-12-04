/**
 * keeps track of the player's position
 */
class Player {
    // the square of the map which the
    // player is at
    Square currentPosition;

    /**
     * make a new player at the spawn position
     * @param startPosition the spawn position
     */
    Player(Square startPosition){
        this.currentPosition = startPosition;
    }

    /**
     * move the player left if it is possible
     */
    void moveLeft() {
        Square candidate = currentPosition.getLeftNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
    /**
     * move the player right if it is possible
     */
    void moveRight() {
        Square candidate = currentPosition.getRightNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
    /**
     * move the player up if it is possible
     */
    void moveUp() {
        Square candidate = currentPosition.getTopNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
    /**
     * move the player down if it is possible
     */
    void moveDown() {
        Square candidate = currentPosition.getBottomNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
}
