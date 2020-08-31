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

public class PerfilHostelero extends AppCompatActivity implements ProtocoloData {
    String HosteleroResultadoNombrePass;
    String HosteleroResultadoIDUsuario;
    ArrayList<String> PerfilMostrarHostelero = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostelero);
        this.setTitle(R.string.perfilhostelero_perfil_cabecera);
        Bundle bundle = getIntent().getExtras();
        ListView ListaViewPerfilHostelero = (ListView) findViewById(R.id.perfil_host_listviewDatosHostelero);

        Button botoncrearrest   = (Button) findViewById(R.id.perfil_host_boton_crearest);
        Button botonmisrest     = (Button) findViewById(R.id.perfil_host_boton_misrest);
        Button botoninformacion = (Button) findViewById(R.id.perfil_host_boton_info);

        SharedPreferences prefs = getSharedPreferences("UsuarioIdHostelero", Context.MODE_PRIVATE);
        String UserIDHostelero = prefs.getString("IDHostelero","");
        if (bundle != null) {
            String HosteleroNombre = bundle.getString("NombreComensal");
            String HosteleroPass   = bundle.getString("PasswordComensal");
            String busqueda = HosteleroNombre+SP+HosteleroPass;
            ConexionBuscarHosteleroNamePass conex2 = new ConexionBuscarHosteleroNamePass();
            try {
                HosteleroResultadoNombrePass = conex2.execute(busqueda).get();
                if(HosteleroResultadoNombrePass.isEmpty()==true){
                    String PerfilMostrar ="Se ha producido un error, para este usuario";
                    PerfilMostrarHostelero.add(PerfilMostrar);
                }else{
                    String camps[] = HosteleroResultadoNombrePass.split(" ");
                    if(camps.length==6){
                        String usuarioid = camps[0];
                        String nombre    = camps[1];
                        String apellidos = camps[2];
                        String numtlf    = camps[3];
                        String contacto  = camps[4];
                        String password  = camps[5];
                        String PerfilMostrar =  "Nombre: "+nombre+"\n"+"Primer Apellido: "+apellidos+"\n"+"Número de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto;
                        PerfilMostrarHostelero.add(PerfilMostrar);
                    } if (camps.length==7){
                        String usuarioid  = camps[0];
                        String nombre1    = camps[1];
                        String nombre2    = camps[2];
                        String apellidos  = camps[3];
                        String numtlf     = camps[4];
                        String contacto   = camps[5];
                        String password   = camps[6];
                        String PerfilMostrar =  "Nombre: "+nombre1+" "+nombre2+"\n"+"Primer Apellido: "+apellidos+"\n"+"Número de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto;
                        PerfilMostrarHostelero.add(PerfilMostrar);
                    }
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } if(UserIDHostelero.length()>0 && bundle == null){
            Toast.makeText(getApplicationContext(), "Bienvenido de nuevo hostelero con ID: "+UserIDHostelero, Toast.LENGTH_SHORT).show();
            ConexionBuscarHostelero conex = new ConexionBuscarHostelero();
            try {
                HosteleroResultadoIDUsuario=conex.execute(UserIDHostelero).get();
                if(HosteleroResultadoIDUsuario.isEmpty()==true){
                    String PerfilMostrar ="Se ha producido un error, para este usuario";
                    PerfilMostrarHostelero.add(PerfilMostrar);
                }else{
                    String camps[] = HosteleroResultadoIDUsuario.split(" ");
                    if(camps.length==6){
                        String usuarioid = camps[0];
                        String nombre    = camps[1];
                        String apellidos = camps[2];
                        String numtlf    = camps[3];
                        String contacto  = camps[4];
                        String password  = camps[5];
                        String PerfilMostrar =  "Nombre: "+nombre+"\n"+"Primer Apellido: "+apellidos+"\n"+"Núm de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto;
                        PerfilMostrarHostelero.add(PerfilMostrar);
                    } if (camps.length==7){
                        String usuarioid  = camps[0];
                        String nombre1    = camps[1];
                        String nombre2    = camps[2];
                        String apellidos  = camps[3];
                        String numtlf     = camps[4];
                        String contacto   = camps[5];
                        String password   = camps[6];
                        String PerfilMostrar =  "Nombre: "+nombre1+" "+nombre2+"\n"+"Primer Apellido: "+apellidos+"\n"+"Núm de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto;
                        PerfilMostrarHostelero.add(PerfilMostrar);
                    }
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, PerfilMostrarHostelero);
        ListaViewPerfilHostelero.setAdapter(adapter2);

        botoncrearrest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Creación de Restaurante.", Toast.LENGTH_SHORT).show();
                Intent IcrearRest = new Intent(PerfilHostelero.this, PerfilHosteleroCrearRest.class);
                startActivity(IcrearRest);
            }
        });

        botonmisrest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a sus Restaurantes.", Toast.LENGTH_SHORT).show();
                Intent ImisRest= new Intent(PerfilHostelero.this, PerfilHosteleroMisRest.class);
                startActivity(ImisRest);
            }
        });

        botoninformacion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Información.", Toast.LENGTH_SHORT).show();
                Intent Iinfo = new Intent(PerfilHostelero.this, PerfilHosteleroInfo.class);
                startActivity(Iinfo);
            }
        });
    }


    public class ConexionBuscarHostelero extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHostelero.this);
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
                String UsuarioHosteleroID = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BH+SP+UsuarioHosteleroID+CRLF).getBytes());
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

    public class ConexionBuscarHosteleroNamePass extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHostelero.this);
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
                String HosteleroNamePass = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((MHNPT+SP+HosteleroNamePass+CRLF).getBytes());
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
