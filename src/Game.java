import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Draws and updates the game
 * Test for the end of levels,
 * the playing losing or winning
 */
public class Game extends JPanel implements ActionListener {
    // randomly creates a map and adds food to it
    private MapGenerator mapGenerator;
    // how the map is stored
    private Square[][] squares;
    // the width of the JPanel
    private int viewWidth;
    // the height of the JPanel
    private int viewHeight;
    // the number of squares in each row of the map
    private int width;
    // the number of rows of squares in the map
    private int height;
    // the x-coordinate of the left side of the map
    private int xOffset;
    // the y-coordinate of the top of the map
    private int yOffset;
    // the number of squares that have not been searched by all searches
    private int numUnSearchedSquares;
    // the width and height of the squares that make up the make
    private int squareSize;
    // the number of ticks that pass between searches
    // advancing to the next square. decreases by 1 when the
    // player completes a level
    private int ticksBetweenSearchMoves;
    // used to keep of track of when the searches should
    // advanced to their next squares
    private int ticksTilSearchMove;
    private Search[] searches;
    // the x and y coordinate pair of the foods still
    // remaining in the map. the player must collect
    // all foods to advance to the next level
    private HashSet<List<Integer>> foods;
    // particles are spawned when the player collects
    // food to inform the player that they have succeeded
    private HashSet<Particle> particles;
    // used to update the game
    private Timer timer;
    // if the player is found by a search,
    // this will record the type of the search so the player
    // can be informed what search made them lose the game
    private SearchType typeOfSearchFoundPlayer;
    // the number of the level the player is playing.
    // as this increases, the speed at which the searches
    // search increases
    private int level;
    // keeps track of whether or not the game is running,
    // paused, over, or has not yet started
    GameState gameState;
    Player player;

    /**
     * define all fields that will be constant
     * throughout the course of the game
     * @param viewWidth the width of the JPanel
     * @param viewHeight the height of the JPanel
     */
    Game(int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.width = Parameters.numSquaresInRow;
        this.height = Parameters.numRowsOfSquares;
        int maxHorizontalSquareSize = (int) (viewWidth * (1 - Parameters.horizontalPadding) / width);
        int maxVerticalSquareSize = (int) (viewHeight * (1 - Parameters.verticalPadding) / height);
        // ensure that all necessary squares can fit in the view by making
        // them as small they are required to be by either restraint
        this.squareSize = Math.min(maxHorizontalSquareSize, maxVerticalSquareSize);
        // find the xOffset of the map that makes it centered horizontally
        this.xOffset = (viewWidth - width * squareSize) / 2;
        // find the yOffset of the map that makes it centered vertically
        this.yOffset = (viewHeight - height * squareSize) / 2;
        // detect key presses
        addKeyListener(new KeyResponder(this));
        // in order for key detection to work, the game
        // has to be focusable
        setFocusable(true);
        // prepare to generate the map
        this.mapGenerator = new MapGenerator(width, height);
        // determine how many ticks must pass before the searches should advance
        // by correcting for the tick rate
        this.ticksBetweenSearchMoves = (int) (((double) Parameters.searchDelay) / Parameters.tickDelay);
        this.gameState = GameState.MENU;
        this.timer = new Timer(Parameters.tickDelay, this);
        // create the map and the searches
        prepareLevel();
    }

    /**
     * create the map, foods, and searches
     */
    private void prepareLevel() {
        this.level++;
        // if the searches will still wait between ticks
        // before searching, decrease the number of ticks
        // they wait to increase the difficulty of the game
        if (this.ticksBetweenSearchMoves > 0) {
            this.ticksBetweenSearchMoves--;
        }
        // width * height is the total number of squares
        // the -1 accounts for the squares in which the searches
        // begin, which they do not have to search
        this.numUnSearchedSquares = width * height - 1;
        this.squares = mapGenerator.generateMap();
        // put the player in the middle of the map
        this.player = new Player(squares[height / 2][width / 2]);
        // initialize all the searches
        this.searches = new Search[]{
                new Search(SearchType.BFS, squares[height / 4][width / 4], DirectionPriority.RIGHT_THEN_DOWN),
                new Search(SearchType.DFS, squares[height / 4][3 * width / 4], DirectionPriority.LEFT_THEN_DOWN),
                new Search(SearchType.DFS, squares[3 * height / 4][width / 4], DirectionPriority.RIGHT_THEN_UP),
                new Search(SearchType.BFS, squares[3 * height / 4][3 * width / 4], DirectionPriority.LEFT_THEN_UP)
        };
        // generate foods, making sure to not spawn them on the player
        this.foods = mapGenerator.generateFood(Parameters.numFoods, player.currentPosition);
        this.particles = new HashSet<>();
        this.ticksTilSearchMove = ticksBetweenSearchMoves;
        // this has to be set to null because the player
        // might be found by a search and then start a new game.
        // in that case, we wouldn't want to still think that
        // a search had found the player
        this.typeOfSearchFoundPlayer = null;
    }

