package coinflip;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Desenha a moeda e anima o giro. */
public class MoedaPanel extends JPanel {

    private static final int QUADROS = 36;
    private static final int TAMANHO = 180;

    private Lado face = Lado.CARA;
    private Color aura = new Color(0, 0, 0, 0);
    private double escalaX = 1.0;
    private Timer timer;

    public MoedaPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(260, 240));
    }

    public void setAura(Color cor) {
        this.aura = cor;
        repaint();
    }

    public void setFace(Lado lado) {
        this.face = lado;
        this.escalaX = 1.0;
        repaint();
    }

    /** Anima um giro e termina mostrando o lado de destino. */
    public void girar(Lado destino, Runnable aoTerminar) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        final Lado origem = face;
        final int[] quadro = {0};
        // número de meias-voltas: par mantém a origem, ímpar inverte; ajustamos para cair no destino
        final int meiasVoltas = 6 + (origem == destino ? 0 : 1);
        timer = new Timer(16, null);
        timer.addActionListener(e -> {
            quadro[0]++;
            double progresso = (double) quadro[0] / QUADROS;
            double angulo = progresso * meiasVoltas * Math.PI;
            escalaX = Math.abs(Math.cos(angulo));
            int giros = (int) (angulo / Math.PI);
            face = (giros % 2 == 0) ? origem : origem.oposto();
            if (quadro[0] >= QUADROS) {
                timer.stop();
                face = destino;
                escalaX = 1.0;
                repaint();
                if (aoTerminar != null) {
                    aoTerminar.run();
                }
                return;
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int larguraMoeda = Math.max(4, (int) (TAMANHO * escalaX));

        // aura de sorte/azar
        if (aura.getAlpha() > 0) {
            g2.setColor(aura);
            g2.fillOval(cx - larguraMoeda / 2 - 14, cy - TAMANHO / 2 - 14, larguraMoeda + 28, TAMANHO + 28);
        }

        // sombra
        g2.setColor(new Color(0, 0, 0, 40));
        g2.fillOval(cx - larguraMoeda / 2 + 4, cy - TAMANHO / 2 + 8, larguraMoeda, TAMANHO);

        // corpo
        g2.setPaint(new GradientPaint(cx, cy - TAMANHO / 2, new Color(255, 223, 100),
                cx, cy + TAMANHO / 2, new Color(214, 160, 30)));
        g2.fillOval(cx - larguraMoeda / 2, cy - TAMANHO / 2, larguraMoeda, TAMANHO);
        g2.setColor(new Color(166, 116, 10));
        g2.setStroke(new BasicStroke(5f));
        g2.drawOval(cx - larguraMoeda / 2, cy - TAMANHO / 2, larguraMoeda, TAMANHO);

        // texto (só quando a moeda está larga o bastante)
        if (escalaX > 0.25) {
            g2.setColor(new Color(120, 80, 0));
            g2.setFont(new Font("SansSerif", Font.BOLD, (int) (26 * Math.max(escalaX, 0.6))));
            String texto = face == Lado.CARA ? "😀" : "👑";
            Font fonteEmoji = new Font("SansSerif", Font.PLAIN, 56);
            g2.setFont(fonteEmoji);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(texto, cx - fm.stringWidth(texto) / 2, cy + 6);
            g2.setFont(new Font("SansSerif", Font.BOLD, 20));
            fm = g2.getFontMetrics();
            String nome = face.toString();
            g2.drawString(nome, cx - fm.stringWidth(nome) / 2, cy + 44);
        }
        g2.dispose();
    }
}
