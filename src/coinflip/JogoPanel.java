package coinflip;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Locale;

/** Tela principal do jogo. */
public class JogoPanel extends JPanel {

    private static final Color FUNDO = new Color(0xF5F6FA);
    private static final Color PRIMARIA = new Color(0x4C6EF5);
    private static final Color VERDE = new Color(0x2F9E44);
    private static final Color VERMELHO = new Color(0xE03131);
    private static final Color CINZA = new Color(0x868E96);

    private final Jogo jogo;
    private final MoedaPanel moeda = new MoedaPanel();
    private final JLabel mensagem = new JLabel("Escolha cara ou coroa!", SwingConstants.CENTER);
    private final JLabel lblHumor = new JLabel("", SwingConstants.CENTER);
    private final JProgressBar barraNivel = new JProgressBar(0, Jogo.ACERTOS_POR_NIVEL);
    private final JLabel lblNivel = estatistica();
    private final JLabel lblSequencia = estatistica();
    private final JLabel lblRecorde = estatistica();
    private final JLabel lblTaxa = estatistica();
    private final JButton btnCara = botao("  CARA");
    private final JButton btnCoroa = botao("  COROA");
    private final JButton btnReiniciar = new JButton("Reiniciar");

    public JogoPanel(Jogo jogo) {
        this.jogo = jogo;
        setLayout(new BorderLayout(0, 8));
        setBackground(FUNDO);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel titulo = new JLabel(" Cara ou Coroa da Sorte", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setForeground(PRIMARIA);
        add(titulo, BorderLayout.NORTH);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        lblHumor.setFont(new Font("SansSerif", Font.BOLD, 14));
        mensagem.setFont(new Font("SansSerif", Font.BOLD, 16));
        barraNivel.setStringPainted(true);
        barraNivel.setForeground(PRIMARIA);
        barraNivel.setMaximumSize(new Dimension(320, 22));

        for (JLabel l : new JLabel[]{lblHumor, mensagem}) {
            l.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        moeda.setAlignmentX(Component.CENTER_ALIGNMENT);
        barraNivel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centro.add(lblHumor);
        centro.add(moeda);
        centro.add(mensagem);
        centro.add(javax.swing.Box.createVerticalStrut(10));
        centro.add(barraNivel);
        add(centro, BorderLayout.CENTER);

        JPanel sul = new JPanel(new BorderLayout(0, 10));
        sul.setOpaque(false);

        JPanel stats = new JPanel(new GridLayout(2, 2, 8, 8));
        stats.setOpaque(false);
        stats.add(card("Nível", lblNivel));
        stats.add(card("Sequência", lblSequencia));
        stats.add(card("Recorde", lblRecorde));
        stats.add(card("Taxa de acerto", lblTaxa));
        sul.add(stats, BorderLayout.NORTH);

        JPanel botoes = new JPanel(new GridLayout(1, 2, 10, 0));
        botoes.setOpaque(false);
        botoes.add(btnCara);
        botoes.add(btnCoroa);
        sul.add(botoes, BorderLayout.CENTER);

        btnReiniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sul.add(btnReiniciar, BorderLayout.SOUTH);
        add(sul, BorderLayout.SOUTH);

        btnCara.addActionListener(e -> jogar(Lado.CARA));
        btnCoroa.addActionListener(e -> jogar(Lado.COROA));
        btnReiniciar.addActionListener(e -> {
            jogo.reiniciar();
            mensagem.setForeground(Color.DARK_GRAY);
            mensagem.setText("Jogo reiniciado. Escolha cara ou coroa!");
            moeda.setFace(Lado.CARA);
            atualizar();
        });
        atualizar();
    }

    private void jogar(Lado palpite) {
        habilitarBotoes(false);
        Resultado r = jogo.jogar(palpite);
        mensagem.setForeground(Color.DARK_GRAY);
        mensagem.setText("Girando...");
        moeda.girar(r.sorteado(), () -> {
            mostrarResultado(r);
            atualizar();
            habilitarBotoes(true);
        });
    }

    private void mostrarResultado(Resultado r) {
        StringBuilder sb = new StringBuilder();
        if (r.acertou()) {
            mensagem.setForeground(VERDE);
            sb.append("Deu ").append(r.sorteado()).append("! Você acertou ");
            if (r.subiuDeNivel()) {
                sb.append("  ⬆ Nível ").append(jogo.getNivel()).append("! A moeda mudou de humor: ")
                        .append(jogo.getHumor());
            }
        } else {
            mensagem.setForeground(VERMELHO);
            sb.append("Deu ").append(r.sorteado()).append(". Você errou ");
            if (r.perdeuNivel()) {
                sb.append("  Voltou ao nível 1.");
            }
        }
        mensagem.setText("<html><div style='text-align:center;width:340px'>" + sb + "</div></html>");
    }

    private void atualizar() {
        lblNivel.setText(String.valueOf(jogo.getNivel()));
        lblSequencia.setText(String.valueOf(jogo.getSequencia()));
        lblRecorde.setText("seq. " + jogo.getMelhorSequencia() + " • nível " + jogo.getMelhorNivel());
        lblTaxa.setText(String.format(Locale.US, "%.0f%% (%d/%d)",
                jogo.taxaDeAcerto() * 100, jogo.getAcertos(), jogo.getRodadas()));

        int naBarra = jogo.getSequencia() % Jogo.ACERTOS_POR_NIVEL;
        barraNivel.setValue(naBarra);
        barraNivel.setString(naBarra + "/" + Jogo.ACERTOS_POR_NIVEL + " acertos para o próximo nível");

        Humor h = jogo.getHumor();
        String chance = String.format(Locale.US, "%.0f%%", jogo.chanceDeAcerto() * 100);
        switch (h) {
            case SORTE -> {
                lblHumor.setText(" Moeda com SORTE — chance de acerto: " + chance);
                lblHumor.setForeground(VERDE);
                moeda.setAura(new Color(47, 158, 68, 90));
            }
            case AZAR -> {
                lblHumor.setText(" Moeda com AZAR — chance de acerto: " + chance);
                lblHumor.setForeground(VERMELHO);
                moeda.setAura(new Color(224, 49, 49, 90));
            }
            default -> {
                lblHumor.setText(" Moeda neutra — chance de acerto: " + chance);
                lblHumor.setForeground(CINZA);
                moeda.setAura(new Color(0, 0, 0, 0));
            }
        }
    }

    private void habilitarBotoes(boolean on) {
        btnCara.setEnabled(on);
        btnCoroa.setEnabled(on);
        btnReiniciar.setEnabled(on);
    }

    private static JLabel estatistica() {
        JLabel l = new JLabel("-", SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 16));
        return l;
    }

    private static JPanel card(String titulo, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDEE2E6)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        JLabel t = new JLabel(titulo, SwingConstants.CENTER);
        t.setForeground(CINZA);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(t, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        return p;
    }

    private static JButton botao(String texto) {
        JButton b = new JButton(texto);
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(140, 46));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
