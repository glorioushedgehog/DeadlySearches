import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Game extends JPanel implements ActionListener {
    private Square[][] squares;
    private int viewWidth;
    private int viewHeight;
    private int width;
    private int height;
    private int squareSize;
    private int ticksBetweenSearchMoves;
    private int ticksTilSearchMove;
    Player player;
    Search[] searches;
    Timer timer;
    Game(int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.width = Parameters.numSquaresInRow;
        this.height = Parameters.numRowsOfSquares;
        this.squareSize = viewHeight / width;
        addKeyListener(new KeyResponder(this));
        // in order for key detection to work, the game
        // has to be focusable
        setFocusable(true);
        MapGenerator mapGenerator = new MapGenerator(width, height);
        this.squares = mapGenerator.generateMap();
        this.player = new Player(squares[height / 2][width / 2]);
        this.searches = new Search[]{
                new Search(SearchType.BFS, squares[0][0], DirectionPriority.DOWN_THEN_RIGHT, new Color(178, 249, 181)),
                new Search(SearchType.DFS, squares[0][width - 1], DirectionPriority.DOWN_THEN_LEFT, new Color(248, 165, 228)),
                new Search(SearchType.DFS, squares[height - 1][0], DirectionPriority.RIGHT_THEN_UP, new Color(169, 159, 249)),
                new Search(SearchType.BFS, squares[height - 1][width - 1], DirectionPriority.LEFT_THEN_UP, new Color(248, 162, 162))
        };
        this.ticksBetweenSearchMoves = (int) (((double) Parameters.searchDelay) / Parameters.tickDelay);
        this.ticksTilSearchMove = 0;
        this.timer = new Timer(Parameters.tickDelay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateGame(g);
    }

    private void updateGame(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Parameters.mapColor);
        g2d.fillRect(0, 0, Parameters.mapWidth, Parameters.mapHeight);
        drawSquares(g2d);
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

        for (Search search : searches) {
            int x = search.currentPosition.x;
            int y = search.currentPosition.y;
            int xPlayer = player.currentPosition.x;
            int yPlayer = player.currentPosition.y;
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
    private void drawSquares(Graphics2D g2d){
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Square aSquare = squares[i][j];
                if (aSquare.color != null) {
                    g2d.setColor(aSquare.color);
                    int leftX = j * this.squareSize;
                    int topY = i * this.squareSize;
                    g2d.fillRect(leftX, topY, this.squareSize, this.squareSize);
                }
            }
        }
    }
    private void drawPlayer(Graphics2D g2d){
        g2d.setColor(Parameters.playerColor);
        int playerX = this.player.currentPosition.x * this.squareSize;
        int playerY = this.player.currentPosition.y * this.squareSize;
        g2d.fillRect(playerX, playerY, this.squareSize, this.squareSize);
    }
    private void drawSearches(Graphics2D g2d){
        g2d.setColor(Parameters.searchColor);
        for (Search search : searches) {
            int leftX = search.currentPosition.x * this.squareSize;
            int topY = search.currentPosition.y * this.squareSize;
            g2d.fillRect(leftX, topY, this.squareSize, this.squareSize);
        }
    }
    private void drawWalls(Graphics2D g2d){
        g2d.setColor(Parameters.wallColor);
        g2d.setStroke(new BasicStroke(Parameters.wallWidth));
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                Square aSquare = squares[i][j];
                int leftX = j * this.squareSize;
                int rightX = leftX + this.squareSize;
                int topY = i * this.squareSize;
                int bottomY = topY + this.squareSize;
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
    }
}