import view.GameFrame;

public class Main {
    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(() -> {

            new GameFrame();
        });
    }
}