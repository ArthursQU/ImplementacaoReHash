import java.util.Random;

public class Main {
    static final int TAMANHO = 100;
    static final int VAZIO = -1;
    static final int LIMITE_OCUPACAO = 90;
    static final int MAX_CHAVE = 100000;

    static int colisoesDaUltimaInsercao = 0;
    static int passosDaUltimaBusca = 0;

    public static int funcaoHash(int chave) {
        return chave % TAMANHO;
    }

    public static int rehash(int i) {
        return (i + 1) % TAMANHO;
    }

    public static int insereHashing(int[] tabela, int chave) {
        int i = funcaoHash(chave);
        colisoesDaUltimaInsercao = 0;

        while (tabela[i] != VAZIO) {
            colisoesDaUltimaInsercao = colisoesDaUltimaInsercao + 1;
            i = rehash(i);
        }

        tabela[i] = chave;
        return i;
    }

    public static int buscaHashing(int[] tabela, int chave) {
        int i = funcaoHash(chave);
        passosDaUltimaBusca = 1;

        while (tabela[i] != chave && tabela[i] != VAZIO) {
            i = rehash(i);
            passosDaUltimaBusca = passosDaUltimaBusca + 1;
        }

        if (tabela[i] == VAZIO) {
            return -1;
        } else {
            return i;
        }
    }

    public static void main(String[] args) {
        Random random = new Random(1090);

        int[] tabela = new int[TAMANHO];
        for (int i = 0; i < TAMANHO; i++) {
            tabela[i] = VAZIO;
        }

        int ocupados = 0;
        int rejeitados = 0;
        int totalColisoes = 0;

        int[] topChave = new int[3];
        int[] topColisoes = new int[3];
        for (int i = 0; i < 3; i++) {
            topChave[i] = VAZIO;
            topColisoes[i] = -1;
        }

        System.out.println("---- INSERÇÕES (mostrando só colisões e bloqueios) ----");
        for (int n = 1; n <= 100; n++) {
            int chave = random.nextInt(MAX_CHAVE + 1);

            if (ocupados * 100 >= LIMITE_OCUPACAO * TAMANHO) {
                rejeitados = rejeitados + 1;
                System.out.println("Tentativa " + n + ": chave " + chave
                        + " BLOQUEADA (tabela com " + ocupados + "% de ocupação)");
            } else {
                int posicao = insereHashing(tabela, chave);
                int colisoes = colisoesDaUltimaInsercao;
                ocupados = ocupados + 1;
                totalColisoes = totalColisoes + colisoes;

                if (colisoes > 0) {
                    System.out.println("Tentativa " + n + ": chave " + chave
                            + " h=" + funcaoHash(chave) + " colidiu " + colisoes
                            + "x -> posição " + posicao);
                }

                if (colisoes > topColisoes[0]) {
                    topColisoes[2] = topColisoes[1];
                    topChave[2] = topChave[1];
                    topColisoes[1] = topColisoes[0];
                    topChave[1] = topChave[0];
                    topColisoes[0] = colisoes;
                    topChave[0] = chave;
                } else if (colisoes > topColisoes[1]) {
                    topColisoes[2] = topColisoes[1];
                    topChave[2] = topChave[1];
                    topColisoes[1] = colisoes;
                    topChave[1] = chave;
                } else if (colisoes > topColisoes[2]) {
                    topColisoes[2] = colisoes;
                    topChave[2] = chave;
                }
            }
        }

        System.out.println();
        System.out.println("---- RESULTADOS ----");
        System.out.println("Elementos inseridos: " + ocupados);
        System.out.println("Inserções bloqueadas (ocupação de 90%): " + rejeitados);
        System.out.println("Total de colisões na inserção: " + totalColisoes);

        System.out.println();
        System.out.println("Os 3 elementos que mais colidiram:");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ") chave " + topChave[i]
                    + " -> " + topColisoes[i] + " colisões");
        }

        System.out.println();
        System.out.print("Posições livres na matriz: ");
        int livres = 0;
        for (int i = 0; i < TAMANHO; i++) {
            if (tabela[i] == VAZIO) {
                if (livres > 0) {
                    System.out.print(", ");
                }
                System.out.print(i);
                livres = livres + 1;
            }
        }
        System.out.println();
        System.out.println("Quantidade de posições livres: " + livres);

        System.out.println();
        System.out.println("---- TESTE DE BUSCA ----");
        for (int i = 0; i < 3; i++) {
            int chave = topChave[i];
            int posicao = buscaHashing(tabela, chave);

            if (posicao == -1) {
                System.out.println("Chave " + chave + ": NÃO encontrada");
            } else {
                System.out.println("Chave " + chave + ": encontrada na posição " + posicao
                        + " (h=" + funcaoHash(chave) + ", " + passosDaUltimaBusca
                        + " posições consultadas)");
            }
        }
    }
}