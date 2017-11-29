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

    boolean isOutOfBounds(int viewWidth, int viewHeight) {
        return leftX + width < 0 || topY + height < 0 || leftX > viewWidth || topY > viewHeight;
    }
}
