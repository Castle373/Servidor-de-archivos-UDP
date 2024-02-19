/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author diego
 */
public class Server {

    private static final int ECHOMAX = 255;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        if (args.length!=1) {
//           throw new IllegalArgumentException("puerto");   
//        }
        
         int servPort = 7;
         DatagramSocket socket = new DatagramSocket(servPort);
         DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
         
         while (true) {            
             socket.receive(packet);
             System.out.println("handling client at "+packet.getAddress().getHostAddress()+"on port "+ packet.getPort());
             
             String otro= "quequieres";
             
            packet.setData(otro.getBytes());
             
            socket.send(packet);
            
            packet.setLength(packet.getData().length);
         }
    }
    
}
