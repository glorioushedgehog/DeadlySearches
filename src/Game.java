import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Game extends JPanel implements ActionListener {
    private MapGenerator mapGenerator;
    private Square[][] squares;
    private int viewWidth;
    private int viewHeight;
    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private int numUnSearchedSquares;
    private int squareSize;
    private int ticksBetweenSearchMoves;
    private int ticksTilSearchMove;
    private Search[] searches;
    private HashSet<List<Integer>> foods;
    private HashSet<Particle> particles;
    Player player;
    Timer timer;

    Game(int viewWidth, int viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.width = Parameters.numSquaresInRow;
        this.height = Parameters.numRowsOfSquares;
        this.numUnSearchedSquares = width * height - 1;
        int maxHorizontalSquareSize = (int) (viewWidth * (1 - Parameters.horizontalPadding) / width);
        int maxVerticalSquareSize = (int) (viewHeight * (1 - Parameters.verticalPadding) / height);
        this.squareSize = Math.min(maxHorizontalSquareSize, maxVerticalSquareSize);
        this.xOffset = (viewWidth - width * squareSize) / 2;
        this.yOffset = (viewHeight - height * squareSize) / 2;
        addKeyListener(new KeyResponder(this));
        // in order for key detection to work, the game
        // has to be focusable
        setFocusable(true);
        this.mapGenerator = new MapGenerator(width, height);
        this.squares = mapGenerator.generateMap();
        this.player = new Player(squares[height / 2][width / 2]);
        this.searches = new Search[]{
                new Search(SearchType.BFS, squares[height / 4][width / 4], DirectionPriority.RIGHT_THEN_DOWN),
                new Search(SearchType.DFS, squares[height / 4][3 * width / 4], DirectionPriority.LEFT_THEN_DOWN),
                new Search(SearchType.DFS, squares[3 * height / 4][width / 4], DirectionPriority.RIGHT_THEN_UP),
                new Search(SearchType.BFS, squares[3 * height / 4][3 * width / 4], DirectionPriority.LEFT_THEN_UP)
        };
        this.foods = mapGenerator.generateFood(Parameters.numFoods, player.currentPosition);
        this.particles = new HashSet<>();
        this.ticksBetweenSearchMoves = (int) (((double) Parameters.searchDelay) / Parameters.tickDelay);
        this.ticksTilSearchMove = ticksBetweenSearchMoves;
        this.timer = new Timer(Parameters.tickDelay, this);
        timer.start();
    }

    private void updateGame() {
        int xPlayer = player.currentPosition.x;
        int yPlayer = player.currentPosition.y;
        for (List<Integer> aFood : foods) {
            int x = aFood.get(0);
            int y = aFood.get(1);
            if (x == xPlayer && y == yPlayer) {
                foods.remove(aFood);
                if (foods.size() == 0) {
                    timer.stop();
                    System.out.println("you ate ALL the food !");
                } else {
                    Particle[] newParticles = mapGenerator.newParticles(x, y, squareSize);
                    Collections.addAll(particles, newParticles);
                }
                break;
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

        for (Particle aParticle : particles) {
            aParticle.move();
        }
        particles.removeIf(particle -> particle.isOutOfBounds(xOffset, yOffset, viewWidth, viewHeight));

        ticksTilSearchMove--;
        if (ticksTilSearchMove <= 0) {
            ticksTilSearchMove = ticksBetweenSearchMoves;
            numUnSearchedSquares--;
            for (Search search : searches) {
                int x = search.currentPosition.x;
                int y = search.currentPosition.y;
                squares[y][x].addColor(search.visitedColor);
            }
            for (Search search : searches) {
                search.move();
            }
        }

        if (numUnSearchedSquares == 0) {
            timer.stop();
            System.out.println("the searches finished !");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Parameters.mapColor);
        g2d.fillRect(xOffset, yOffset, width * squareSize, height * squareSize);
        drawSquares(g2d);
        drawFoods(g2d);
        drawPlayer(g2d);
        drawSearches(g2d);
        drawWalls(g2d);
        drawParticles(g2d);
//        drawMaze(g2d);
//        drawScore(g2d);
//        doAnim();
//        if (inGame) {
//            playGame(g2d);
//        } else {
//            showIntroScreen(g2d);
//        }
        String s;
        Font smallFont = new Font("Helvetica", Font.BOLD, viewHeight/12);
        g.setFont(smallFont);
        g.setColor(new Color(61, 32, 151));
        s = "Level " + 1;
        g.drawString(s, xOffset, yOffset);

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void drawParticles(Graphics2D g2d) {
        g2d.setColor(Parameters.foodColor);
        for (Particle aParticle : particles) {
            g2d.fillRect(xOffset + aParticle.leftX, yOffset + aParticle.topY, aParticle.width, aParticle.height);
        }
    }

    private void drawFoods(Graphics2D g2d) {
        g2d.setColor(Parameters.foodColor);
        for (List<Integer> aFood : foods) {
            int leftX = aFood.get(0) * squareSize;
            int topY = aFood.get(1) * squareSize;
            g2d.fillRect(xOffset + leftX, yOffset + topY, squareSize, squareSize);
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
                    g2d.fillRect(xOffset + leftX, yOffset + topY, squareSize, squareSize);
                }
            }
        }
    }

    private void drawPlayer(Graphics2D g2d) {
        g2d.setColor(Parameters.playerColor);
        int playerX = player.currentPosition.x * squareSize;
        int playerY = player.currentPosition.y * squareSize;
        g2d.fillRect(xOffset + playerX, yOffset + playerY, squareSize, squareSize);
    }

    private void drawSearches(Graphics2D g2d) {
        g2d.setColor(Parameters.searchColor);
        for (Search search : searches) {
            int leftX = search.currentPosition.x * squareSize;
            int topY = search.currentPosition.y * squareSize;
            g2d.fillRect(xOffset + leftX, yOffset + topY, squareSize, squareSize);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        updateGame();
    }
}