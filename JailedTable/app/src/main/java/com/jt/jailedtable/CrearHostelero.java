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

public class CrearHostelero extends AppCompatActivity implements ProtocoloData {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearhostelero);
        this.setTitle(R.string.crearhostelero_txt);

        Button botoncrearhostelero = (Button) findViewById(R.id.crearhostelero_boton);

        botoncrearhostelero.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText hosteleronombre = (EditText) findViewById(R.id.crearhostelero_nombre);
                EditText hosteleroapellidos = (EditText) findViewById(R.id.crearhostelero_apellidos);
                EditText hosteleronumtlf = (EditText) findViewById(R.id.crearhostelero_numtlf);
                EditText hostelerocontacto = (EditText) findViewById(R.id.crearhostelero_contacto);
                EditText hosteleropassword = (EditText) findViewById(R.id.crearhostelero_password);
                String nombre = hosteleronombre.getText().toString();
                String apellidos = hosteleroapellidos.getText().toString();
                String numerotlf = hosteleronumtlf.getText().toString();
                String contacto = hostelerocontacto.getText().toString();
                String password = hosteleropassword.getText().toString();
                if(nombre.isEmpty() == false && apellidos.isEmpty() == false && numerotlf.isEmpty() == false && contacto.isEmpty() == false && password.isEmpty() == false) {
                    crearusuariohostelero creandohostelero = new crearusuariohostelero(nombre,apellidos,numerotlf,contacto,password);
                    ConexionCrearHostelero conex = new ConexionCrearHostelero();
                    conex.execute(creandohostelero);
                } else {
                    Toast.makeText(getApplicationContext(), "Hay campos vacíos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class ConexionCrearHostelero extends AsyncTask<crearusuariohostelero, String, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(CrearHostelero.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Creando Hostelero.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(crearusuariohostelero... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String Name = arg0[0].getNombre();
                String Apellido = arg0[0].getApellidos();
                String NumTlf = arg0[0].getNumTlf();
                String Contacto = arg0[0].getContacto();
                String Password = arg0[0].getPassword();
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                String hostelero = Name+"-"+Apellido+"-"+NumTlf+"-"+Contacto+"-"+Password;
                os.write((IH+SP+hostelero+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Hosteler", respuesta);
                bis.close();
                os.close();
                cliente.close();
                SharedPreferences prefs = getSharedPreferences("UsuarioIdHostelero", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("IDHostelero", respuesta);
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
            pdia.dismiss();//Cierro el pdia.
            if (respuesta != null) {
                Toast.makeText(getApplicationContext(), "Su perfil de hostelero ha sido creado.", Toast.LENGTH_SHORT).show();
                Intent a = new Intent(CrearHostelero.this, PerfilHostelero.class);
                startActivity(a);
            }
        }
    }
}