    /**
     * pause the game
     * only called when game is in PLAYING state
     */
    void pause() {
        gameState = GameState.PAUSED;
        timer.stop();
        // draw the pause screen to inform
        // the player that the game is paused
        repaint();
    }

    /**
     * un-pause the game
     * called both when the game is over
     * and when the player has completed a level
     */
    void unPause() {

        if (gameState == GameState.GAME_OVER) {
            level = 0;
            prepareLevel();
        } else if (gameState == GameState.LEVEL_COMPLETE) {
            prepareLevel();
        }
        gameState = GameState.PLAYING;
        timer.start();
    }

    /**
     * mark the game as being in
     * the LEVEL_COMPLETE state
     */
    private void levelComplete() {
        gameState = GameState.LEVEL_COMPLETE;
        timer.stop();
        // draw the level complete scene
        // to inform the player that they have
        // completed the level
        repaint();
    }

    /**
     * mark the game as being over
     */
    private void gameOver() {
        gameState = GameState.GAME_OVER;
        timer.stop();
        // draw the game over scene
        repaint();
    }

    /**
     * move searches, particles, and check if
     * the player has eaten food, eaten all the foods,
     * been found by a search, or if the searches have
     * finished
     */
    private void updateGame() {
        // check if the player has collected food
        int xPlayer = player.currentPosition.x;
        int yPlayer = player.currentPosition.y;
        for (List<Integer> aFood : foods) {
            int x = aFood.get(0);
            int y = aFood.get(1);
            if (x == xPlayer && y == yPlayer) {
                foods.remove(aFood);
                if (foods.size() == 0) {
                    // the player has eaten all
                    // the foods, so this level
                    // is complete
                    levelComplete();
                } else {
                    // if the game is still going, create
                    // an explosion of particles to inform the
                    // player that they ate a food
                    Particle[] newParticles = mapGenerator.newParticles(x, y, squareSize);
                    Collections.addAll(particles, newParticles);
                }
                // since foods cannot be spawned on top of each
                // other, we can safely assume that the player
                // only ate one food
                break;
            }
        }
        // check if the player has been found by a search
        for (Search search : searches) {
            int x = search.currentPosition.x;
            int y = search.currentPosition.y;
            if (x == xPlayer && y == yPlayer) {
                // remember the type of search that found
                // the player
                typeOfSearchFoundPlayer = search.searchType;
                gameOver();
            }
        }
        // move any particles that were spawned when
        // the player ate food
        for (Particle aParticle : particles) {
            aParticle.move();
        }
        // delete any particles that are now off-screen
        // NOTE: particles are drawn even when they are outside the map
        particles.removeIf(particle -> particle.isOutOfBounds(xOffset, yOffset, viewWidth, viewHeight));

        // check if the searches should advance
        ticksTilSearchMove--;
        if (ticksTilSearchMove <= 0) {
            ticksTilSearchMove = ticksBetweenSearchMoves;
            // this is decremented once for all searches
            // because the searches always advance at the
            // same time, so they will be done searching
            // at the same time
            numUnSearchedSquares--;
            // give the squares the searches are at their
            // corresponding color so they player can see
            // where the searches have been
            for (Search search : searches) {
                int x = search.currentPosition.x;
                int y = search.currentPosition.y;
                squares[y][x].addColor(search.visitedColor);
            }
            // move the searches
            for (Search search : searches) {
                search.move();
            }
        }
        // end the game if the player loses by
        // letting all the searches finish
        if (numUnSearchedSquares == 0) {
            gameOver();
        }
    }

