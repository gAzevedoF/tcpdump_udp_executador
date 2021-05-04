package ConexaoCliente;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ConexaoMain {
    public static void main(String[] args) throws UnknownHostException {
        Scanner resposta = new Scanner(System.in);

        System.out.println("===INICIANDO SERVIDOR===");
        System.out.println("DIGITE A PORTA DESEJADA: ");
//        int porta = resposta.nextInt();
        int porta = 3300;

        System.out.println("DIGITE O IP DO SERVIDOR DESEJADA: ");
//        InetAddress ipServidor = InetAddress.getByName(resposta.next());
        InetAddress ipServidor = InetAddress.getByName("192.168.25.4");

        System.out.println("DIGITE O TAMANHO ESTIMADO DO BUFFER QUE SERÁ RECEBIDO: ");
//        int tamanho = resposta.nextInt();
        int tamanho = 1024;

        System.out.println("DIGITE O NOME DESEJADO PARA O ARQUIVO DE RESPOSTA: ");
//        String nomeArquivo = resposta.next();
        String nomeArquivo = "resposta.txt";

        System.out.println("DIGITE O COMANDO TCPDUMP QUE SERÁ EXECUTADO: ");
//        String comando = resposta.next();
        String comando = "ping 8.8.8.8";

        Conexao c = new Conexao(porta, ipServidor, tamanho, nomeArquivo, comando);
    }

}
