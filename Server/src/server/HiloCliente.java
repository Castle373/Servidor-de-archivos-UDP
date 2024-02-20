/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 *
 */
public class HiloCliente extends Thread {

    private static final int ECHOMAX = 255;
    private DatagramSocket socket;
    private DatagramPacket packetRecibir;

    public HiloCliente(DatagramSocket socket, DatagramPacket packetRecibir) {
        this.socket = socket;
        this.packetRecibir = packetRecibir;
    }

    @Override
    public void run() {

        DatagramPacket packetEnviar = new DatagramPacket(new byte[255], 255, packetRecibir.getAddress(), packetRecibir.getPort());
        String palabra = new String(packetRecibir.getData());
        String palabraC = "LISTA";

        if (palabra.contains(palabraC)) {
            try {
                File archivo = new File("./archivos");
                String[] listaArchivos = archivo.list();
                String cadenaArchivos = "";

                for (String listaArchivo : listaArchivos) {

                    cadenaArchivos += (listaArchivo);
                    cadenaArchivos += " ";

                }

                packetEnviar.setData(cadenaArchivos.getBytes());
                System.out.println("enviandox");
                System.out.println(packetRecibir.getAddress().toString() + " " + packetRecibir.getPort());
                socket.send(packetEnviar);
                packetEnviar.setLength(packetEnviar.getData().length);
            } catch (IOException ex) {
                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
           
        }

        while (true) {
            try {
                socket.receive(packetRecibir);

                String fileName = new String(packetRecibir.getData()).trim();
                System.out.println("enviando :" + fileName);
                
                DatagramPacket recieveArchive;
                
               
                
                try (FileInputStream path = new FileInputStream("./archivos/" + fileName)) {

                    // Lee el contenido actual del archivo (si existe)
                    byte[] packet = new byte[1024];

                    while (true) {
                        
                        if (!(path.read(packet) != -1)) {
                            break;
                        }
                        System.out.println(packetRecibir.getAddress().toString()+" "+packetRecibir.getPort());
                        recieveArchive = new DatagramPacket(packet, packet.length,packetRecibir.getAddress(),packetRecibir.getPort());
                        socket.send(recieveArchive);
                    }

                    path.close();
                }

            } catch (IOException ex) {
                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
