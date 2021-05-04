package ConexaoServidor;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

public class Servidor implements Runnable {

    private final int porta;
    private final int tamanhoBufferEstimado;
    private DatagramSocket socketUDP;

    private static final String REGRA_DE_SEPARACAO_PARA_COMANDO = " ";

    public Servidor(int porta, int tamanhoBufferEstimado) {
        this.porta = porta;
        this.tamanhoBufferEstimado = tamanhoBufferEstimado;
        this.socketUDP = null;
    }

    @Override
    public void run() {
        while (true) {
            byte[] bufferParaReceber = new byte[this.tamanhoBufferEstimado];
            try {
                socketUDP = new DatagramSocket(this.porta);

                while (true) {
                    DatagramPacket pacoteRecebidoDoCliente = new DatagramPacket(bufferParaReceber, bufferParaReceber.length);

                    System.out.println("SERVIDOR COM IP " + InetAddress.getLocalHost().getHostAddress() + " AGUARDANDO CLIENTE NA PORTA " + this.porta);

                    socketUDP.receive(pacoteRecebidoDoCliente);
                    System.out.println("COMANDO RECEBIDO DO CLIENTE " + pacoteRecebidoDoCliente.getAddress().getHostAddress());

                    this.executarComando(pacoteRecebidoDoCliente);

                    int porta = pacoteRecebidoDoCliente.getPort();

                    byte[] arquivoGerado = this.converterArquivoEmBytes();

                    System.out.println("ENVIANDO ARQUIVO PARA O CLIENTE " + pacoteRecebidoDoCliente.getAddress().getHostAddress());
                    DatagramPacket pacoteEnviado = new DatagramPacket(arquivoGerado, arquivoGerado.length, pacoteRecebidoDoCliente.getAddress(), porta);
                    socketUDP.send(pacoteEnviado);
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public byte[] converterArquivoEmBytes() throws IOException {
        File file = new File("log.txt");
        byte[] arquivo = Files.readAllBytes(file.toPath());

        PrintWriter writer = new PrintWriter("log.txt");
        writer.write("");
        writer.close();

        return arquivo;
    }

    private String[] gerarComando(DatagramPacket pacoteParaReceber) {
        int cont = 0;
        while (pacoteParaReceber.getData()[cont] != 0) {
            cont++;
        }

        byte[] listaAjustada = new byte[cont];
        int i = 0;
        for (Byte dado : pacoteParaReceber.getData()) {
            if (dado != 0) {
                listaAjustada[i] = dado;
                i++;
            }
        }

        String comando = new String(listaAjustada);
        System.out.println("INICIANDO EXECUÇÃO DO COMANDO: " + comando);
        return comando.split(REGRA_DE_SEPARACAO_PARA_COMANDO);
    }

    public void executarComando(DatagramPacket pacote) throws InterruptedException, IOException {
        String[] comando = this.gerarComando(pacote);
        ProcessBuilder pb = new ProcessBuilder(comando);
        pb.redirectErrorStream(true);
        File log = new File("log.txt");
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        Process p = pb.start();
        TimeUnit.SECONDS.sleep(10);
        p.waitFor();
        System.out.println("TERMINOU EXECUÇÃO DO COMANDO");
    }
}