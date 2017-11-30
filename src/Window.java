import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private Window() {
        int viewWidth;
        int viewHeight;
        if(Parameters.fullScreen){
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
            setLocationRelativeTo(null);
            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            viewWidth = gd.getDisplayMode().getWidth();
            viewHeight = gd.getDisplayMode().getHeight();
        }else{
            setResizable(false);
            setSize(Parameters.viewWidth, Parameters.viewHeight);
            viewWidth = Parameters.viewWidth;
            viewHeight = Parameters.viewHeight;
        }
        add(new Game(viewWidth, viewHeight));
        setTitle(Parameters.title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Window::new);
    }
}
