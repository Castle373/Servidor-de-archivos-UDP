/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class pruebaDeCliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SocketException, UnknownHostException {
        
        InetAddress serverAddres = InetAddress.getByName("192.168.1.254");
        int serverPort = 70;
        DatagramSocket socket = new DatagramSocket();
       
        
        String d= "LISTA";
        byte[] bytesToSend = d.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(bytesToSend,bytesToSend.length, serverAddres, serverPort);
        try {
            socket.send(sendPacket);//YA TE DIJE "DAME ARCHIVOS"
        } catch (IOException ex) {
            Logger.getLogger(pruebaDeCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        DatagramPacket recievePacket = new DatagramPacket(new byte[255],255);
        try {
            socket.receive(recievePacket); //ME ACABA DE ENVIAR LA LISTA DE ARCHIVOS
        } catch (IOException ex) {
            Logger.getLogger(pruebaDeCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        String archivos= new String(recievePacket.getData()); //ESTA ES LA LISTA DE ARCHIVOS
        System.out.println(archivos);
        System.out.println("me llego");
        
    }
    
}
