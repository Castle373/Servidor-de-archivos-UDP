/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package archivosudp;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marcos T.
 */
public class ClienteSocket extends Thread {

    private InetAddress serverAddres;
    private int serverPort;
    private DatagramSocket socket;
    private Archivos pantallaArchivo;
    private byte[] receiveData = new byte[1024];
    private FileOutputStream fileOutputStream;
    private DatagramPacket receivePacket;

    public ClienteSocket(Archivos pantalla) {
        try {
            serverAddres = InetAddress.getByName("localhost");
            serverPort = 70;
            socket = new DatagramSocket();
            pantallaArchivo = pantalla;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void enviarSocket(String mensaje) {
        byte[] bytesToSend = mensaje.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddres, serverPort);
        try {
            fileOutputStream = new FileOutputStream(mensaje);
            
            socket.send(sendPacket);

        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            String mensaje = "LISTA";
            byte[] bytesToSend = mensaje.getBytes();
            DatagramPacket recievePacket = new DatagramPacket(new byte[255], 255);
            DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddres, serverPort);
            socket.send(sendPacket);

            socket.receive(recievePacket);
            String archivos = new String(recievePacket.getData());
//            archivos = archivos.trim();
            String[] nombresArchivos = archivos.split("\\s+");

            List<String> listaNombresArchivos = new ArrayList<>();

            for (String nombreArchivo : nombresArchivos) {
                listaNombresArchivos.add(nombreArchivo);
                System.out.println(nombreArchivo);
            }
            pantallaArchivo.mostrarNombresArchivos(listaNombresArchivos);

            while (true) {
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                
                socket.receive(receivePacket);
               
                fileOutputStream.write(receivePacket.getData(), 0, receivePacket.getLength());
                if (receivePacket.getLength() < 1024) {
                    break;
                }
            }      

        } catch (Exception e) {

        }

    }

}
