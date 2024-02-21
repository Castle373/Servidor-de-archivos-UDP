/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
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

    public static void main(String[] args) {
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
                DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
                
                clientSocket.receive(packet);
                
                String destino = "";
                destino+=(packet.getAddress().toString());
                destino+=(String.valueOf(packet.getPort()));
              
                if (!puertos.contains(destino)) {
//                    puertos.add(destino);
                    service.execute(new HiloCliente(clientSocket,packet));;
                    System.out.println("Cliente nuevo");
                }else{
                    System.out.println("pidio algo raro");
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
