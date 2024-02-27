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
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
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
    ArrayList<ArrayList> listaArchivos = new ArrayList<>();

    public HiloCliente(DatagramSocket socket, DatagramPacket packetRecibir, ArrayList<ArrayList> lista) {
        this.socket = socket;
        this.packetRecibir = packetRecibir;
        this.listaArchivos = lista;
    }

    @Override
    public void run() {
        DatagramPacket packetEnviar = new DatagramPacket(new byte[255], 255, packetRecibir.getAddress(), packetRecibir.getPort());
//        String palabra = new String(packetRecibir.getData());
        String palabra = "s";
        String palabraC = "LISTA";
        System.out.println(palabra);
        if (palabra.contains(palabraC)) {
            try {
                File archivo = new File("./archivos");
                String[] archivosStrings = archivo.list();
                String cadenaArchivos = "";

                for (String listaArchivo : archivosStrings) {
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

            byte[] bytesRecibidos = packetRecibir.getData();

            byte[] bytesString = new byte[bytesRecibidos.length - Integer.BYTES];
            System.arraycopy(bytesRecibidos, 0, bytesString, 0, bytesString.length);

            byte[] bytesInt = new byte[Integer.BYTES];
            System.arraycopy(bytesRecibidos, bytesString.length, bytesInt, 0, bytesInt.length);

            String fileName = new String(bytesString).trim();
            ByteBuffer intBuffer = ByteBuffer.wrap(bytesInt);
            System.out.println(fileName);
            int numeroEntero = intBuffer.getInt();
            if (numeroEntero != 0) {
                System.out.println("pidio indivual");
                System.out.println("numero "+numeroEntero);
                File archivos = new File("./archivos");
                int i = 0;
                for (String listaArchivo : archivos.list()) {
                    if (fileName.contains(listaArchivo)) {
                        break;
                    }
                    i++;
                }
                ArrayList<byte[]> paquetes = listaArchivos.get(i);
                byte[] paquete = paquetes.get(numeroEntero);
                DatagramPacket recieveArchive;
                try {
                        byte[] fileData = Arrays.copyOfRange(paquete, 0, 1016);
                        byte[] etiquetas = Arrays.copyOfRange(paquete, 1016, 1024);
                        // Convertir los últimos 4 bytes a dos números short
                        ByteBuffer buffer2 = ByteBuffer.wrap(etiquetas);
                        int numeroPaquete = buffer2.getInt();
                        int totalDePaquetes = buffer2.getInt();
                        // Mostrar los números short
                        System.out.println("Número de paquete: " + numeroPaquete);
                        System.out.println("Total de paquetes: " + totalDePaquetes);
                        System.out.println("");
                        recieveArchive = new DatagramPacket(paquete, paquete.length, packetRecibir.getAddress(), packetRecibir.getPort());
                        socket.send(recieveArchive);
//                }
                    } catch (IOException ex) {
                        Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
            } else {

                File archivos = new File("./archivos");
                int i = 0;
                for (String listaArchivo : archivos.list()) {
                    if (fileName.contains(listaArchivo)) {
                        break;
                    }
                    i++;
                }
                DatagramPacket recieveArchive;
                ArrayList<byte[]> paquetes = listaArchivos.get(i);
                for (byte[] paquete : paquetes) {
                    try {
                        byte[] fileData = Arrays.copyOfRange(paquete, 0, 1016);
                        byte[] etiquetas = Arrays.copyOfRange(paquete, 1016, 1024);
                        // Convertir los últimos 4 bytes a dos números short
                        ByteBuffer buffer2 = ByteBuffer.wrap(etiquetas);
                        int numeroPaquete = buffer2.getInt();
                        int totalDePaquetes = buffer2.getInt();
                        // Mostrar los números short
                        System.out.println("Número de paquete: " + numeroPaquete);
                        System.out.println("Total de paquetes: " + totalDePaquetes);
                        System.out.println("");
                        recieveArchive = new DatagramPacket(paquete, paquete.length, packetRecibir.getAddress(), packetRecibir.getPort());
                        socket.send(recieveArchive);
//                }
                    } catch (IOException ex) {
                        Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

//        while (true) {
//            try {
//                socket.receive(packetRecibir);
//
//                String fileName = new String(packetRecibir.getData()).trim();
//                System.out.println("enviando :" + fileName);
//                
//                DatagramPacket recieveArchive;
//                
//               
//                
//                try (FileInputStream path = new FileInputStream("./archivos/" + fileName)) {
//
//                    // Lee el contenido actual del archivo (si existe)
//                    byte[] packet = new byte[1024];
//
//                    while (true) {
//                        
//                        if (!(path.read(packet) != -1)) {
//                            break;
//                        }
//                        System.out.println(packetRecibir.getAddress().toString()+" "+packetRecibir.getPort());
//                        recieveArchive = new DatagramPacket(packet, packet.length,packetRecibir.getAddress(),packetRecibir.getPort());
//                        socket.send(recieveArchive);
//                    }
//
//                    path.close();
//                }
//
//            } catch (IOException ex) {
//                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
        }

    }
}
