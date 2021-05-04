package ConexaoServidor;

import java.util.Scanner;

public class ConexaoMain {
    public static void main(String[] args) {
        Scanner resposta = new Scanner(System.in);

        System.out.println("===INICIANDO SERVIDOR===");
        System.out.println("DIGITE A PORTA DESEJADA: ");
//        int porta = resposta.nextInt();
        int porta = 3300;

        System.out.println("DIGITE O TAMANHO ESTIMADO DO BUFFER QUE SERÁ RECEBIDO: ");
//        int tamanho = resposta.nextInt();
        int tamanho = 1024;

        Conexao c = new Conexao(porta, tamanho);
    }
}
