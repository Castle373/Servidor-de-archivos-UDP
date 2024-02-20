/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package archivosudp;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marcos T.
 */
public class ClienteSocket extends Thread {

    private InetAddress serverAddres;
    private int serverPort;
    private DatagramSocket socket;
    private Archivos pantallaArchivo;

    public ClienteSocket(Archivos pantalla) {
        try {
            serverAddres = InetAddress.getByName("192.168.100.20");
            serverPort = 1234;
            socket = new DatagramSocket();
            pantallaArchivo = pantalla;
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        try {
            String mensaje = "dame";
            byte[] bytesToSend = mensaje.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddres, serverPort);
            socket.send(sendPacket);
            DatagramPacket recievePacket = new DatagramPacket(new byte[255], 255);
            socket.receive(recievePacket);
            String archivos = ("archivo1.jpg archivo2.pdf archivo3.rar");
            String[] nombresArchivos = archivos.split("\\s+");

            List<String> listaNombresArchivos = new ArrayList<>();

            for (String nombreArchivo : nombresArchivos) {
                listaNombresArchivos.add(nombreArchivo);
                
            }
            
            pantallaArchivo.mostrarNombresArchivos(listaNombresArchivos);
        } catch (Exception e) {
        }

    }

}
