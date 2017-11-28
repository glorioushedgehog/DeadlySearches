import java.awt.*;

class Square {
    private Square leftNeighbor;
    private Square rightNeighbor;
    private Square topNeighbor;
    private Square bottomNeighbor;
    int x;
    int y;
    Color color;
    private int redTotal = 0;
    private int greenTotal = 0;
    private int blueTotal = 0;
    private int numCombinedColors = 0;

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

    void addColor(Color colorToAdd) {
        numCombinedColors++;
        redTotal += colorToAdd.getRed();
        greenTotal += colorToAdd.getGreen();
        blueTotal += colorToAdd.getBlue();
        int red = redTotal / numCombinedColors;
        int green = greenTotal / numCombinedColors;
        int blue = blueTotal / numCombinedColors;
        color = new Color(red, green, blue);
    }

    Square(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
