class Player {
    Square currentPosition;
    Player(Square startPosition){
        this.currentPosition = startPosition;
    }
    void moveLeft() {
        Square candidate = currentPosition.getLeftNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
    void moveRight() {
        Square candidate = currentPosition.getRightNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
    void moveUp() {
        Square candidate = currentPosition.getTopNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
    void moveDown() {
        Square candidate = currentPosition.getBottomNeighbor();
        if(candidate != null){
            currentPosition = candidate;
        }
    }
}
