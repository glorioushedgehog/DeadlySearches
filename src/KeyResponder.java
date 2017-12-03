import sun.plugin2.message.GetAppletMessage;

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
        if (heldDownKeys.contains(key)) {
            return;
        }
        heldDownKeys.add(key);
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
        } else if (game.gameState == GameState.MENU ||
                game.gameState == GameState.PAUSED ||
                game.gameState == GameState.LEVEL_COMPLETE ||
                game.gameState == GameState.GAME_OVER) {
            if (key == KeyEvent.VK_ENTER) {
                game.unPause();
            } else if (key == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (heldDownKeys.contains(key)) {
            heldDownKeys.remove(key);
        }
    }
}