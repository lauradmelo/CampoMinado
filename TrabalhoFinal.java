// Trabalho Final - Fazer um jogo utilizando tudo que foi ensinado até agora
// feito por @Melissa e @Laura
// Jogo escolhido: Campo Minado 

package trabalhofinal;
import java.util.Random;
import java.util.Scanner;

public class TrabalhoFinal {

    //Base do jogo que só funcionou a partir do class
    static class Jogo {
        int TAM = 10;
        int BOMBAS = 10;
        char[][] visivel = new char[TAM][TAM];
        int[][] tab = new int[TAM][TAM];
        boolean[][] revelado = new boolean[TAM][TAM];
    }

    static Jogo jogo;

    //Main principal
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int opcao;

        // Menu de apresentação
        do {
            System.out.println("------------------");
            System.out.println("<<<CAMPO MINADO>>>");
            System.out.println("------------------");
            System.out.println("Aperte 1 para jogar ou 2 para ver as regras do jogo");

            opcao = input.nextInt();

            if (opcao != 1 && opcao != 2) {
                System.err.println("Opção inválida! Tente novamente.\n");
            }

        } while (opcao != 1 && opcao != 2);

        if (opcao == 2) {
            System.out.println("<<Modo de jogar:>>\n"
                    + "* O tabuleiro tem casas escondidas.\n"
                    + "* Algumas casas têm minas, outras são seguras.\n"
                    + "* Escolha uma casa (-).\n"
                    + "* Se for mina, você perde.\n"
                    + "* Se for segura, aparece um número.\n"
                    + "* O número indica quantas minas existem nas casas vizinhas.\n"
                    + "* Use lógica para descobrir onde estão as minas.\n"
                    + "* Você vence quando revelar todas as casas seguras.\n");
        }

        System.out.println("Vamos começar!\n");

        jogo = new Jogo();
        iniciarJogo();
        jogar();
    }

    //Visivel e nao visivel
    static void iniciarJogo() {

        // Iniciar visivel e revelado
        for (int i = 0; i < jogo.TAM; i++) {
            for (int j = 0; j < jogo.TAM; j++) {
                jogo.visivel[i][j] = '#';
                jogo.revelado[i][j] = false;
            }
        }

        plantarBombas();
        calcularNumeros();
    }

    //Adicionar Bombas
    static void plantarBombas() {
        Random r = new Random();
        int colocadas = 0;

        while (colocadas < jogo.BOMBAS) {
            int x = r.nextInt(jogo.TAM);
            int y = r.nextInt(jogo.TAM);

            if (jogo.tab[x][y] != -1) {
                jogo.tab[x][y] = -1;
                colocadas++;
            }
        }
    }

    //Coordenadas
    static void calcularNumeros() {
        for (int x = 0; x < jogo.TAM; x++) {
            for (int y = 0; y < jogo.TAM; y++) {
                if (jogo.tab[x][y] == -1) continue;

                int cont = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int xx = x + i, yy = y + j;
                        if (xx >= 0 && xx < jogo.TAM && yy >= 0 && yy < jogo.TAM &&
                                jogo.tab[xx][yy] == -1)
                            cont++;
                    }
                }
                jogo.tab[x][y] = cont;
            }
        }
    }

    //Iniciar jogo
    static void jogar() {
        Scanner input = new Scanner(System.in);

        while (true) {
            imprimir();

            System.out.print("Linha: ");
            int x = input.nextInt();
            System.out.print("Coluna: ");
            int y = input.nextInt();

            if (x < 0 || x >= jogo.TAM || y < 0 || y >= jogo.TAM) {
                System.err.println("Coordenada inválida!");
                continue;
            }

            if (jogo.tab[x][y] == -1) {
                revelarTudo();
                imprimir();
                System.err.println("BOOM! Você perdeu!");
                break;
            }

            revelarCelula(x, y);

            if (ganhou()) {
                revelarTudo();
                imprimir();
                System.out.println("Você venceu!");
                break;
            }
        }
    }

    // Revelar casa no tabuleiro
    static void revelarCelula(int x, int y) {
        if (x < 0 || x >= jogo.TAM || y < 0 || y >= jogo.TAM) return;
        if (jogo.revelado[x][y]) return;

        jogo.revelado[x][y] = true;

        if (jogo.tab[x][y] > 0) {
            jogo.visivel[x][y] = (char) ('0' + jogo.tab[x][y]);
            return;
        }

        jogo.visivel[x][y] = ' ';

        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                revelarCelula(x + i, y + j);
    }

    //Revelar se...
    static void revelarTudo() {
        for (int x = 0; x < jogo.TAM; x++) {
            for (int y = 0; y < jogo.TAM; y++) {
                if (jogo.tab[x][y] == -1) jogo.visivel[x][y] = '*';
                else if (jogo.tab[x][y] == 0) jogo.visivel[x][y] = ' ';
                else jogo.visivel[x][y] = (char) ('0' + jogo.tab[x][y]);
            }
        }
    }

    //Como saber se ganhou
    static boolean ganhou() {
        for (int x = 0; x < jogo.TAM; x++)
            for (int y = 0; y < jogo.TAM; y++)
                if (!jogo.revelado[x][y] && jogo.tab[x][y] != -1)
                    return false;

        return true;
    }

    //Imprimir o jogo
    static void imprimir() {
        System.out.print("\n   ");
        for (int i = 0; i < jogo.TAM; i++)
            System.out.print(i + " ");
        System.out.println();

        System.out.print("   ");
        for (int i = 0; i < jogo.TAM; i++)
            System.out.print("--");
        System.out.println();

        for (int i = 0; i < jogo.TAM; i++) {
            System.out.print(i + "| ");
            for (int j = 0; j < jogo.TAM; j++) {
                System.out.print(jogo.visivel[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

