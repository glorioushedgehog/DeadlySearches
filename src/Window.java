import javax.swing.*;
import java.awt.*;

/**
 * the JFrame class that displays Game
 */
public class Window extends JFrame {
    // determine the dimensions of the view
    // and make the game
    private Window() {
        int viewWidth;
        int viewHeight;
        if(Parameters.fullScreen){
            // make the JFrame take up the whole screen
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            // don't show the top bar of the window
            setUndecorated(true);
            // place the window in the center of the screen
            setLocationRelativeTo(null);
            // figure out the dimensions of the player's screen
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            viewWidth = gd.getDisplayMode().getWidth();
            viewHeight = gd.getDisplayMode().getHeight();
        }else{
            // keep the user from changing the size of the window
            setResizable(false);
            // since they game is not fullscreen, use the dimensions
            // found in Parameters
            setSize(Parameters.viewWidth, Parameters.viewHeight);
            viewWidth = Parameters.viewWidth;
            viewHeight = Parameters.viewHeight;
        }
        // add the Game JPanel
        add(new Game(viewWidth, viewHeight));
        // show the game's title in the top bar of the window
        setTitle(Parameters.title);
        // close the game if the user presses the "x" in the
        // top bar of the window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // show the game
        setVisible(true);
    }

    /**
     * launch the game
     * @param args unused
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(Window::new);
    }
}
