package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class CrearComensal extends AppCompatActivity implements ProtocoloData {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearcomensal);
        this.setTitle(R.string.crearcomensal_txt);

        Button botoncreacomensal = (Button) findViewById(R.id.crearcomensal_boton);

        botoncreacomensal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {

                EditText comensalnombre = (EditText) findViewById(R.id.crearcomensal_nombre);
                EditText comensalapellidos = (EditText) findViewById(R.id.crearcomensal_apellidos);
                EditText comensalnumtlf = (EditText) findViewById(R.id.crearcomensal_numtlf);
                EditText comensalcontacto = (EditText) findViewById(R.id.crearcomensal_contacto);
                EditText comensalcomunidad = (EditText) findViewById(R.id.crearcomensal_comun);
                EditText comensalprovincia = (EditText) findViewById(R.id.crearcomensal_provin);
                EditText comensallocalidad = (EditText) findViewById(R.id.crearcomensal_local);
                EditText comensalpassword = (EditText) findViewById(R.id.crearcomensal_pass);

                String nombre = comensalnombre.getText().toString();
                String apellidos = comensalapellidos.getText().toString();
                String numerotlf = comensalnumtlf.getText().toString();
                String contacto = comensalcontacto.getText().toString();
                String comunidad = comensalcomunidad.getText().toString();
                String provincia = comensalprovincia.getText().toString();
                String localidad = comensallocalidad.getText().toString();
                String password = comensalpassword.getText().toString();
                String valoracion_positiva = "0";
                String valoracion_negativa = "0";
                if(nombre.isEmpty() == false && apellidos.isEmpty() == false && numerotlf.isEmpty() == false && contacto.isEmpty() == false && comunidad.isEmpty() == false && provincia.isEmpty() == false && localidad.isEmpty() == false && password.isEmpty() == false) {
                    creausuariocomensal creandocomensal = new creausuariocomensal(nombre,apellidos,numerotlf,contacto,valoracion_positiva,valoracion_negativa,comunidad,provincia,localidad,password);
                    ConexionCrearComensal conectar = new ConexionCrearComensal();
                    conectar.execute(creandocomensal);
                } else {
                    Toast.makeText(getApplicationContext(), "Hay campos vacíos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class ConexionCrearComensal extends AsyncTask<creausuariocomensal, String, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(CrearComensal.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Creando Comensal.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(creausuariocomensal... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String Name = arg0[0].getNombre();
                String Apellido = arg0[0].getApellidos();
                String NumTlf = arg0[0].getNumTlf();
                String Contacto = arg0[0].getContacto();
                String valorpositivo = arg0[0].getValorPositivo();
                String valornegativo = arg0[0].getValorNegativo();
                String Comunidad = arg0[0].getComunidad();
                String Provincia = arg0[0].getProvincia();
                String Localidad = arg0[0].getLocalidad();
                String Password = arg0[0].getPassword();
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                String comensal = Name+"-"+Apellido+"-"+NumTlf+"-"+Contacto+"-"+valorpositivo+"-"+valornegativo+"-"+Comunidad+"-"+Provincia+"-"+Localidad+"-"+Password;
                os.write((IC+SP+comensal+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                bis.close();
                os.close();
                cliente.close();
                SharedPreferences prefs = getSharedPreferences("UsuarioIdComensal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("IDComensal", respuesta);
                editor.commit();
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
            if (respuesta != null) {
                    Toast.makeText(getApplicationContext(), "Su perfil de comensal ha sido creado.", Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(CrearComensal.this, PerfilComensal.class);
                    startActivity(a);
            }
        }
    }
}
