package coinflip;

import java.util.Random;

/** Testes simples, sem dependências externas. Execute: java -cp out coinflip.JogoTeste */
public class JogoTeste {

    /** Random controlável: devolve valores pré-definidos. */
    static class RandomFixo extends Random {
        private final double[] doubles;
        private final boolean[] booleans;
        private int d, b;

        RandomFixo(double[] doubles, boolean[] booleans) {
            this.doubles = doubles;
            this.booleans = booleans;
        }

        @Override public double nextDouble() { return doubles[d++ % doubles.length]; }
        @Override public boolean nextBoolean() { return booleans[b++ % booleans.length]; }
    }

    static int falhas = 0;

    static void verifica(boolean condicao, String nome) {
        System.out.println((condicao ? "OK   " : "FALHA") + " - " + nome);
        if (!condicao) falhas++;
    }

    public static void main(String[] args) {
        // sempre acerta (0.0 < chance), humor sorteado = SORTE (nextBoolean true)
        Jogo j = new Jogo(new RandomFixo(new double[]{0.0}, new boolean[]{true}));
        verifica(j.chanceDeAcerto() == 0.5, "começa com 50% de chance");
        j.jogar(Lado.CARA); j.jogar(Lado.CARA);
        verifica(j.getNivel() == 1, "2 acertos ainda é nível 1");
        Resultado r = j.jogar(Lado.COROA);
        verifica(r.acertou() && r.subiuDeNivel(), "3º acerto sobe de nível");
        verifica(j.getNivel() == 2 && j.getHumor() == Humor.SORTE, "nível 2 com humor SORTE");
        verifica(Math.abs(j.chanceDeAcerto() - 0.55) < 1e-9, "SORTE no nível 2 = 55%");

        // humor AZAR reduz a chance
        Jogo a = new Jogo(new RandomFixo(new double[]{0.0}, new boolean[]{false}));
        for (int i = 0; i < 3; i++) a.jogar(Lado.CARA);
        verifica(a.getHumor() == Humor.AZAR, "humor AZAR sorteado");
        verifica(Math.abs(a.chanceDeAcerto() - 0.45) < 1e-9, "AZAR no nível 2 = 45%");

        // limite máximo do bônus
        Jogo m = new Jogo(new RandomFixo(new double[]{0.0}, new boolean[]{true}));
        for (int i = 0; i < 40; i++) m.jogar(Lado.CARA);
        verifica(Math.abs(m.chanceDeAcerto() - 0.80) < 1e-9, "chance limitada a 80%");

        // errar zera tudo
        Jogo e = new Jogo(new RandomFixo(new double[]{0.0, 0.0, 0.0, 0.99}, new boolean[]{true}));
        for (int i = 0; i < 3; i++) e.jogar(Lado.CARA);
        Resultado erro = e.jogar(Lado.CARA);
        verifica(!erro.acertou() && erro.sorteado() == Lado.COROA, "erro devolve o lado oposto");
        verifica(erro.perdeuNivel() && e.getNivel() == 1 && e.getSequencia() == 0, "erro volta ao nível 1");
        verifica(e.getHumor() == Humor.NEUTRO, "erro volta ao humor neutro");
        verifica(e.getMelhorSequencia() == 3 && e.getMelhorNivel() == 2, "recorde é preservado");

        // estatísticas
        verifica(e.getRodadas() == 4 && e.getAcertos() == 3, "contagem de rodadas/acertos");
        e.reiniciar();
        verifica(e.getRodadas() == 0 && e.getMelhorNivel() == 1, "reiniciar zera o jogo");

        System.out.println(falhas == 0 ? "\nTodos os testes passaram." : "\n" + falhas + " teste(s) falharam.");
        if (falhas > 0) System.exit(1);
    }
}
