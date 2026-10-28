package coinflip;

/** O "humor" da moeda: pode ajudar, atrapalhar ou ser neutro. */
public enum Humor {
    NEUTRO("Neutra", 0),
    SORTE("Sorte", +1),
    AZAR("Azar", -1);

    private final String rotulo;
    private final int sinal;

    Humor(String rotulo, int sinal) {
        this.rotulo = rotulo;
        this.sinal = sinal;
    }

    /** +1 aumenta a chance de acerto, -1 diminui, 0 não altera. */
    public int sinal() {
        return sinal;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