    /**
     * called by the timer
     * used to draw and update the game at
     * a constant time interval
     * @param e unused
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        // avoid updating the game if it is
        // paused, over, or has not started
        if (gameState == GameState.PLAYING) {
            updateGame();
        }
    }

    /**
     * called whenever repaint() is called
     * draws the entire game
     * @param g the graphics instance that draws in the JPanel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // draw the background of the entire JPanel
        g2d.setColor(Parameters.backGroundColor);
        g2d.fillRect(0, 0, viewWidth, viewHeight);
        // draw the background of the map
        g2d.setColor(Parameters.mapColor);
        g2d.fillRect(xOffset, yOffset, width * squareSize, height * squareSize);
        drawSquares(g2d);
        drawFoods(g2d);
        drawPlayer(g2d);
        drawSearches(g2d);
        drawWalls(g2d);
        drawLevelBanner(g2d);
        drawParticles(g2d);
        if (gameState == GameState.MENU) {
            drawMenu(g2d);
        } else if (gameState == GameState.PAUSED) {
            drawPauseScreen(g2d);
        } else if (gameState == GameState.LEVEL_COMPLETE) {
            drawLevelCompleteScreen(g2d);
        } else if (gameState == GameState.GAME_OVER) {
            drawGameOverScreen(g2d);
        }
        // update the JPanel with the new game drawing
        Toolkit.getDefaultToolkit().sync();
        // get rid of g2d since it was made from Graphics g,
        // so it will not be automatically disposed of
        g2d.dispose();
    }

    /**
     * draw the overlay the player will see when the game is
     * over. only shown when the player has lost the game
     * @param g2d graphics used to draw on the JPanel
     */
    private void drawGameOverScreen(Graphics2D g2d) {
        String gameOverString;
        // tell the player why they lost the game
        if (typeOfSearchFoundPlayer == null) {
            gameOverString = "The searches ended. You lose.";
        } else {
            gameOverString = "You were found by a " + typeOfSearchFoundPlayer;
        }
        // tell they player what they can do now
        String playAgainString = "Press enter to play again";
        String quitString = "Press escape to quit";
        drawOverlay(g2d, gameOverString, playAgainString, quitString);
    }

    /**
     * draw the overly the player see after completing a level
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawLevelCompleteScreen(Graphics2D g2d) {
        // tell the player what level they completed
        String levelString = "Level " + level + " complete";
        // tell the player what they can do now
        String nextLevelString = "Press enter to play next level";
        String quitString = "Press escape to quit";
        drawOverlay(g2d, levelString, nextLevelString, quitString);
    }

    /**
     * draw the overlay the player sees when the game is paused
     * @param g2d the graphics for drawing on the JPanel
     */
    private void drawPauseScreen(Graphics2D g2d) {
        drawOverlay(g2d, "Paused", "Press enter to resume", "Press escape to quit");
    }

    /**
     * draw the overly the player sees when first launching the game
     * @param g2d the graphics for drawing on the JPanel
     */
    private void drawMenu(Graphics2D g2d) {
        // tell the user how to play, pause, and quit the game
        drawOverlay(g2d, "Use arrow keys to move", "Press enter to play", "Press escape to quit");
        // draw the title of the game in big text
        Font font = new Font("Helvetica", Font.BOLD, viewHeight / 24);
        g2d.setFont(font);
        g2d.setColor(Parameters.textColor);
        // put the title on the map and space it two squares above the other text
        g2d.drawString(Parameters.title, xOffset + squareSize, yOffset + squareSize * (height - 7));
    }

    /**
     * used for drawing menu, pause screen, level complete screen, and game over screen
     * @param g2d the graphics for drawing on the JPanel
     * @param string1 the first string that will be drawn
     * @param string2 second string to draw
     * @param string3 third string to draw
     */
    private void drawOverlay(Graphics2D g2d, String string1, String string2, String string3) {
        // draw a transparent rectangle over the map to make the text easier to read
        g2d.setColor(Parameters.overlayColor);
        g2d.fillRect(xOffset, yOffset, width * squareSize, height * squareSize);
        // base the font size on the view height so that the text will scale based on
        // different view sizes
        Font font = new Font("Helvetica", Font.BOLD, viewHeight / 36);
        g2d.setFont(font);
        // draw the text
        g2d.setColor(Parameters.textColor);
        // put the text on the map and give each string a vertical spacing of
        // two squares between the next string
        g2d.drawString(string1, xOffset + squareSize, yOffset + squareSize * (height - 5));
        g2d.drawString(string2, xOffset + squareSize, yOffset + squareSize * (height - 3));
        g2d.drawString(string3, xOffset + squareSize, yOffset + squareSize * (height - 1));
    }

