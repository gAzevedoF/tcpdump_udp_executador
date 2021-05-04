package ConexaoCliente;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

class Cliente implements Runnable {
    private int porta;
    private  InetAddress ipServidor;
    private  int tamanho;
    private String nomeArquivo;
    private String comando;
    private DatagramSocket socket;

    public Cliente(int porta, InetAddress ipServidor, int tamanho, String nomeArquivo, String comando) {
        this.porta = porta;
        this.ipServidor = ipServidor;
        this.tamanho = tamanho;
        this.nomeArquivo = nomeArquivo;
        this.comando = comando;
        this.socket = null;
    }

    @Override
    public void run() {
        byte[] bufferReceber = new byte[tamanho];

        try {
            this.socket = new DatagramSocket();
            byte[] enviar = comando.getBytes();
            DatagramPacket enviando = new DatagramPacket(enviar, enviar.length, ipServidor , porta);
            socket.send(enviando);
            DatagramPacket recebido = new DatagramPacket(bufferReceber, bufferReceber.length);
            socket.receive(recebido);
            this.criarArquivo(recebido, nomeArquivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void criarArquivo(DatagramPacket recebido, String nomeArquivo) throws IOException {
        int cont = 0;
        while (recebido.getData()[cont] != 0) {
            cont++;
        }

        byte[] listaAjustada = new byte[cont];
        int i = 0;
        for (Byte dado: recebido.getData()) {
            if(dado != 0){
                listaAjustada[i] = dado;
                i++;
            }
        }

        File file = new File(nomeArquivo);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(listaAjustada);
        bos.close();

        String resposta = new String(recebido.getData());
        System.out.println("ARQUIVO RECEBIDO E SALVO COMO " + nomeArquivo);
    }
}