import java.awt.*;

class Parameters {
    static final String title = "Deadly Searches";
    static final boolean fullScreen = false;
    static final int viewWidth = 1500;
    static final int viewHeight = 1000;
    static final double horizontalPadding = 0.2;
    static final double verticalPadding = 0.2;

    static final int tickDelay = 10;
    static final int searchDelay = 500;
    static final int numFoods = 10;

    static final int wallWidth = 3;
    static final int numSquaresInRow = 20;
    static final int numRowsOfSquares = 20;

    static final Color mapColor = new Color(183, 188, 176);
    static final Color wallColor = new Color(35, 26, 150);
    static final Color playerColor = new Color(234, 18, 36);
    // this should be different for BFS's and DFS's
    static final Color searchColor = new Color(128, 118, 68);
    static final Color foodColor = new Color(3, 187, 32);

    static final Color leftThenUpColor = new Color(248, 102, 105);
    static final Color leftThenDownColor = new Color(248, 177, 106);
    static final Color rightThenUpColor = new Color(137, 144, 249);
    static final Color rightThenDownColor = new Color(125, 249, 126);
}
