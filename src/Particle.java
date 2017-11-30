class Particle {
    int topY;
    int leftX;
    int width;
    int height;
    private int dx;
    private int dy;
    Particle(int leftX, int topY, int width, int height, int dx, int dy){
        this.leftX = leftX;
        this.topY = topY;
        this.width = width;
        this.height = height;
        this.dx = dx;
        this.dy = dy;
    }
    void move(){
        leftX += dx;
        topY += dy;
    }

    boolean isOutOfBounds(int xOffset, int yOffset, int viewWidth, int viewHeight) {
        return leftX + width < -xOffset || topY + height < -yOffset || leftX > viewWidth - xOffset || topY > viewHeight - yOffset;
    }
}
