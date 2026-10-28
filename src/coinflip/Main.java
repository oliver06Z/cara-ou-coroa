package coinflip;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // usa o visual padrão
            }
            JFrame janela = new JFrame("Cara ou Coroa da Sorte");
            janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            janela.setContentPane(new JogoPanel(new Jogo()));
            janela.pack();
            janela.setMinimumSize(janela.getSize());
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }
}
