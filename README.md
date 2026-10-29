# 🪙 Cara ou Coroa da Sorte

Jogo de cara ou coroa em **Java (Swing)** onde a sua sorte muda conforme você acerta. Quanto mais você acerta, mais a moeda "se estressa": ela pode passar a **ajudar** ou a **atrapalhar** você.

## Como funciona

- Você começa no **nível 1** com a moeda **neutra** (50% de chance de acerto).
- A cada **3 acertos seguidos** você sobe de nível.
- Ao subir de nível, a moeda muda de humor **aleatoriamente**:
  -  **Sorte**: aumenta a sua chance de acerto
  -  **Azar**: diminui a sua chance de acerto
- O efeito cresce com o nível: **+/- 5% por nível**, até no máximo **30%** (chance entre 20% e 80%).
- Se você **errar**, volta ao nível 1 e a moeda fica neutra.

| Nível | Intensidade | Chance com Sorte | Chance com Azar |
|------:|------------:|-----------------:|----------------:|
| 1     | 0%          | 50%              | 50%             |
| 2     | 5%          | 55%              | 45%             |
| 3     | 10%         | 60%              | 40%             |
| 4     | 15%         | 65%              | 35%             |
| 7+    | 30%         | 80%              | 20%             |

A interface mostra o humor da moeda, a chance atual, nível, sequência, recorde e taxa de acerto.

## Como executar

Requer **JDK 17 ou superior** (`java -version` e `javac -version` para conferir).

```bash
git clone https://github.com/SEU-USUARIO/cara-ou-coroa.git
cd cara-ou-coroa

# Linux / Mac
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")
java -cp out coinflip.Main
```

```bat
:: Windows (cmd)
mkdir out
dir /s /b src\*.java > fontes.txt
javac -encoding UTF-8 -d out @fontes.txt
java -cp out coinflip.Main
```

### No VS Code
1. Instale a extensão **Extension Pack for Java**.
2. Abra a pasta do projeto, abra `src/coinflip/Main.java` e clique em **Run** (acima do `main`).

## Testes

```bash
# Linux / Mac
javac -encoding UTF-8 -d out $(find src test -name "*.java")
java -cp out coinflip.JogoTeste
```

Os testes usam um `Random` controlado para verificar as regras de forma determinística, sem bibliotecas externas.

## Estrutura

```
├── src/coinflip/
│   ├── Main.java         # abre a janela
│   ├── Jogo.java         # regras (sem dependência de interface)
│   ├── Lado.java         # CARA / COROA
│   ├── Humor.java        # NEUTRO / SORTE / AZAR
│   ├── Resultado.java    # resultado de uma rodada
│   ├── JogoPanel.java    # tela do jogo
│   └── MoedaPanel.java   # desenho e animação da moeda
├── test/coinflip/JogoTeste.java
└── LICENSE
```

## Ideias para evoluir

- Salvar o recorde em arquivo
- Modo de apostas com pontos
- Sons e mais animações

## Licença

MIT
