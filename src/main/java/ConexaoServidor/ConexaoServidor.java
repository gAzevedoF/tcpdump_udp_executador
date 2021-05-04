package ConexaoServidor;

import java.util.Observable;
import java.util.Scanner;

public class ConexaoServidor extends Observable {

    private int porta;
    private int tamanho;

    public ConexaoServidor(int porta, int tamanho) {
        this.porta = porta;
        this.tamanho = tamanho;
        new Thread(new Servidor(porta, tamanho)).start();
    }

    public static void main(String[] args) {
        Scanner resposta = new Scanner(System.in);

        System.out.println("===INICIANDO SERVIDOR===");
        System.out.println("DIGITE A PORTA DESEJADA: ");
//        int porta = resposta.nextInt();
        int porta = 3300;

        System.out.println("DIGITE O TAMANHO ESTIMADO DO BUFFER QUE SERÁ RECEBIDO: ");
//        int tamanho = resposta.nextInt();
        int tamanho = 1024;

        ConexaoServidor c = new ConexaoServidor(porta, tamanho);
    }
}