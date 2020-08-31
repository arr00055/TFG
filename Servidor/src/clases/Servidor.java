package clases;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
     public static final int PUERTO_TCP = 5000;
     static ServerSocket server = null;
      //En caso de no tener conexion cogerá la IP 127.0.0.1
     public static void main (String[] args) throws IOException {
        System.out.println("SERVER: Lanzando servidor");
        try {	
        InetAddress ip = InetAddress.getLocalHost(); //Saco la direccion de mi equipo.
        //String hostname = ip.getHostAddress(); System.out.println(hostname); Saca un string con la dir Ipv4.  
	server = new ServerSocket(PUERTO_TCP,50,ip);
	System.out.println("SERVER Esperando en: "+server.getInetAddress().toString());
	} catch (IOException error) {
	System.err.println("ERROR: " + error.getMessage());
        }while (true){
            try{
            Socket socket = server.accept(); //Espera a que entre una conexion. 
            System.out.println("SERVER Conexion entrante con: " + socket.getInetAddress().toString() + ":"+ socket.getPort());
            new Thread(new Comunicacion(socket)).start();// El socket se pasa a la clase Comunicacion para que sea tratada y en dicha clase es donde se establece el intercambio de los mensajes.
            }catch (IOException error) {
	     System.err.println("ERROR: " + error.getMessage());
            }
         }//Fin While.
        }//Fin del main.
     } //Fin clase Servidor.

