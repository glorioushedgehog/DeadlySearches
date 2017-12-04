/**
 * particles are spawned when the player
 * collects food. they move in a predetermined
 * direction until they leave the view, when they
 * are deleted
 */
class Particle {
    // the y-coordinate of the particle relative to the top
    // left corner of the map
    int topY;
    // the x-coordinate of the particle relative to the top
    // left corner of the map
    int leftX;
    int width;
    int height;
    // dx and dy are added to the particle's
    // position every tick, regardless of the tick rate
    private int dx;
    private int dy;

    /**
     * make a new particle
     *
     * @param leftX  x-coordinate of the particle relative to the top left corner of the map
     * @param topY   y-coordinate of the particle relative to the top left corner of the map
     * @param width  the width of the particle
     * @param height the height of the particle
     * @param dx     the change in the particle's x-coordinate that will occur ever tick
     * @param dy     the change in the particle's y-coordinate that will occur ever tick
     */
    Particle(int leftX, int topY, int width, int height, int dx, int dy) {
        this.leftX = leftX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * move the particle
     */
    void move() {
        leftX += dx;
        topY += dy;
    }

    /**
     * tell whether or not the particle is off-screen
     * @param xOffset distance between left edge of map and left edge of screen
     * @param yOffset distance between top edge of map and top edge of screen
     * @param viewWidth the width of the whole view
     * @param viewHeight the height of the whole view
     * @return true if the particle is out of bounds, false otherwise
     */
    boolean isOutOfBounds(int xOffset, int yOffset, int viewWidth, int viewHeight) {
        // this only evaluates to true if the particle is off-screen. it is not
        // enough to be outside the map. particles are rendered until they would be
        // invisible
        return leftX + width < -xOffset || topY + height < -yOffset || leftX > viewWidth - xOffset || topY > viewHeight - yOffset;
    }
}
