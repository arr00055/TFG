package com.jt.jailedtable;

import android.app.ProgressDialog;
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
import java.util.concurrent.ExecutionException;

public class PerfilHosteleroRestauranteCreaMenu extends AppCompatActivity implements ProtocoloData {
    String IDdelRestaurante;
    String IDdelMenuGenerado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestcrearmenu);
        Bundle bundle = getIntent().getExtras();
        this.setTitle(R.string.perfilhost_mirestaurante_creandomenu_cabecera1);

        final InsertarMenu CreandoMenu                    = new InsertarMenu ();
        final InsertarEstablece InsertandoEstableceElMenu = new InsertarEstablece();

        final Button CrearMenu  = (Button) findViewById(R.id.perfil_host_mirest_crearmenu_boton1);

        if (bundle != null) {
            IDdelRestaurante = bundle.getString("IdRestHostaCreaMenu");
        }

        CrearMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText nombredelmenu = (EditText) findViewById(R.id.perfil_host_mirest_crearmenu_nombremenu);
                String NameMenu = nombredelmenu.getText().toString();
                if(NameMenu.length()<100){
                    if(NameMenu.isEmpty()==false){
                        try {
                            IDdelMenuGenerado = CreandoMenu.execute(NameMenu).get();
                            if(IDdelMenuGenerado.isEmpty()==false){
                                InsertandoEstableceElMenu.execute(IDdelMenuGenerado,IDdelRestaurante);
                                Toast.makeText(getApplicationContext(), "Su Menú ha sido creado.", Toast.LENGTH_SHORT).show();
                                CrearMenu.setEnabled(false);
                            }else{
                                Toast.makeText(getApplicationContext(), "No se ha podido crear su Menú, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "El campo Nombre del Menú se encuentra vacío.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "El campo Nombre del Menú es demasiado largo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class InsertarMenu extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteCreaMenu.this);
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
                String menu = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IM+SP+menu+CRLF).getBytes());
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
            if (respuesta != null && respuesta.equalsIgnoreCase(" 0")==false) {
                Toast.makeText(getApplicationContext(), "Se ha creado su menú, con ID "+respuesta, Toast.LENGTH_SHORT).show();
            }else{
                respuesta = "";
            }
        }
    }

    public class InsertarEstablece extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteCreaMenu.this);
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
                String idmenu = arg0[0];
                String idrest = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IEST+SP+idmenu+SP+idrest+CRLF).getBytes());
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
