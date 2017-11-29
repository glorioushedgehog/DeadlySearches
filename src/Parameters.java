import java.awt.*;

class Parameters {
    static final String title = "Deadly Searches";
    static final boolean fullScreen = false;
    static final int viewWidth = 1000;
    static final int viewHeight = 1000;

    static final int tickDelay = 10;
    static final int searchDelay = 500;
    static final int numFoods = 10;

    static final int wallWidth = 3;
    static final int numSquaresInRow = 20;
    static final int numRowsOfSquares = 20;
    static final int mapWidth = 1000;
    static final int mapHeight = 800;

    static final Color mapColor = new Color(228, 234, 219);
    static final Color wallColor = new Color(35, 26, 150);
    static final Color playerColor = new Color(234, 18, 36);
    // this should be different for BFS's and DFS's
    static final Color searchColor = new Color(128, 118, 68);
    static final Color foodColor = new Color(3, 187, 32);

    static final Color leftThenUpColor = new Color(248, 162, 162);
    static final Color leftThenDownColor = new Color(248, 165, 228);
    static final Color rightThenUpColor = new Color(169, 159, 249);
    static final Color rightThenDownColor = new Color(178, 249, 181);
}
