/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    private static final int ECHOMAX = 255;
    private static final ArrayList<ArrayList> listaArchivos = new ArrayList<>();
    
    private static void GenerarPaquetes() {
        File archivos = new File("./archivos");
        String[] archi = archivos.list();
        for (String listaArchivo : archi) {

            try (FileInputStream path = new FileInputStream("./archivos/" + listaArchivo)) {

                ArrayList<byte[]> paquetes = new ArrayList<>();
                int i = 1;
                long tamanoArchivo = path.available();
                int totalDePaquetes = (int) Math.ceil((double) tamanoArchivo / 1016);
                while (true) {
                    byte[] packet = new byte[1016];
                    byte[] packetNum = new byte[4];
                    byte[] packetMax = new byte[4];
                    byte[] packetFull = new byte[1024];
                    if (!(path.read(packet) != -1)) {
                        break;
                    }
                    System.out.println(i);
                    System.out.println(totalDePaquetes);
                    packetNum = ByteBuffer.allocate(4).putInt(i).array();
                    packetMax = ByteBuffer.allocate(4).putInt(totalDePaquetes).array();

                    System.arraycopy(packet, 0, packetFull, 0, packet.length);
                    System.arraycopy(packetNum, 0, packetFull, packet.length, packetNum.length);
                    System.arraycopy(packetMax, 0, packetFull, packet.length + packetNum.length, packetMax.length);
                    paquetes.add(packetFull);
                    i++;
                }
                listaArchivos.add(paquetes);
                path.close();
            } catch (IOException ex) {
                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Encendiendo server");
        GenerarPaquetes();
        System.out.println("Server Encendido");
        ArrayList<String> puertos = new ArrayList<>();
        Executor service = Executors.newCachedThreadPool();
        
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket(70);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                
                clientSocket.receive(packet);
                
                String destino = "";
                destino+=(packet.getAddress().toString());
                destino+=(String.valueOf(packet.getPort()));
                System.out.println(destino);
                if (!puertos.contains(destino)) {
                    System.out.println("Cliente nuevo");
                    service.execute(new HiloCliente(clientSocket,packet,listaArchivos));;
                    
                }else{
                    System.out.println("pidio algo raro");
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
