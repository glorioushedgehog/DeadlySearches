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
            game.player.moveLeft();
        } else if (key == KeyEvent.VK_RIGHT && game.timer.isRunning()) {
            game.player.moveRight();
        } else if (key == KeyEvent.VK_UP && game.timer.isRunning()) {
            game.player.moveUp();
        } else if (key == KeyEvent.VK_DOWN && game.timer.isRunning()) {
            game.player.moveDown();
        } else if (key == KeyEvent.VK_ESCAPE) {
            if (game.timer.isRunning()) {
                game.timer.stop();
            } else {
                // make sure this can't happen if player
                // has lost the game
                game.timer.start();
            }
        } else if (key == KeyEvent.VK_PAUSE) {
            if (game.timer.isRunning()) {
                game.timer.stop();
            } else {
                game.timer.start();
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