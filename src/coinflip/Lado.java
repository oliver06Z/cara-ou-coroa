package coinflip;

/** Os dois lados da moeda. */
public enum Lado {
    CARA("CARA"),
    COROA("COROA");

    private final String rotulo;

    Lado(String rotulo) {
        this.rotulo = rotulo;
    }

    public Lado oposto() {
        return this == CARA ? COROA : CARA;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