    /**
     * tells the player what level they are playing,
     * even when the game is in the PLAYING state
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawLevelBanner(Graphics2D g2d) {
        // base the font size on the view height so that the text will scale based on
        // different view sizes
        Font font = new Font("Helvetica", Font.BOLD, viewHeight / 12);
        g2d.setFont(font);
        g2d.setColor(Parameters.textColor);
        String levelString = "Level " + level;
        // put the text on the left side of the top of the map
        g2d.drawString(levelString, xOffset, yOffset);
    }

    /**
     * draw any particles that were spawned when
     * the player collected food
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawParticles(Graphics2D g2d) {
        g2d.setColor(Parameters.foodColor);
        for (Particle aParticle : particles) {
            g2d.fillRect(xOffset + aParticle.leftX, yOffset + aParticle.topY, aParticle.width, aParticle.height);
        }
    }

    /**
     * draw the food that player has still not collected
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawFoods(Graphics2D g2d) {
        g2d.setColor(Parameters.foodColor);
        for (List<Integer> aFood : foods) {
            // multiplying the coordinates of the square grid
            // by the squareSize provides the x and y coordinates on the JPanel
            int leftX = aFood.get(0) * squareSize;
            int topY = aFood.get(1) * squareSize;
            // xOffset and yOffset are needed to place the foods in the map
            g2d.fillRect(xOffset + leftX, yOffset + topY, squareSize, squareSize);
        }
    }

    /**
     * draw the squares that have been searched
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawSquares(Graphics2D g2d) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Square aSquare = squares[i][j];
                if (aSquare.color != null) {
                    g2d.setColor(aSquare.color);
                    int leftX = j * squareSize;
                    int topY = i * squareSize;
                    g2d.fillRect(xOffset + leftX, yOffset + topY, squareSize, squareSize);
                }
            }
        }
    }

    /**
     * draw the player
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawPlayer(Graphics2D g2d) {
        g2d.setColor(Parameters.playerColor);
        int playerX = player.currentPosition.x * squareSize;
        int playerY = player.currentPosition.y * squareSize;
        g2d.fillRect(xOffset + playerX, yOffset + playerY, squareSize, squareSize);
    }

    /**
     * draw where the searches are at this time.
     * these locations are what the player must avoid
     * to avoid being found by the searches
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawSearches(Graphics2D g2d) {
        g2d.setColor(Parameters.searchColor);
        for (Search search : searches) {
            int leftX = search.currentPosition.x * squareSize;
            int topY = search.currentPosition.y * squareSize;
            g2d.fillRect(xOffset + leftX, yOffset + topY, squareSize, squareSize);
        }
    }

    /**
     * draw the walls that make up the map
     * @param g2d graphics for drawing on the JPanel
     */
    private void drawWalls(Graphics2D g2d) {
        g2d.setColor(Parameters.wallColor);
        // we will use lines instead of rectangles
        g2d.setStroke(new BasicStroke(Parameters.wallWidth));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Square aSquare = squares[i][j];
                int leftX = j * squareSize;
                int rightX = leftX + squareSize;
                int topY = i * squareSize;
                int bottomY = topY + squareSize;
                // walls are represented in the map as
                // squares having null neighbors
                if (aSquare.getTopNeighbor() == null) {
                    g2d.drawLine(xOffset + leftX, yOffset + topY, xOffset + rightX, yOffset + topY);
                }
                if (aSquare.getBottomNeighbor() == null) {
                    g2d.drawLine(xOffset + leftX, yOffset + bottomY, xOffset + rightX, yOffset + bottomY);
                }
                if (aSquare.getLeftNeighbor() == null) {
                    g2d.drawLine(xOffset + leftX, yOffset + topY, xOffset + leftX, yOffset + bottomY);
                }
                if (aSquare.getRightNeighbor() == null) {
                    g2d.drawLine(xOffset + rightX, yOffset + topY, xOffset + rightX, yOffset + bottomY);
                }
            }
        }
    }
}