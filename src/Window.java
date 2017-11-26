import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private Window() {
        add(new Game(Parameters.mapWidth, Parameters.mapHeight));
        setTitle("Video Game 2");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Parameters.mapWidth, Parameters.mapHeight);

        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setUndecorated(true);
        //setLocationRelativeTo(null);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //double width = screenSize.getWidth();
        //double height = screenSize.getHeight();

        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(Window::new);
    }
}
