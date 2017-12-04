import java.util.*;

/**
 * randomly creates new maps
 * and randomly fills them with food
 */
class MapGenerator {
    // the number of squares in each row of the map
    private int width;
    // the number of rows of squares in the map
    private int height;
    private Random random;

    /**
     * prepare to generate maps.
     * this is only done once during the game's life
     * @param width the number of squares in each row of the map
     * @param height the number of rows of squares in the map
     */
    MapGenerator(int width, int height){
        this.width = width;
        this.height = height;
        this.random = new Random();
    }

    /**
     * randomly determine where walls are placed
     * in the map
     * @return the new map
     */
    Square[][] generateMap() {
        Square[][] squares = new Square[this.height][this.width];
        // initialize the squares and give each square the x and y
        // coordinates of where is in they map. the coordinates are
        // needed for drawing the squares
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                squares[i][j] = new Square(j, i);
            }
        }
        // tell each square that its right neighbor is the
        // square to the right of it and its left neighbor is the
        // square to the left of it
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width - 1; j++) {
                Square leftSquare = squares[i][j];
                Square rightSquare = squares[i][j + 1];
                leftSquare.setRightNeighbor(rightSquare);
                rightSquare.setLeftNeighbor(leftSquare);
            }
        }
        // tell each square that its top neighbor is the
        // square on top of it and its bottom neighbor is the
        // square below it
        for (int i = 0; i < this.height - 1; i++) {
            for (int j = 0; j < this.width; j++) {
                Square topSquare = squares[i][j];
                Square bottomSquare = squares[i + 1][j];
                topSquare.setBottomNeighbor(bottomSquare);
                bottomSquare.setTopNeighbor(topSquare);
            }
        }
        // now, actually generate the walls
        // this hashSet will keep track of all lattice points
        // in the map that could still be one of the two lattice
        // points defining a wall
        HashSet<List<Integer>> notDone = new HashSet<>();
        // add all the map's lattice points to the hashSet
        for (int i = 0; i < this.height + 1; i++) {
            for (int j = 0; j < this.width + 1; j++) {
                notDone.add(Arrays.asList(i, j));
            }
        }
        // this hashSet keeps track of all lattice points that
        // could be used to start a series of walls, where each wall
        // in the series touches some other wall in the series and
        // no three walls all touch each other
        HashSet<List<Integer>> startingPoints = new HashSet<>();
        // add all lattice points except the edge points to the hashSet.
        // the edge points are not added because if they were, a series
        // of walls could start and end on the map's edge, which would
        // result in a part of the map that cannot be accessed
        for (int i = 1; i < this.height; i++) {
            for (int j = 1; j < this.width; j++) {
                startingPoints.add(Arrays.asList(i, j));
            }
        }
        // while there are still possible starting points, try to add
        // new walls
        while (startingPoints.size() != 0) {
            // pick a new starting point
            List<Integer> current = startingPoints.iterator().next();
            // add walls to the series of walls until no more walls
            // can be added
            while (true) {
                // mark this lattice point as done so no other wall
                // will touch it
                notDone.remove(current);
                // ensure that a different series of walls isn't started
                // on this lattice point
                startingPoints.remove(current);
                int i = current.get(0);
                int j = current.get(1);
                ArrayList<List<Integer>> neighbors = new ArrayList<>();
                ArrayList<List<Integer>> validNeighbors = new ArrayList<>();
                // enumerate all of this lattice point's neighbors
                // (the lattice points that are distance 1 away from it)
                neighbors.add(Arrays.asList(i, j + 1));
                neighbors.add(Arrays.asList(i, j - 1));
                neighbors.add(Arrays.asList(i + 1, j));
                neighbors.add(Arrays.asList(i - 1, j));
                // only consider the neighbors that do not already
                // have walls touching them
                for (List<Integer> aNeighbor : neighbors) {
                    if (notDone.contains(aNeighbor)) {
                        validNeighbors.add(aNeighbor);
                    }
                }
                // if there are no possible new walls to make,
                // this series of walls is done and we should move
                // on to the next
                if (validNeighbors.size() == 0) {
                    break;
                }
                // if there are possible walls to be made, randomly
                // pick one to make
                int index = random.nextInt(validNeighbors.size());
                current = validNeighbors.get(index);
                int newI = current.get(0);
                int newJ = current.get(1);
                // put the new wall we chose in the map
                // by setting the correct neighbor fields in the squares
                // to null
                if (newI != i) {
                    // if the new lattice point has a different i value,
                    // this wall is a vertical wall.
                    // only make a wall if it is in between two squares
                    if (j != 0 && j != width) {
                        newI = Math.min(newI, i);
                        Square leftSquare = squares[newI][j - 1];
                        Square rightSquare = squares[newI][j];
                        leftSquare.removeRightNeighbor();
                        rightSquare.removeLeftNeighbor();
                    }
                } else {
                    // if the new lattice point has the same i value, it
                    // must have a different j value, so
                    // this wall is a horizontal wall.
                    // only make a wall if it is in between two squares
                    if (i != 0 && i != height) {
                        newJ = Math.min(newJ, j);
                        Square topSquare = squares[i - 1][newJ];
                        Square bottomSquare = squares[i][newJ];
                        topSquare.removeBottomNeighbor();
                        bottomSquare.removeTopNeighbor();
                    }
                }
                // don't add any more walls if this wall touches
                // the map's edge. this prevents having a series of walls
                // that touches the edge twice, which would partition the
                // map into two completely walled-off areas
                if (isOnMapBorder(current)) {
                    break;
                }
            }
        }
        return squares;
    }

    /**
     * tell if a given lattice point is on the map's edge
     * @param position the lattice point
     * @return true if the point is on the map's edge, false otherwise
     */
    private boolean isOnMapBorder(List<Integer> position) {
        int x = position.get(1);
        int y = position.get(0);
        return x == 0 || x == width || y == 0 || y == height;
    }

    /**
     * randomly add foods to the map
     * @param numFoods the number of foods that will be added
     * @param playerPosition the position of the player, where no food will be spawned
     * @return the locations of the foods
     */
    HashSet<List<Integer>> generateFood(int numFoods, Square playerPosition) {
        int playerX = playerPosition.x;
        int playerY = playerPosition.y;
        List<Integer> playerCoordinates = Arrays.asList(playerX, playerY);
        HashSet<List<Integer>> foods = new HashSet<>();
        for(int i = 0; i < numFoods; i++){
            List<Integer> aFood = Arrays.asList(random.nextInt(width), random.nextInt(height));
            // avoid putting food where the player spawns or on top of
            // other food
            while(foods.contains(aFood) || aFood == playerCoordinates){
                aFood = Arrays.asList(random.nextInt(width), random.nextInt(height));
            }
            foods.add(aFood);
        }
        return foods;
    }

    /**
     * make new particles to draw at the location
     * where the player ate a food
     * @param x the x coordinate of the food the player ate
     * @param y the y coordinate of the food the player ate
     * @param squareSize the size of the squares in the map
     * @return an array of particles
     */
    Particle[] newParticles(int x, int y, int squareSize){
        // find the coordinates of the particles relative to the
        // top left corner of the map
        int leftX = x * squareSize;
        int topY = y * squareSize;
        // make the particles smaller than the food so they look reasonable
        int particleWidth = squareSize / 3;
        int particleHeight = squareSize / 3;
        // create particle's at the food's location with random variations
        // in size and velocity
        return new Particle[] {
                new Particle(leftX, topY, particleWidth+4, particleHeight+1, 11, 10),
                new Particle(leftX, topY, particleWidth+1, particleHeight+5, -10, 12),
                new Particle(leftX, topY, particleWidth+5, particleHeight+2, 7, -14),
                new Particle(leftX, topY, particleWidth+2, particleHeight+3, -9, -10),
                new Particle(leftX, topY, particleWidth+3, particleHeight+1, 14, 2),
                new Particle(leftX, topY, particleWidth+1, particleHeight+5, -14, 3),
                new Particle(leftX, topY, particleWidth+5, particleHeight+1, 1, 14),
                new Particle(leftX, topY, particleWidth+1, particleHeight+4, 0, -12)
        };
    }
}
