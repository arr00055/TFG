package clases;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Comunicacion implements Runnable {
    public static final String QUIT = "QUIT"; //Salir.
    public static final String OK = "OK";     //Un OK. 
    public static final String CRLF = "\r\n"; //CRLF. 
    Socket miSocket; 

    public Comunicacion(Socket sock) {
        miSocket = sock;
    }
    
    @Override
    public void run() {
        String outputData = "";
        String inputData = null;
	String com = "";
	String param = "";
        
        if (miSocket != null){
            try{
                
        //Flujos de datos de entrada y salida. 
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(miSocket.getInputStream()));
	DataOutputStream outputStream = new DataOutputStream(miSocket.getOutputStream());
        
        //Envio un mensaje al cliente de OK cuando se conecta a modo de bienvenida para arrancar. 
        outputData=OK+CRLF;
	outputStream.write(outputData.getBytes());
	outputStream.flush();
        
        while ((inputData = inputStream.readLine()) != null){
            //Procedo a leer la informacion que ha sido colocada en el flujo de entrada. 
            System.out.println("SERVIDOR HA RECIBIDO: " + inputData);
            //Comprobacion del flujo enviado, para saber si es un comando o contiene informacion adicional.
	    String fields[] = inputData.split(" "); //Formateo COM CAMP1 CAMPN
            //Si el flujo al ser separado, solo cuenta con un campo, es que es solo un COM.
	    if (fields.length == 1){ 
                //NO hay informacion adicional, solo el COM que es guardado en la entrada.
	        com = inputData;
	        param = null;
                //System.out.println(com);
            //Si el flujo al ser separado, cuenta con dos campos o mas, quiere decir que es un com con informacion adicional.
	    }else if (fields.length >= 2){ 
                //Extraigo el comando. 
                com = fields[0];
                System.out.println(com);
                //Se extrae toda la informacion que es util y va mas allá del primer campo que es un COM.
		param = inputData.substring(inputData.indexOf(" ")); 
		param = param.trim(); //Limpiar los espacios. 
            }//Fin de la comprobacion de la entrada. 

            if(com.equalsIgnoreCase("RC")==true){ //Regresar Comensal.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String name = camps[0];
                    String pass = camps[1];
                    Boolean a = conecBD.RegresarComensal(name,pass);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("RH")==true){ //Regresar Hostelero.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String name = camps[0];
                    String pass = camps[1];
                    Boolean a = conecBD.RegresarHostelero(name,pass);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BA")==true){ //Buscar Alergeno por su ID y devuelve su nombre.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idalerg = Integer.parseInt(campo1);
                    String a = conecBD.BuscarAlergeno(idalerg);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IAC")==true){ //Insertar Alergenos en la BD por un Comensal.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split("-"); //os.write(("IAC Cacahuete Pera Manzana -12"+CRLF).getBytes());
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idcomensal = Integer.parseInt(campo2);
                    conecBD.insertarAlergenoComensal(campo1,idcomensal);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IAM")==true){ //Insertar Alergenos en la BD por un Menu.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split("-"); //os.write(("IAM Chocolate Lacteos Soja Cereal -5"+CRLF).getBytes());
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idmenu = Integer.parseInt(campo2);
                    conecBD.InsertarAlergenoMenu(campo1,idmenu);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BSIDC")==true){ //Busco en la tabla selecciona, la cual relacciona un alergeno con un Comensal.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" "); 
                    String campo1 = camps[0];
                    int idcomensal = Integer.parseInt(campo1);
                    String result = conecBD.BuscarSeleccionaPorIDComensal(idcomensal);
                    System.out.println(result);
                    outputData = String.valueOf(result)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BSIDA")==true){ //Busco en la tabla selecciona, la cual relacciona un alergeno con un Comensal.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" "); 
                    String campo1 = camps[0];
                    int idalerg = Integer.parseInt(campo1);
                    String result = conecBD.BuscarSeleccionaPorIDAlergeno(idalerg);
                    System.out.println(result);
                    outputData = String.valueOf(result)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IC")==true){ //Insertar Comensal
            //Para insertar correctamente se debe formatear de esta forma en el cliente:
            //String comensal = "Siri-Severo Hassan-911623459-pe1@hotmail.com-0-0-Andalucia-Jaen-Linares-4040";
            //os.write(("IC "+comensal+CRLF).getBytes());
                    ConectarBD conecBD = new ConectarBD();
                    int b = conecBD.insertarComensal(param);
                    System.out.println(b);
                    outputData = String.valueOf(b)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }            
            
            if(com.equalsIgnoreCase("BC")==true){ //Buscar Comensal por su ID y devuelve toda su informacion.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idcomen = Integer.parseInt(campo1);
                    String a = conecBD.BuscarComensal(idcomen);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("MCNP")==true){ //Regresa el ID_Usuario de un comensal por su name y password.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String name = camps[0];
                    String pass = camps[1];
                    int idresult = conecBD.MostrarComensalNamePass(name,pass);
                    System.out.println(idresult);
                    outputData = String.valueOf(idresult)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("MCNPT")==true){ //Regresa toda la informacion de un comensal por su name y password.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String name = camps[0];
                    String pass = camps[1];
                    String result = conecBD.MostrarComensalNamePassTodo(name,pass);
                    System.out.println(result);
                    outputData = String.valueOf(result)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IH")==true){ //Insertar Hostelero
            //Para insertar correctamente se debe formatear de esta forma en el cliente:
            //String hostelero = "Jose-Sanchez Ruiz-911121314-jsr@hotmail.com-123456";
            //os.write(("IC "+hostelero+CRLF).getBytes());
                    ConectarBD conecBD = new ConectarBD();
                    int b = conecBD.insertarHostelero(param);
                    System.out.println(b);
                    outputData = String.valueOf(b)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }  
            
            if(com.equalsIgnoreCase("BH")==true){ //Buscar Hostelero por su ID y devuelve toda su informacion.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idcomen = Integer.parseInt(campo1);
                    String a = conecBD.BuscarHostelero(idcomen);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("MHNP")==true){ //Regresa el ID_Usuario de un hostelero por su name y password.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String name = camps[0];
                    String pass = camps[1];
                    int idresult = conecBD.MostrarHosteleroNamePass(name,pass);
                    System.out.println(idresult);
                    outputData = String.valueOf(idresult)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("MHNPT")==true){ //Regresa toda la informacion de un hostelero por su name y password.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String name = camps[0];
                    String pass = camps[1];
                    String result = conecBD.MostrarHosteleroNamePassTodo(name,pass);
                    System.out.println(result);
                    outputData = String.valueOf(result)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IM")==true){ //Insertar Menu.
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.insertarMenu(param);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BM")==true){ //Buscar un menu por su ID.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idmenu = Integer.parseInt(campo1);
                    String a = conecBD.BuscarMenu(idmenu);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IP")==true){ //Insertar Plato.
            //Para insertar correctamente se debe formatear de esta forma en el cliente:
            //String platito = "Este plato trae una chuleta de cerdo y patatas fritas-11.20-Chuleta de cerdo y patatas";;
            //os.write(("IP "+platito+CRLF).getBytes());
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.insertarPlato(param);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BP")==true){ //Buscar un Plato por su ID.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idplato = Integer.parseInt(campo1);
                    String a = conecBD.BuscarPlato(idplato);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IPOL")==true){ //Insertar Politicas.
            //Para insertar correctamente se debe formatear de esta forma en el cliente:
            //String politica = "1 - 0";
            //os.write(("IPOL "+politica+CRLF).getBytes());
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.insertarPoliticas(param);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BPOL")==true){ //Buscar una Politica por su ID.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idpolitic = Integer.parseInt(campo1);
                    String a = conecBD.BuscarPoliticas(idpolitic);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IRESV")==true){ //Insertar Reserva.
            //Para insertar correctamente se debe formatear de esta forma en el cliente:
            //String reserva = "2020-07-31 11 2 3";
            //os.write(("IRESV "+reserva+CRLF).getBytes());
                    String camps[] = param.split(" ");
                    String fecha = camps[0];
                    String campo1 = camps[1];
                    String campo2 = camps[2];
                    String campo3 = camps[3];
                    int iduser = Integer.parseInt(campo1);
                    int idmesa = Integer.parseInt(campo2);
                    int idrest = Integer.parseInt(campo3);
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.insertarReserva(fecha,iduser,idmesa,idrest);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BRESV")==true){ //Buscar una Reserva por su ID_Reserva
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idreserv = Integer.parseInt(campo1);
                    String a = conecBD.BuscarReservasPorReserva(idreserv);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BRESVC")==true){ //Buscar una Reserva por su ID_Comensal
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int iduser = Integer.parseInt(campo1);
                    String a = conecBD.BuscarReservasPorComensal(iduser);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IREST")==true){ //Insertar Restaurante.
            //Para insertar correctamente se debe formatear de esta forma en el cliente:
            //String restaurante = "Casa Pepe-Madrid-Madrid-Madrid-911121233";
            //int iduserhost = 4;
            //os.write(("IREST "+iduserhost+"/"+restaurante+CRLF).getBytes());
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split("/");
                    String campo1 = camps[0];
                    String restaurant = camps[1];
                    int iduserhost = Integer.parseInt(campo1);
                    conecBD.insertarRestaurante(iduserhost,restaurant);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
           if(com.equalsIgnoreCase("BREST")==true){ //Buscar un Restaurante por ID del restaurante.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idrest = Integer.parseInt(campo1);
                    String a = conecBD.BuscarRestaurante(idrest);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
           
            if(com.equalsIgnoreCase("BRESTH")==true){ //Buscar un Restaurante por ID del hostelero.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idhost = Integer.parseInt(campo1);
                    String a = conecBD.BuscarRestauranteIdHostelero(idhost);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IMESA")==true){ //Insertar Mesa.
            //Para insertar correctamente se debe formatear de esta forma en el cliente:
            //String mesa = "250.25-3-10";
            //int idresta = 1;
            //os.write(("IMESA "+idresta+"/"+mesa+CRLF).getBytes());
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split("/");
                    String campo1 = camps[0];
                    String mesa = camps[1];
                    int idrestaurant = Integer.parseInt(campo1);
                    conecBD.insertarMesas(idrestaurant,mesa);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BMESA")==true){ //Buscar una Mesa por su ID_Mesa
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idmesa = Integer.parseInt(campo1);
                    String a = conecBD.BuscarMesas(idmesa);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BMESR")==true){ //Buscar una Mesa por su ID_Restaurante 
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idrest = Integer.parseInt(campo1);
                    String a = conecBD.BuscarMesasIDRest(idrest);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BMES")==true){ //Buscar una Mesa por idmesa e idrestaurante.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idmesa = Integer.parseInt(campo1);
                    int idrest = Integer.parseInt(campo2);
                    String a = conecBD.BuscarMesa(idmesa,idrest);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("ICOMP")==true){ //Insertar Compone.
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idmenu = Integer.parseInt(campo1);
                    int idplato = Integer.parseInt(campo2);
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.insertarCompone(idmenu, idplato);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BCOMPM")==true){ //Buscar en compone por idmenu.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idmenu = Integer.parseInt(campo1);
                    String a = conecBD.BuscarComponeporIDMenu(idmenu);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BCOMPP")==true){ //Buscar en compone por idplato.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idplato = Integer.parseInt(campo1);
                    String a = conecBD.BuscarComponeporIDPlato(idplato);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IDISP")==true){ //Insertar Dispone.
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idmenu = Integer.parseInt(campo1);
                    int idalerg = Integer.parseInt(campo2);
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.InsertarDispone(idmenu, idalerg);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BDISPM")==true){ //Buscar en dispone por idmenu.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idmenu = Integer.parseInt(campo1);
                    String a = conecBD.BuscarDisponeporIDMenu(idmenu);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BDISPAL")==true){ //Buscar en dispone por idalergeno.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idalerg = Integer.parseInt(campo1);
                    String a = conecBD.BuscarDisponeporIDAlergeno(idalerg);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
             if(com.equalsIgnoreCase("IEST")==true){ //Insertar Establece.
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idmenu = Integer.parseInt(campo1);
                    int idrest = Integer.parseInt(campo2);
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.InsertarEstablece(idmenu, idrest);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BESTM")==true){ //Buscar en establece por idmenu.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idmenu = Integer.parseInt(campo1);
                    String a = conecBD.BuscarEstableceporIDMenu(idmenu);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BESTR")==true){ //Buscar en establece por idrest.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idrest = Integer.parseInt(campo1);
                    String a = conecBD.BuscarEstableceporIDRestaurante(idrest);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IHAB")==true){ //Insertar Habilita.
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idpolitic = Integer.parseInt(campo1);
                    int idrest = Integer.parseInt(campo2);
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.InsertarHabilita(idpolitic, idrest);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BHABP")==true){ //Buscar en habilita por IDPolitica.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idpolitic = Integer.parseInt(campo1);
                    String a = conecBD.BuscarHabilitaporIDPolitica(idpolitic);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BHABR")==true){ //Buscar en habilita por idRestaurante.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idrest = Integer.parseInt(campo1);
                    String a = conecBD.BuscarHabilitaporIDRestaurante(idrest);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("IVAL")==true){ //Insertar Valora.
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    String campo2 = camps[1];
                    int idcomensal = Integer.parseInt(campo1);
                    int idrest = Integer.parseInt(campo2);
                    ConectarBD conecBD = new ConectarBD();
                    conecBD.InsertarValora(idcomensal, idrest);
                    outputData = OK+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            } 
            
            if(com.equalsIgnoreCase("BVALC")==true){ //Buscar en Valora por IDComensal.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idcomensal = Integer.parseInt(campo1);
                    String a = conecBD.BuscarValoraporIDUsuario(idcomensal);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            if(com.equalsIgnoreCase("BVALR")==true){ //Buscar en Valora por idRestaurante.
                    ConectarBD conecBD = new ConectarBD();
                    String camps[] = param.split(" ");
                    String campo1 = camps[0];
                    int idrest = Integer.parseInt(campo1);
                    String a = conecBD.BuscarValoraporIDRestaurante(idrest);
                    System.out.println(a);
                    outputData = String.valueOf(a)+CRLF;
                    System.out.println(outputData);
                    outputStream.write(outputData.getBytes());
	            outputStream.flush();
            }
            
            }//Fin While. 
        
                    System.out.println("Conexion finalizada");
                    //Se cierran los Stream y el socket. 
                    outputStream.close();
                    inputStream.close();
                    miSocket.close();
                    
            }catch(SocketException er1){
                System.err.println("SERVER ERROR: " + er1.getMessage());
            }catch(IOException er2){
                System.err.println("SERVER ERROR: " + er2.getMessage());
            } catch (SQLException ex) {
                Logger.getLogger(Comunicacion.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(Comunicacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }//Fin del run.
    
}//Fin clase Comunicacion.
