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

public class PerfilHosteleroRestauranteVerMenuCreaPlatos extends AppCompatActivity implements ProtocoloData {

    String IDdelMenuParaCrearPlatos;
    String IDdelPlatoGenerado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestmismenuscreaplato);
        this.setTitle(R.string.perfilhost_mirestaurante_mimenu_crearplato_textocabeceraplato);
        Bundle bundle = getIntent().getExtras();

        final InsertarPlato   InsertarPlato                  = new InsertarPlato();
        final InsertarCompone InsertandoComponeElPlato       = new InsertarCompone();

        final Button BotonCreaPlato  = (Button) findViewById(R.id.perfil_host_mirest_vermenu_creaplato_botonplatocrea);

        if (bundle != null) {
            IDdelMenuParaCrearPlatos = bundle.getString("IdMenuHostCreaPlato");
        }

        BotonCreaPlato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText nombredelplato = (EditText) findViewById(R.id.perfil_host_mirest_vermenu_creaplato_etnameplato);
                EditText costedelplato  = (EditText) findViewById(R.id.perfil_host_mirest_vermenu_creaplato_etcosteplato);
                EditText descripciplato = (EditText) findViewById(R.id.perfil_host_mirest_vermenu_creaplato_etdescripplato);
                String NamePlato    = nombredelplato.getText().toString();
                String CostePlato   = costedelplato.getText().toString();
                String DescripPlato = descripciplato.getText().toString();
                String PlatoEnviar = DescripPlato+"-"+CostePlato+"-"+NamePlato;
                if(NamePlato.isEmpty()==false && CostePlato.isEmpty()==false && DescripPlato.isEmpty()==false){
                    double CosteDelPlato = Double.parseDouble(CostePlato);
                    if(CosteDelPlato <= 999.99){
                        if(DescripPlato.length()<150){
                            try {
                                IDdelPlatoGenerado = InsertarPlato.execute(PlatoEnviar).get();
                                if(IDdelPlatoGenerado.isEmpty()==false){
                                    InsertandoComponeElPlato.execute(IDdelMenuParaCrearPlatos,IDdelPlatoGenerado);
                                    Toast.makeText(getApplicationContext(), "Su Plato ha sido creado.", Toast.LENGTH_SHORT).show();
                                    BotonCreaPlato.setEnabled(false);
                                }else{
                                    Toast.makeText(getApplicationContext(), "No se ha podido crear su Plato, inténtelo de nuevo.", Toast.LENGTH_SHORT).show();
                                    BotonCreaPlato.setEnabled(false);
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "El campo Descripción del Plato es demasiado grande.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "El campo Coste del Plato es demasiado grande, valor máximo: 999.99.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Hay campos vacíos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class InsertarPlato extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuCreaPlatos.this);
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
                String plato = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IP+SP+plato+CRLF).getBytes());
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
                Toast.makeText(getApplicationContext(), "Se ha creado su plato, con ID "+respuesta, Toast.LENGTH_SHORT).show();
            }else{
                respuesta = "";
            }
        }
    }

    public class InsertarCompone extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuCreaPlatos.this);
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
                String idplato = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((ICOMP+SP+idmenu+SP+idplato+CRLF).getBytes());
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
