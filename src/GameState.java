/**
 * used by Game to track its own state
 * and by KeyResponder to determine how to
 * react to key presses
 */
public enum GameState {
    MENU,
    PAUSED,
    LEVEL_COMPLETE,
    GAME_OVER,
    PLAYING
}
