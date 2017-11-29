import java.util.*;

class MapGenerator {
    private int width;
    private int height;
    private Random random;
    MapGenerator(int width, int height){
        this.width = width;
        this.height = height;
        this.random = new Random();
    }
    Square[][] generateMap() {
        Square[][] squares = new Square[this.height][this.width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                squares[i][j] = new Square(j, i);
            }
        }
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width - 1; j++) {
                Square leftSquare = squares[i][j];
                Square rightSquare = squares[i][j + 1];
                leftSquare.setRightNeighbor(rightSquare);
                rightSquare.setLeftNeighbor(leftSquare);
            }
        }
        for (int i = 0; i < this.height - 1; i++) {
            for (int j = 0; j < this.width; j++) {
                Square topSquare = squares[i][j];
                Square bottomSquare = squares[i + 1][j];
                topSquare.setBottomNeighbor(bottomSquare);
                bottomSquare.setTopNeighbor(topSquare);
            }
        }
        HashSet<List<Integer>> notDone = new HashSet<>();
        for (int i = 0; i < this.height + 1; i++) {
            for (int j = 0; j < this.width + 1; j++) {
                notDone.add(Arrays.asList(i, j));
            }
        }
        HashSet<List<Integer>> startingPoints = new HashSet<>();
        for (int i = 1; i < this.height; i++) {
            for (int j = 1; j < this.width; j++) {
                startingPoints.add(Arrays.asList(i, j));
            }
        }
        while (startingPoints.size() != 0) {
            List<Integer> current = startingPoints.iterator().next();
            while (true) {
                notDone.remove(current);
                startingPoints.remove(current);
                int i = current.get(0);
                int j = current.get(1);
                ArrayList<List<Integer>> neighbors = new ArrayList<>();
                ArrayList<List<Integer>> validNeighbors = new ArrayList<>();
                neighbors.add(Arrays.asList(i, j + 1));
                neighbors.add(Arrays.asList(i, j - 1));
                neighbors.add(Arrays.asList(i + 1, j));
                neighbors.add(Arrays.asList(i - 1, j));
                for (List<Integer> aNeighbor : neighbors) {
                    if (notDone.contains(aNeighbor)) {
                        validNeighbors.add(aNeighbor);
                    }
                }
                if (validNeighbors.size() == 0) {
                    break;
                }
                int index = random.nextInt(validNeighbors.size());
                current = validNeighbors.get(index);
                int newI = current.get(0);
                int newJ = current.get(1);
                if (newI != i) {
                    if (j != 0 && j != width) {
                        newI = Math.min(newI, i);
                        Square leftSquare = squares[newI][j - 1];
                        Square rightSquare = squares[newI][j];
                        leftSquare.removeRightNeighbor();
                        rightSquare.removeLeftNeighbor();
                    }
                } else {
                    if (i != 0 && i != height) {
                        newJ = Math.min(newJ, j);
                        Square topSquare = squares[i - 1][newJ];
                        Square bottomSquare = squares[i][newJ];
                        topSquare.removeBottomNeighbor();
                        bottomSquare.removeTopNeighbor();
                    }
                }
                if (isOnMapBorder(current)) {
                    break;
                }
            }
        }
        return squares;
    }

    private boolean isOnMapBorder(List<Integer> position) {
        int x = position.get(1);
        int y = position.get(0);
        return x == 0 || x == width || y == 0 || y == height;
    }

    HashSet<List<Integer>> generateFood(int numFoods) {
        HashSet<List<Integer>> foods = new HashSet<>();
        for(int i = 0; i < numFoods; i++){
            List<Integer> aFood = Arrays.asList(random.nextInt(width), random.nextInt(height));
            while(foods.contains(aFood)){
                aFood = Arrays.asList(random.nextInt(width), random.nextInt(height));
            }
            foods.add(aFood);
        }
        return foods;
    }

    Particle[] newParticles(int x, int y, int squareSize){
        return new Particle[] {
                new Particle(x * squareSize, y * squareSize, 10, 10, 10, 10),
                new Particle(x * squareSize, y * squareSize, 10, 10, -10, 10),
                new Particle(x * squareSize, y * squareSize, 10, 10, 10, -10),
                new Particle(x * squareSize, y * squareSize, 10, 10, -10, -10)
        };
    }
}
