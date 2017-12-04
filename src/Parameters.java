import java.awt.*;

/**
 * stores constants concerning gameplay and game appearance
 */
class Parameters {
    // the game's title
    static final String title = "Deadly Searches";
    static final boolean fullScreen = false;
    // these are only used if fullscreen is false
    static final int viewWidth = 1500;
    static final int viewHeight = 1000;
    // the percentage of the viewWidth that is guaranteed
    // to not be taken up by the map
    static final double horizontalPadding = 0.2;
    // the percentage of the viewHeight that is guaranteed
    // to not be taken up by the map
    static final double verticalPadding = 0.2;

    // the number of milliseconds that pass
    // between ticks
    static final int tickDelay = 10;
    // this is divided by tickDelay to get the
    // number of ticks that must pass before
    // the searches move
    static final int searchDelay = 500;
    // the number of foods that will spawn
    // each level
    static final int numFoods = 10;

    // the width of the lines that are drawn
    // to show the walls
    static final int wallWidth = 3;
    static final int numSquaresInRow = 20;
    static final int numRowsOfSquares = 20;

    // colors
    // background of the entire JPanel
    static final Color backGroundColor = new Color(230, 230, 210);
    // color of the level banner and all text that appears
    // in the menu, pause screen, and game over screen
    static final Color textColor = new Color(61, 32, 151);
    // the color of the rectangle that is drawn behind the text
    // in the menu, pause screen, and the game over screen
    static final Color overlayColor = new Color(255, 255, 255, 200);
    // the color of the rectangle that is drawn underneath the map
    static final Color mapColor = new Color(230, 230, 230);
    // the color of the lines that show the walls
    static final Color wallColor = new Color(35, 26, 150);
    // the color of the square the player is in
    static final Color playerColor = new Color(234, 18, 36);
    // the color of the current positions of the searches
    static final Color searchColor = new Color(128, 118, 68);
    // the color of any square that has food in it
    static final Color foodColor = new Color(3, 187, 32);
    // the visited color of a search with the LEFT_THEN_UP direction priority
    static final Color leftThenUpColor = new Color(248, 102, 105);
    // the visited color of a search with the LEFT_THEN_DOWN direction priority
    static final Color leftThenDownColor = new Color(248, 177, 106);
    // the visited color of a search with the RIGHT_THEN_UP direction priority
    static final Color rightThenUpColor = new Color(137, 144, 249);
    // the visited color of a search with the RIGHT_THEN_DOWN direction priority
    static final Color rightThenDownColor = new Color(125, 249, 126);
}
