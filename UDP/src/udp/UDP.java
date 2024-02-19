/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package udp;
    
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author diego
 */
public class UDP {
    private static final int TIMEOUT =3000;
    private static final int MAXTRIES =5;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{

        InetAddress serverAddres = InetAddress.getByName("192.168.0.112");
        
        String d= "diego";
        byte[] bytesToSend = d.getBytes();
        
    
        int serverPort = 7;
        
        DatagramSocket socket = new DatagramSocket();
        
        DatagramPacket sendPacket = new DatagramPacket(bytesToSend,bytesToSend.length, serverAddres, serverPort);
        DatagramPacket recievePacket = new DatagramPacket(new byte[255],255);
        
        
        int tries=0;     
        boolean recieveResponse =false;
        do {
        socket.send(sendPacket);
            try {
                socket.receive(recievePacket);
                
                if (!recievePacket.getAddress().equals(serverAddres)) {
                   throw new IOException("llego un paquete de un lugar desconocido");    
                }
                
                recieveResponse =true;
                
            } catch (InterruptedIOException e) {
            tries+=1;
                System.err.println("TIME OUT, "+(MAXTRIES-tries)+" more tries");
            }
            
        } while ((!recieveResponse)&&(tries<MAXTRIES));
        
        
        if (recieveResponse) {
            System.out.println("received: "+ new String(recievePacket.getData()));
        }else{
            System.out.println("no llego nada");
        }
        socket.close();
    }
    
}
