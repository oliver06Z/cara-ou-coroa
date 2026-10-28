package coinflip;

/** Resultado de uma rodada. */
public record Resultado(
        Lado palpite,
        Lado sorteado,
        boolean acertou,
        boolean subiuDeNivel,
        boolean perdeuNivel,
        double chanceUsada,
        Humor humorUsado) {
}
