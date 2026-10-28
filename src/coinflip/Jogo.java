package coinflip;

import java.util.Random;

/**
 * Regras do jogo (sem nenhuma dependência de interface).
 *
 * <p>Mecânica: a cada {@value #ACERTOS_POR_NIVEL} acertos seguidos você sobe de nível.
 * Ao subir, a moeda muda de humor de forma aleatória: pode virar SORTE (ajuda você) ou
 * AZAR (atrapalha). Quanto maior o nível, mais forte é o efeito (de 5% por nível, até 30%).
 * Se você errar, volta ao nível 1 e a moeda fica neutra (50%).
 */
public class Jogo {

    public static final int ACERTOS_POR_NIVEL = 3;
    public static final double BONUS_POR_NIVEL = 0.05;
    public static final double BONUS_MAXIMO = 0.30;

    private final Random random;

    private int nivel = 1;
    private int sequencia;
    private int melhorSequencia;
    private int melhorNivel = 1;
    private int rodadas;
    private int acertos;
    private Humor humor = Humor.NEUTRO;

    public Jogo() {
        this(new Random());
    }

    /** Permite injetar um Random (útil para testes determinísticos). */
    public Jogo(Random random) {
        this.random = random;
    }

    /** Intensidade do efeito do humor no nível atual (0.0 no nível 1, até 0.30). */
    public double intensidade() {
        return Math.min(BONUS_POR_NIVEL * (nivel - 1), BONUS_MAXIMO);
    }

    /** Probabilidade de o palpite atual estar certo, considerando o humor da moeda. */
    public double chanceDeAcerto() {
        return 0.5 + humor.sinal() * intensidade();
    }

    public Resultado jogar(Lado palpite) {
        double chance = chanceDeAcerto();
        Humor humorUsado = humor;
        boolean acertou = random.nextDouble() < chance;
        Lado sorteado = acertou ? palpite : palpite.oposto();

        rodadas++;
        boolean subiu = false;
        boolean perdeu = false;

        if (acertou) {
            acertos++;
            sequencia++;
            melhorSequencia = Math.max(melhorSequencia, sequencia);
            int novoNivel = 1 + sequencia / ACERTOS_POR_NIVEL;
            if (novoNivel > nivel) {
                nivel = novoNivel;
                melhorNivel = Math.max(melhorNivel, nivel);
                humor = random.nextBoolean() ? Humor.SORTE : Humor.AZAR;
                subiu = true;
            }
        } else {
            perdeu = nivel > 1;
            sequencia = 0;
            nivel = 1;
            humor = Humor.NEUTRO;
        }
        return new Resultado(palpite, sorteado, acertou, subiu, perdeu, chance, humorUsado);
    }

    public void reiniciar() {
        nivel = 1;
        sequencia = 0;
        melhorSequencia = 0;
        melhorNivel = 1;
        rodadas = 0;
        acertos = 0;
        humor = Humor.NEUTRO;
    }

    public int getNivel() { return nivel; }
    public int getSequencia() { return sequencia; }
    public int getMelhorSequencia() { return melhorSequencia; }
    public int getMelhorNivel() { return melhorNivel; }
    public int getRodadas() { return rodadas; }
    public int getAcertos() { return acertos; }
    public Humor getHumor() { return humor; }

    public double taxaDeAcerto() {
        return rodadas == 0 ? 0 : (double) acertos / rodadas;
    }
}
