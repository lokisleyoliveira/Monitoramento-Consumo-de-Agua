package Main;

import model.Receptor;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Server implements Runnable{

    private DatagramSocket socketUDP;
    private ServerSocket socketTCP;
    private Thread principal;

    /**
     * Inicializa o servidor, e realiza a leitura do arquivo de persistencia
     * @param args
     */
    public static void main (String[] args) {
        try {
            Controller.getInstance().leArquivo();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erro na leitura do arquivo de persistencia");
        }
        System.out.print("Informe a porta a ser usada pelo servidor: ");
        Scanner scanner = new Scanner(System.in);
        Server s = new Server(scanner.nextInt());
        System.out.println("O Servidor foi inicializado");
    }

    /**
     * Construtor do servidor, que o inicializa abrindo a conexao TCP e UDP
     * @param port
     */
    public Server (int port){
        try {
            socketUDP = new DatagramSocket(port);
            socketTCP = new ServerSocket(port);
        } catch (SocketException e) {
            System.out.println("Erro ao abrir a conexão TCP");
        } catch (IOException e) {
            System.out.println("Erro ao abrir conexão UDP");
        }
        principal = new Thread(this);
        principal.start();
    }

    /**
     * Mantem a conexao aberta para os dois protocolos de aplicação
     */
    @Override
    public void run() {
        conexaoUDP();
        conexaoTCP();
    }

    /**
     * Inicializa a conexao UDP
     */
    public void conexaoUDP() {
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        byte[] dados = new byte[1024];
                        DatagramPacket datagramPacket = new DatagramPacket(dados, dados.length);
                        socketUDP.receive(datagramPacket);
                        new Thread(new Receptor(datagramPacket)).start();
                    } catch (IOException e) {
                        System.out.println("Erro ao receber pacote UDP");
                    }
                }
            }
        }.start();
    }

    /**
     * Inicializa a conexao TCP
     */
    public void conexaoTCP() {
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        Socket recebido = socketTCP.accept();
                        new Thread(new Receptor(recebido)).start();
                    } catch (IOException e) {
                        System.out.println("Erro ao receber pacote TCP");
                    }
                }
            }
        }.start();
    }
}
