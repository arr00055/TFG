package clases;

import java.io.*;
import java.net.*;


public class Client {
     public static final int PUERTO_TCP = 5000;
         public static final String CRLF = "\r\n";

      
     public static void main (String[] args) throws IOException {
        Socket cliente = null;
        String respuesta = null;
        String outputData = "";
        try {	
        InetAddress ip = InetAddress.getLocalHost();
        InetSocketAddress direccion = new InetSocketAddress(ip,PUERTO_TCP); 
        cliente = new Socket();
        cliente.connect(direccion);
        BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        OutputStream os = cliente.getOutputStream();
        System.out.println("Lanzando Cliente");
        respuesta = bis.readLine();
        System.out.println("Cliente HA RECIBIDO: " + respuesta); //Recibimos el "OK" tras enviar el User al servidor.
        os.write(("BVALC 14"+CRLF).getBytes());
        os.flush();
        respuesta = bis.readLine();
        System.out.println("Cliente HA RECIBIDO: " + respuesta);
        //int respuesta2 = Integer.parseInt(respuesta);
        //System.out.println("Cliente HA RECIBIDO Entero: " + respuesta2);
        os.close();//Cierro el buffer de OUT.
        cliente.close();//Cierro el cliente socket.
        System.out.println("Desconectando cliente.");
	} catch (IOException error) {
	System.err.println("ERROR: " + error.getMessage());
        }
   }//Fin del main.
}//Fin de Client.
