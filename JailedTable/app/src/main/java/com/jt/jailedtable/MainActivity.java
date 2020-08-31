package com.jt.jailedtable;

import androidx.appcompat.app.AppCompatActivity;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements ProtocoloData {
    String nameComensal = "";
    String passComensal = "";
    String nameHostelero = "";
    String passHostelero = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(R.string.login_text);

        SharedPreferences prefs = getSharedPreferences("UsuarioIdComensal", Context.MODE_PRIVATE);
        String ComensalUsuarioID = prefs.getString("IDComensal","");
        SharedPreferences prefs2 = getSharedPreferences("UsuarioIdHostelero", Context.MODE_PRIVATE);
        String HosteleroUsuarioID = prefs2.getString("IDHostelero","");

/*
       if(ComensalUsuarioID.length()>0){
           Toast.makeText(getApplicationContext(), "Bienvenido de nuevo comensal con ID: "+ComensalUsuarioID, Toast.LENGTH_SHORT).show(); //Muestro un mensaje al usuario, para avisarle de que aun sigue autenticado.
           Intent intro = new Intent(this, PerfilComensal.class);
            startActivity(intro);
       }
*/

        if(HosteleroUsuarioID.length()>0){
        Toast.makeText(getApplicationContext(), "Bienvenido de nuevo hostelero con ID: "+HosteleroUsuarioID, Toast.LENGTH_SHORT).show(); //Muestro un mensaje al usuario, para avisarle de que aun sigue autenticado.
        Intent intro = new Intent(this, PerfilHostelero.class);
        startActivity(intro);
        }

        Button botonlogin1 = (Button) findViewById(R.id.login_boton_comensal);
        Button botonlogin2 = (Button) findViewById(R.id.login_boton_hostelero);
        Button botonlogin3 = (Button) findViewById(R.id.login_boton_creacuenta);

        botonlogin1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText loginname = (EditText) findViewById(R.id.login_name);
                EditText loginpass = (EditText) findViewById(R.id.login_pass);
                String name = loginname.getText().toString();
                String pass = loginpass.getText().toString();
                if(name.isEmpty() == false && pass.isEmpty() == false) {
                    AutenticacionComensal autenticaComensal = new AutenticacionComensal(name, pass);
                    ConexionComensal conecta = new ConexionComensal();
                    conecta.execute(autenticaComensal);
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario y/o contraseña vacios.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonlogin2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText loginname = (EditText) findViewById(R.id.login_name2);
                EditText loginpass = (EditText) findViewById(R.id.login_pass2);
                String name2 = loginname.getText().toString();
                String pass2 = loginpass.getText().toString();
                if(name2.isEmpty() == false && pass2.isEmpty() == false) {
                    AutenticacionHostelero autenticaHostelero = new AutenticacionHostelero(name2, pass2);
                    ConexionHostelero conecta2 = new ConexionHostelero();
                    conecta2.execute(autenticaHostelero);
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario y/o contraseña vacios.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botonlogin3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Creando Cuenta.", Toast.LENGTH_SHORT).show();
                Intent a3 = new Intent(MainActivity.this, CrearCuenta1.class);
                startActivity(a3);
            }
        });
    }

    public class ConexionComensal extends AsyncTask<AutenticacionComensal, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(MainActivity.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Autenticando, espere por favor.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(AutenticacionComensal... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String Name = arg0[0].getName();
                String Pass = arg0[0].getPass();
                nameComensal = Name;
                passComensal = Pass;
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((RC+SP+Name+SP+Pass+CRLF).getBytes());
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
            if (respuesta != null) {
                if (respuesta.startsWith("true")) {
                    Toast.makeText(getApplicationContext(), "Autenticacion completada, bienvenido.", Toast.LENGTH_SHORT).show();
                    Intent a = new Intent(MainActivity.this, PerfilComensal.class);
                    a.putExtra("NombreComensal", nameComensal);
                    a.putExtra("PasswordComensal", passComensal);
                    startActivity(a);
                    nameComensal = "";
                    passComensal = "";
                }
                if(respuesta.startsWith("false")){
                    Toast.makeText(getApplicationContext(), "Clave y/o usuario incorrecto, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class ConexionHostelero extends AsyncTask<AutenticacionHostelero, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(MainActivity.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Autenticando, espere por favor.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(AutenticacionHostelero... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String Name = arg0[0].getName();
                String Pass = arg0[0].getPass();
                nameHostelero = Name;
                passHostelero = Pass;
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((RH+SP+Name+SP+Pass+CRLF).getBytes());
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
            pdia.dismiss();//Cierro el pdia.
            if (respuesta != null) {
                if (respuesta.startsWith("true")) {
                    Toast.makeText(getApplicationContext(), "Autenticacion completada, bienvenido.", Toast.LENGTH_SHORT).show();
                    Intent a2 = new Intent(MainActivity.this, PerfilHostelero.class);
                    startActivity(a2);
                    a2.putExtra("NombreHostelero", nameHostelero);
                    a2.putExtra("PasswordHostelero", passHostelero);
                    nameHostelero = "";
                    passHostelero = "";
                }
                if(respuesta.startsWith("false")){
                    Toast.makeText(getApplicationContext(), "Clave y/o usuario incorrecto, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}