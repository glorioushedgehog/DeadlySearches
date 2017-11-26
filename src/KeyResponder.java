import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

class KeyResponder extends KeyAdapter {
    private Game game;
    private HashSet<Integer> heldDownKeys;
    KeyResponder(Game game) {
        this.game = game;
        this.heldDownKeys = new HashSet<>();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(heldDownKeys.contains(key)){
            return;
        }
        heldDownKeys.add(key);
        if (key == KeyEvent.VK_LEFT && game.timer.isRunning()) {
            this.game.player.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT && game.timer.isRunning()) {
            this.game.player.moveRight();
        } else if (key == KeyEvent.VK_UP && game.timer.isRunning()) {
            this.game.player.moveUp();
        } else if (key == KeyEvent.VK_DOWN && game.timer.isRunning()) {
            this.game.player.moveDown();
        } else if (key == KeyEvent.VK_ESCAPE) {
            if (this.game.timer.isRunning()) {
                this.game.timer.stop();
            } else {
                // make sure this can't happen if player
                // has lost the game
                this.game.timer.start();
            }
        } else if (key == KeyEvent.VK_PAUSE) {
            if (this.game.timer.isRunning()) {
                this.game.timer.stop();
            } else {
                this.game.timer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(heldDownKeys.contains(key)){
            heldDownKeys.remove(key);
        }
    }
}