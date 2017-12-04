import java.awt.*;

/**
 * how the map is stored. walls are the absence
 * of neighbors in a square's neighbor fields
 */
class Square {
    // neighbors are as determined by the map
    private Square leftNeighbor;
    private Square rightNeighbor;
    private Square topNeighbor;
    private Square bottomNeighbor;
    // the j and i coordinate of the square
    // in Game's matrix of squares. these
    // are used for drawing the squares
    int x;
    int y;
    // a square's color is changed when a search
    // looks at it. the color is always the average
    // of the colors of each of the searches that
    // have looked at it
    Color color;
    // color totals are used to find the average
    // of the colors of the searches that have
    // looked at this square
    private int redTotal = 0;
    private int greenTotal = 0;
    private int blueTotal = 0;
    // the number of colors that have been averaged
    // to get this squares color. this is needed for correctly
    // calculating the average of all the colors.
    private int numCombinedColors = 0;

    // getter and setters
    Square getLeftNeighbor() {
        return leftNeighbor;
    }

    Square getRightNeighbor() {
        return rightNeighbor;
    }

    Square getTopNeighbor() {
        return topNeighbor;
    }

    Square getBottomNeighbor() {
        return bottomNeighbor;
    }

    void setLeftNeighbor(Square leftNeighbor) {
        this.leftNeighbor = leftNeighbor;
    }

    void setRightNeighbor(Square rightNeighbor) {
        this.rightNeighbor = rightNeighbor;
    }

    void setTopNeighbor(Square topNeighbor) {
        this.topNeighbor = topNeighbor;
    }

    void setBottomNeighbor(Square bottomNeighbor) {
        this.bottomNeighbor = bottomNeighbor;
    }

    // neighbors need to be removed by MapGenerator when it makes
    // maps because walls are represented as the absence of neighbors
    void removeLeftNeighbor() {
        this.leftNeighbor = null;
    }

    void removeRightNeighbor() {
        this.rightNeighbor = null;
    }

    void removeTopNeighbor() {
        this.topNeighbor = null;
    }

    void removeBottomNeighbor() {
        this.bottomNeighbor = null;
    }

    /**
     * when a search looks at this square,
     * the search's visited color will be added
     * to this squares color by taking the average
     * of all the colors of searches that have visited
     * this square
     * @param colorToAdd the visited color of the search that looked at this square
     */
    void addColor(Color colorToAdd) {
        // find the average of all the colors that have been
        // added to this square
        numCombinedColors++;
        redTotal += colorToAdd.getRed();
        greenTotal += colorToAdd.getGreen();
        blueTotal += colorToAdd.getBlue();
        int red = redTotal / numCombinedColors;
        int green = greenTotal / numCombinedColors;
        int blue = blueTotal / numCombinedColors;
        color = new Color(red, green, blue);
    }

    /**
     * make a square
     * @param x the j coordinate of the square in Game's matrix of squares
     * @param y the i coordinate of the square in Game's matrix of squares
     */
    Square(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
