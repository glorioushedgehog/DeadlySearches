import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
    private Square[][] squares;
    private int viewWidth;
    private int viewHeight;
    private int width;
    private int height;
    private int squareSize;
    private int ticksBetweenSearchMoves;
    private int ticksTilSearchMove;
    private Search[] searches;
    private Food[] foods;
    Player player;
    Timer timer;

    Game(int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.width = Parameters.numSquaresInRow;
        this.height = Parameters.numRowsOfSquares;
        this.squareSize = Math.min(Parameters.mapWidth / width, Parameters.mapHeight / height);
        addKeyListener(new KeyResponder(this));
        // in order for key detection to work, the game
        // has to be focusable
        setFocusable(true);
        MapGenerator mapGenerator = new MapGenerator(width, height);
        this.squares = mapGenerator.generateMap();
        this.player = new Player(squares[height / 2][width / 2]);
        this.searches = new Search[]{
                new Search(SearchType.BFS, squares[height / 4][width / 4], DirectionPriority.RIGHT_THEN_DOWN),
                new Search(SearchType.DFS, squares[height / 4][3 * width / 4], DirectionPriority.LEFT_THEN_DOWN),
                new Search(SearchType.DFS, squares[3 * height / 4][width / 4], DirectionPriority.RIGHT_THEN_UP),
                new Search(SearchType.BFS, squares[3 * height / 4][3 * width / 4], DirectionPriority.LEFT_THEN_UP)
        };
        this.foods = new Food[Parameters.numFoods];
        Random random = new Random();
        for(int i = 0; i < foods.length; i++){
            foods[i] = new Food(random.nextInt(width), random.nextInt(height));
        }
        this.ticksBetweenSearchMoves = (int) (((double) Parameters.searchDelay) / Parameters.tickDelay);
        this.ticksTilSearchMove = ticksBetweenSearchMoves;
        this.timer = new Timer(Parameters.tickDelay, this);
        timer.start();
    }

    private void updateGame() {
        int xPlayer = player.currentPosition.x;
        int yPlayer = player.currentPosition.y;

        for(int i = 0; i < foods.length; i++){
            Food aFood = foods[i];
            if(aFood != null) {
                int x = aFood.x;
                int y = aFood.y;
                if (x == xPlayer && y == yPlayer) {
                    foods[i] = null;
                    System.out.println("you ate some food !");
                }
            }
        }
        for (Search search : searches) {
            int x = search.currentPosition.x;
            int y = search.currentPosition.y;
            if (x == xPlayer && y == yPlayer) {
                timer.stop();
                System.out.println("you were found by a " + search.searchType);
            }
        }

        ticksTilSearchMove--;
        if (ticksTilSearchMove <= 0) {
            ticksTilSearchMove = ticksBetweenSearchMoves;
            for (Search search : searches) {
                int x = search.currentPosition.x;
                int y = search.currentPosition.y;
                squares[y][x].addColor(search.visitedColor);
            }
            for (Search search : searches) {
                search.move();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Parameters.mapColor);
        g2d.fillRect(0, 0, Parameters.mapWidth, Parameters.mapHeight);
        drawSquares(g2d);
        drawFoods(g2d);
        drawPlayer(g2d);
        drawSearches(g2d);
        drawWalls(g2d);
//        drawMaze(g2d);
//        drawScore(g2d);
//        doAnim();
//        if (inGame) {
//            playGame(g2d);
//        } else {
//            showIntroScreen(g2d);
//        }
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void drawFoods(Graphics2D g2d) {
        g2d.setColor(Parameters.foodColor);
        for(Food aFood : foods){
            if (aFood != null) {
                int leftX = aFood.x * squareSize;
                int topY = aFood.y * squareSize;
                g2d.fillRect(leftX, topY, squareSize, squareSize);
            }
        }
    }

    private void drawSquares(Graphics2D g2d) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Square aSquare = squares[i][j];
                if (aSquare.color != null) {
                    g2d.setColor(aSquare.color);
                    int leftX = j * squareSize;
                    int topY = i * squareSize;
                    g2d.fillRect(leftX, topY, squareSize, squareSize);
                }
            }
        }
    }

    private void drawPlayer(Graphics2D g2d) {
        g2d.setColor(Parameters.playerColor);
        int playerX = player.currentPosition.x * squareSize;
        int playerY = player.currentPosition.y * squareSize;
        g2d.fillRect(playerX, playerY, squareSize, squareSize);
    }

    private void drawSearches(Graphics2D g2d) {
        g2d.setColor(Parameters.searchColor);
        for (Search search : searches) {
            int leftX = search.currentPosition.x * squareSize;
            int topY = search.currentPosition.y * squareSize;
            g2d.fillRect(leftX, topY, squareSize, squareSize);
        }
    }

    private void drawWalls(Graphics2D g2d) {
        g2d.setColor(Parameters.wallColor);
        g2d.setStroke(new BasicStroke(Parameters.wallWidth));
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Square aSquare = squares[i][j];
                int leftX = j * squareSize;
                int rightX = leftX + squareSize;
                int topY = i * squareSize;
                int bottomY = topY + squareSize;
                if (aSquare.getTopNeighbor() == null) {
                    g2d.drawLine(leftX, topY, rightX, topY);
                }
                if (aSquare.getBottomNeighbor() == null) {
                    g2d.drawLine(leftX, bottomY, rightX, bottomY);
                }
                if (aSquare.getLeftNeighbor() == null) {
                    g2d.drawLine(leftX, topY, leftX, bottomY);
                }
                if (aSquare.getRightNeighbor() == null) {
                    g2d.drawLine(rightX, topY, rightX, bottomY);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        updateGame();
    }
}