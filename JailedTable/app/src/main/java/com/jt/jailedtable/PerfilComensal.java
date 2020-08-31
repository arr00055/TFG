package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PerfilComensal extends AppCompatActivity implements ProtocoloData {
    String ComensalResultadoNombrePass;
    String ComensalResultadoIDUsuario;
    ArrayList<String> PerfilMostrarComensal = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilcomensal);
        this.setTitle(R.string.perfilcomensal_txtintro);

        Button botonalergenos    = (Button) findViewById(R.id.perfil_com_botAlerg);
        ImageButton botoninfo    = (ImageButton) findViewById(R.id.perfil_com_botInfo);
        Button botonrestaurantes = (Button) findViewById(R.id.perfil_com_botRest);
        Button botonreservas     = (Button) findViewById(R.id.perfil_com_botReserv);

        Bundle bundle = getIntent().getExtras();
        ListView ListaViewPerfilComensal = (ListView) findViewById(R.id.perfil_host_listviewDatosComensal);
        SharedPreferences prefs = getSharedPreferences("UsuarioIdComensal", Context.MODE_PRIVATE);
        String UserIDComensal = prefs.getString("IDComensal","");

        if (bundle != null) {
            String ComensalNombre = bundle.getString("NombreComensal");
            String ComensalPass   = bundle.getString("PasswordComensal");
            String busqueda = ComensalNombre+" "+ComensalPass;
            ConexionBuscarComensalNamePass conex2 = new ConexionBuscarComensalNamePass();
            try {
                ComensalResultadoNombrePass = conex2.execute(busqueda).get();
                if(ComensalResultadoNombrePass.isEmpty()==true){
                    String PerfilMostrar ="Se ha producido un error, para este usuario";
                    PerfilMostrarComensal.add(PerfilMostrar);
                }else{
                    String camps[] = ComensalResultadoNombrePass.split(" ");
                    if(camps.length==11){
                        String usuarioid = camps[0];
                        String nombre    = camps[1];
                        String apellidos = camps[2];
                        String numtlf    = camps[3];
                        String contacto  = camps[4];
                        String valorpos  = camps[5];
                        String valorneg  = camps[6];
                        String comunidad = camps[7];
                        String provincia = camps[8];
                        String localidad = camps[9];
                        String password  = camps[10];
                        String PerfilMostrar =  "Nombre: "+nombre+"\n"+"Primer Apellido: "+apellidos+"\n"+"Número de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto+"\n"+"Localización: "+comunidad+" "+provincia+" "+localidad;
                        PerfilMostrarComensal.add(PerfilMostrar);
                    } if (camps.length==12){
                        String usuarioid = camps[0];
                        String nombre1   = camps[1];
                        String nombre2   = camps[2];
                        String apellidos = camps[3];
                        String numtlf    = camps[4];
                        String contacto  = camps[5];
                        String valorpos  = camps[6];
                        String valorneg  = camps[7];
                        String comunidad = camps[8];
                        String provincia = camps[9];
                        String localidad = camps[10];
                        String password  = camps[11];
                        String PerfilMostrar =  "Nombre: "+nombre1+" "+nombre2+"\n"+"Primer Apellido: "+apellidos+"\n"+"Número de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto+"\n"+"Localización: "+comunidad+" "+provincia+" "+localidad;
                        PerfilMostrarComensal.add(PerfilMostrar);
                    }
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } if(UserIDComensal.length()>0 && bundle == null){
            Toast.makeText(getApplicationContext(), "Bienvenido de nuevo comensal con ID: "+UserIDComensal, Toast.LENGTH_SHORT).show();
            ConexionBuscarComensal conex = new ConexionBuscarComensal();
            try {
                ComensalResultadoIDUsuario=conex.execute(UserIDComensal).get();
                if(ComensalResultadoIDUsuario.isEmpty()==true){
                    String PerfilMostrar ="Se ha producido un error, para este usuario";
                    PerfilMostrarComensal.add(PerfilMostrar);
                }else{
                    String camps[] = ComensalResultadoIDUsuario.split(" ");
                    if(camps.length==11){
                        String usuarioid = camps[0];
                        String nombre    = camps[1];
                        String apellidos = camps[2];
                        String numtlf    = camps[3];
                        String contacto  = camps[4];
                        String valorpos  = camps[5];
                        String valorneg  = camps[6];
                        String comunidad = camps[7];
                        String provincia = camps[8];
                        String localidad = camps[9];
                        String password  = camps[10];
                        String PerfilMostrar =  "Nombre: "+nombre+"\n"+"Primer Apellido: "+apellidos+"\n"+"Núm de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto+"\n"+"Localización: "+comunidad+" "+provincia+" "+localidad;
                        PerfilMostrarComensal.add(PerfilMostrar);
                    } if (camps.length==12){
                        String usuarioid = camps[0];
                        String nombre1   = camps[1];
                        String nombre2   = camps[2];
                        String apellidos = camps[3];
                        String numtlf    = camps[4];
                        String contacto  = camps[5];
                        String valorpos  = camps[6];
                        String valorneg  = camps[7];
                        String comunidad = camps[8];
                        String provincia = camps[9];
                        String localidad = camps[10];
                        String password  = camps[11];
                        String PerfilMostrar =  "Nombre: "+nombre1+" "+nombre2+"\n"+"Primer Apellido: "+apellidos+"\n"+"Núm de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto+"\n"+"Localización: "+comunidad+" "+provincia+" "+localidad;
                        PerfilMostrarComensal.add(PerfilMostrar);
                    }
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, PerfilMostrarComensal);
        ListaViewPerfilComensal.setAdapter(adapter2);


        botonalergenos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Alérgenos.", Toast.LENGTH_SHORT).show();
                Intent botalg = new Intent(PerfilComensal.this, PerfilComAlergenos.class);
                startActivity(botalg);
            }
        });

        botoninfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Información.", Toast.LENGTH_SHORT).show();
                Intent e = new Intent(PerfilComensal.this, PerfilComInformacion.class);
                startActivity(e);
            }
        });

        botonrestaurantes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a los Restaurantes.", Toast.LENGTH_SHORT).show();
                Intent Irest = new Intent(PerfilComensal.this, PerfilComRestaurantes.class);
                startActivity(Irest);
            }
        });

        botonreservas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a sus Reservas.", Toast.LENGTH_SHORT).show();
                Intent Iresv = new Intent(PerfilComensal.this, PerfilComMisReservas.class);
                startActivity(Iresv);
            }
        });

    }

    public class ConexionBuscarComensal extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComensal.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String UsuarioComensalID = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BC+SP+UsuarioComensalID+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                bis.close();
                os.close();
                cliente.close();
            } catch (IOException err){
                err.printStackTrace();
                respuesta = "IOException: " + err.toString();
            }
            return respuesta;
        }

        /**
         * metodo onPostExecute el cual recibe la respuesta de la hebra y en funcion del valor que contiene esta respuesta se
         * encargara de enviar al usuario a la actividad del servicio, o si se ha producido algun error lo envia a la actividad
         * del login.
         * @param respuesta
         */
        @Override
        protected void onPostExecute(String respuesta) {
            super.onPostExecute(respuesta);
            pdia.dismiss();//Cierro el pdia.
        }//Fin onPostExecute.
    }

    public class ConexionBuscarComensalNamePass extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComensal.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String ComensalNamePass = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((MCNPT+SP+ComensalNamePass+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                bis.close();
                os.close();
                cliente.close();
            } catch (IOException err){
                err.printStackTrace();
                respuesta = "IOException: " + err.toString();
            }
            return respuesta;
        }

        @Override
        protected void onPostExecute(String respuesta) {
            super.onPostExecute(respuesta);
            pdia.dismiss();
        }
    }

}
