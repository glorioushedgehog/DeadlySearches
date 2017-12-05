
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

/**
 * handles key presses
 */
class KeyResponder extends KeyAdapter {
    // reference to the game so that KeyResponder
    // can make changes to the game and know the gameState
    // to determine how key presses should be handled
    private Game game;
    // used to prevent interpreting the player holding
    // down a key as multiple key presses
    private HashSet<Integer> heldDownKeys;

    KeyResponder(Game game) {
        this.game = game;
        this.heldDownKeys = new HashSet<>();
    }

    /**
     * handle key presses
     * @param e the key that was pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // only handle keys that are pressed,
        // not held down
        if (heldDownKeys.contains(key)) {
            return;
        }
        // mark this key as being held down
        heldDownKeys.add(key);
        // if the game is running and an arrow key
        // is pressed, move the player.
        // if enter is pressed, pause the game
        if (game.gameState == GameState.PLAYING) {
            if (key == KeyEvent.VK_LEFT) {
                game.player.moveLeft();
            } else if (key == KeyEvent.VK_RIGHT) {
                game.player.moveRight();
            } else if (key == KeyEvent.VK_UP) {
                game.player.moveUp();
            } else if (key == KeyEvent.VK_DOWN) {
                game.player.moveDown();
            } else if (key == KeyEvent.VK_ENTER) {
                game.pause();
            }
        } else {
            // if the game is not running, check if
            // the user has un-paused the game or
            // exited the game
            if (key == KeyEvent.VK_ENTER) {
                game.unPause();
            } else if (key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }

    }

    /**
     * mark any released key as no longer being
     * held down
     * @param e the key that was released
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (heldDownKeys.contains(key)) {
            heldDownKeys.remove(key);
        }
    }
}