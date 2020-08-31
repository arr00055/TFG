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

public class PerfilHosteleroRestauranteCreaMesa extends AppCompatActivity implements ProtocoloData {
    String IDdelRestaurante;
    Boolean NumMesaResultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestcrearmesa);
        this.setTitle(R.string.perfilhost_mirestaurante_creandomesa_cabecera1);
        Bundle bundle = getIntent().getExtras();

        final InsertaMesa CreandoMesa  = new InsertaMesa();
        final BuscarExisteMesa BusquedaDeMesa = new BuscarExisteMesa();

        final Button CrearMesa  = (Button) findViewById(R.id.perfil_host_mirest_crearmesa_botoncrea);

        if (bundle != null) {
            IDdelRestaurante = bundle.getString("IdRestHostaCreaMesa");
        }

        CrearMesa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText costenoshow = (EditText) findViewById(R.id.perfil_host_mirest_crearmesa_etcostenoshow);
                EditText numdelamesa = (EditText) findViewById(R.id.perfil_host_mirest_crearmesa_etnummesa);
                EditText numdesillas = (EditText) findViewById(R.id.perfil_host_mirest_crearmesa_etnumsillas);
                String CosteAsociadoNoShow = costenoshow.getText().toString();
                String NumeroMesa          = numdelamesa.getText().toString();
                String NumeroSillas        = numdesillas.getText().toString();
                String MesaEnviar = IDdelRestaurante+"/"+CosteAsociadoNoShow+"-"+NumeroMesa+"-"+NumeroSillas;
                if(CosteAsociadoNoShow.isEmpty()==false && NumeroMesa.isEmpty()==false && NumeroSillas.isEmpty()==false){
                    double CosteDelNoShow = Double.parseDouble(CosteAsociadoNoShow);
                    if(CosteDelNoShow <= 999.99){
                        try {
                            NumMesaResultado = BusquedaDeMesa.execute(NumeroMesa,IDdelRestaurante).get();
                            CrearMesa.setEnabled(false);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(NumMesaResultado==false){
                            CreandoMesa.execute(MesaEnviar);
                            CrearMesa.setEnabled(false);
                        }else{
                            Toast.makeText(getApplicationContext(), "La mesa con número "+NumeroMesa+" ya existe, inserte otro número de mesa.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "El campo Coste por No Show es demasiado grande, valor máximo: 999.99.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Hay campos vacíos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class InsertaMesa extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteCreaMesa.this);
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
                String mesa = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IMESA+SP+mesa+CRLF).getBytes());
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
                Toast.makeText(getApplicationContext(), "Se ha creado su mesa, con ID "+respuesta, Toast.LENGTH_SHORT).show();
            }else{
                respuesta = "";
            }
        }
    }

    public class BuscarExisteMesa extends AsyncTask<String, Void, Boolean> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteCreaMesa.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            Boolean existe = null;
            try {
                String NumeroMesa = arg0[0];
                String IdRest = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BMESNR+SP+NumeroMesa+SP+IdRest+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                if (respuesta.isEmpty()==true){
                    existe = false;
                }else{
                    existe = true;
                }
                bis.close();
                os.close();
                cliente.close();
            } catch (IOException err){
                err.printStackTrace();
                respuesta = "IOException: " + err.toString();
            }
            return existe;
        }

        @Override
        protected void onPostExecute(Boolean existe) {
            super.onPostExecute(existe);
            pdia.dismiss();
        }
    }
}
