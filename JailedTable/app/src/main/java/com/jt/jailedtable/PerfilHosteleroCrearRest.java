package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PerfilHosteleroCrearRest extends AppCompatActivity implements ProtocoloData {

    String UserIDHostelero;
    String RestauranteInsertar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostcrearrest);
        this.setTitle(R.string.perfilhost_crearrest_head);

        SharedPreferences prefs = getSharedPreferences("UsuarioIdHostelero", Context.MODE_PRIVATE);
        UserIDHostelero = prefs.getString("IDHostelero","");

        Button BotonCrearUnRestaurante   = (Button) findViewById(R.id.perfil_host_crearest_botoncrearlo);

        BotonCrearUnRestaurante.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {

                ConexionCrearRestaurante CreandoRestaurante = new ConexionCrearRestaurante();

                EditText nombreRest    = (EditText) findViewById(R.id.perfil_host_crearrest_nombre);
                String NombreRestaurante = nombreRest.getText().toString();
                EditText comunidadRest = (EditText) findViewById(R.id.perfil_host_crearrest_comunidad);
                String ComunidadRestaurante = comunidadRest.getText().toString();
                EditText provinciaRest = (EditText) findViewById(R.id.perfil_host_crearrest_provincia);
                String ProvinciaRestaurate = provinciaRest.getText().toString();
                EditText localidadRest = (EditText) findViewById(R.id.perfil_host_crearrest_localidad);
                String LocalidadRestaurante = localidadRest.getText().toString();
                EditText numtlfRest    = (EditText) findViewById(R.id.perfil_host_crearrest_numtlf);
                String NumTlfRestaurante = numtlfRest.getText().toString();

                String partesNombreRest [] = NombreRestaurante.split(" ");
                if(NombreRestaurante.isEmpty() == false && ComunidadRestaurante.isEmpty() == false && ProvinciaRestaurate.isEmpty() == false && LocalidadRestaurante.isEmpty() == false && NumTlfRestaurante.isEmpty() == false){
                    if(partesNombreRest.length>2){
                        Toast.makeText(getApplicationContext(), "El Nombre del Restaurante contiene más de 2 palabras.", Toast.LENGTH_SHORT).show();
                    }else{
                        RestauranteInsertar = NombreRestaurante+"-"+ComunidadRestaurante+"-"+ProvinciaRestaurate+"-"+LocalidadRestaurante+"-"+NumTlfRestaurante;
                        CreandoRestaurante.execute(UserIDHostelero,RestauranteInsertar);
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Hay campos vacíos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class ConexionCrearRestaurante extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroCrearRest.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Creando Restaurante.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String iduserhost = arg0[0];
                String restaurante = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IREST+SP+iduserhost+"/"+restaurante+CRLF).getBytes());
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
            if (respuesta != null && respuesta.equalsIgnoreCase("0")==false) {
                Toast.makeText(getApplicationContext(), "Se ha creado el restaurante con ID "+respuesta, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
