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

        DatagramPacket packetEnviar = new DatagramPacket(new byte[255], 255,packetRecibir.getAddress(),packetRecibir.getPort());
        String palabra = new String(packetRecibir.getData());
        String palabraC = "LISTA";
        
        if (palabra.contains(palabraC)) {
            try {
                File archivo = new File("./archivos");
                String[] listaArchivos = archivo.list();
                String cadenaArchivos = "";
                
                for (String listaArchivo : listaArchivos) {
                   
                    cadenaArchivos+=(listaArchivo);
                    cadenaArchivos+=" ";
                    
                }
                
                packetEnviar.setData(cadenaArchivos.getBytes());
                
                socket.send(packetEnviar);
                packetEnviar.setLength(packetEnviar.getData().length);
            } catch (IOException ex) {
                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("noentro");
        }
        packetRecibir = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
        while (true) {
            try {
                socket.receive(packetRecibir);
                String fileName = new String(packetRecibir.getData()).trim();

                DatagramPacket recieveArchive;

                //Lo busca en el directorio por el nombre
                try (FileInputStream path = new FileInputStream("./archivos/" + fileName)) {

                    // Lee el contenido actual del archivo (si existe)
                    byte[] packet = new byte[1024];

                    while (true) {

                        if (!(path.read(packet) != -1)) {
                            break;
                        }
                        recieveArchive = new DatagramPacket(packet, packet.length);

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
