package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class PerfilHosteleroRestaurantePoliticas extends AppCompatActivity implements ProtocoloData {
    String UserIDHostelero;
    String salidaBuscarHabilita;
    String salidaBuscarPoliticas;
    String IDdelRestaurante;
    CheckBox CreacioncbPoliticasSiNo;
    CheckBox CreacioncbUsuariosNegSiNo;
    CheckBox CambiarcbPoliticasSiNo;
    CheckBox CambiarcbUsuariosNegSiNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestpoliticas);
        this.setTitle(R.string.perfilhost_mirestaurante_politicas_cabecera1);
        Bundle bundle = getIntent().getExtras();
        SharedPreferences prefs = getSharedPreferences("UsuarioIdHostelero", Context.MODE_PRIVATE);
        UserIDHostelero = prefs.getString("IDHostelero", "");

        if (bundle != null) {
            IDdelRestaurante = bundle.getString("IdRestHostaPoliticas");
        }

        BuscarHabilitaPorIdRest       BuscandoEnHabilita    = new BuscarHabilitaPorIdRest();
        BuscarPoliticas               BuscandoPoliticas     = new BuscarPoliticas();
        final InsertarPolitica        InsertPolitica        = new InsertarPolitica();
        final InsertarHabilita        InsertHabilita        = new InsertarHabilita();
        final ActualizarLasPoliticas  ActualizaPolitica     = new ActualizarLasPoliticas();

        CreacioncbPoliticasSiNo = (CheckBox) findViewById(R.id.perfil_host_mirest_politic_cb1);
        CreacioncbUsuariosNegSiNo = (CheckBox) findViewById(R.id.perfil_host_mirest_politic_cb2);

        CambiarcbPoliticasSiNo = (CheckBox) findViewById(R.id.perfil_host_mirest_politic_cb3);
        CambiarcbUsuariosNegSiNo = (CheckBox) findViewById(R.id.perfil_host_mirest_politic_cb4);

        final Button CrearPolitica      = (Button) findViewById(R.id.perfil_host_mirest_politic_enviar1);
        final Button ActuaPolitica      = (Button) findViewById(R.id.perfil_host_mirest_politic_enviar2);

        try {
            salidaBuscarHabilita  = BuscandoEnHabilita.execute(IDdelRestaurante).get();
            if(salidaBuscarHabilita.isEmpty()==false){
                CrearPolitica.setEnabled(false);
                CreacioncbPoliticasSiNo.setEnabled(false);
                CreacioncbUsuariosNegSiNo.setEnabled(false);
                CambiarcbPoliticasSiNo.setEnabled(true);
                CambiarcbUsuariosNegSiNo.setEnabled(true);
                salidaBuscarPoliticas = BuscandoPoliticas.execute(salidaBuscarHabilita).get();
                String partes [] = salidaBuscarPoliticas.split(" ");
                String PoliticasSiNo   = partes[0];
                String UsuariosNegSiNo = partes[1];

                if(PoliticasSiNo.equals("true") && UsuariosNegSiNo.equals("true")) {
                    CambiarcbPoliticasSiNo.setChecked(true);
                    CambiarcbUsuariosNegSiNo.setChecked(true);
                }
                if(PoliticasSiNo.equals("true") && UsuariosNegSiNo.equals("false")){
                    CambiarcbPoliticasSiNo.setChecked(true);
                    CambiarcbUsuariosNegSiNo.setChecked(false);
                }
                if(PoliticasSiNo.equals("false") && UsuariosNegSiNo.equals("true")){
                    CambiarcbPoliticasSiNo.setChecked(false);
                    CambiarcbUsuariosNegSiNo.setChecked(true);
                }
                if(PoliticasSiNo.equals("false") && UsuariosNegSiNo.equals("false")){
                    CambiarcbPoliticasSiNo.setChecked(false);
                    CambiarcbUsuariosNegSiNo.setChecked(false);
                }

            }else{
                ActuaPolitica.setEnabled(false);
                CreacioncbPoliticasSiNo.setEnabled(true);
                CreacioncbUsuariosNegSiNo.setEnabled(true);
                CambiarcbPoliticasSiNo.setEnabled(false);
                CambiarcbUsuariosNegSiNo.setEnabled(false);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CrearPolitica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                String PolSiNo = "";
                String UsNegSiNo = "";
                Toast.makeText(getApplicationContext(), "Creando Políticas", Toast.LENGTH_SHORT).show();
                if(CreacioncbPoliticasSiNo.isChecked()==true && CreacioncbUsuariosNegSiNo.isChecked()==true){
                    PolSiNo   = "1";
                    UsNegSiNo = "1";
                }
                if(CreacioncbPoliticasSiNo.isChecked()==true && CreacioncbUsuariosNegSiNo.isChecked()==false){
                    PolSiNo   = "1";
                    UsNegSiNo = "0";
                }
                if(CreacioncbPoliticasSiNo.isChecked()==false && CreacioncbUsuariosNegSiNo.isChecked()==true){
                    PolSiNo   = "0";
                    UsNegSiNo = "1";
                }
                if(CreacioncbPoliticasSiNo.isChecked()==false && CreacioncbUsuariosNegSiNo.isChecked()==false){
                    PolSiNo   = "0";
                    UsNegSiNo = "0";
                }
                String IDPoliticaGenerado = "";
                try {
                    IDPoliticaGenerado = InsertPolitica.execute(PolSiNo,UsNegSiNo).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                InsertHabilita.execute(IDPoliticaGenerado,IDdelRestaurante);
                CreacioncbPoliticasSiNo.setEnabled(false);
                CreacioncbUsuariosNegSiNo.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Vuelva a entrar en Políticas si necesita actualizarlas.", Toast.LENGTH_SHORT).show();
                CrearPolitica.setEnabled(false);
            }
        });

        ActuaPolitica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                String PolSiNo2 = "";
                String UsNegSiNo2 = "";
                if(CambiarcbPoliticasSiNo.isChecked()==true && CambiarcbUsuariosNegSiNo.isChecked()==true){
                    PolSiNo2   = "1";
                    UsNegSiNo2 = "1";
                }
                if(CambiarcbPoliticasSiNo.isChecked()==true && CambiarcbUsuariosNegSiNo.isChecked()==false){
                    PolSiNo2   = "1";
                    UsNegSiNo2 = "0";
                }
                if(CambiarcbPoliticasSiNo.isChecked()==false && CambiarcbUsuariosNegSiNo.isChecked()==true){
                    PolSiNo2   = "0";
                    UsNegSiNo2 = "1";
                }
                if(CambiarcbPoliticasSiNo.isChecked()==false && CambiarcbUsuariosNegSiNo.isChecked()==false){
                    PolSiNo2   = "0";
                    UsNegSiNo2 = "0";
                }
                ActualizaPolitica.execute(PolSiNo2,UsNegSiNo2,salidaBuscarHabilita);
                CambiarcbPoliticasSiNo.setEnabled(false);
                CambiarcbUsuariosNegSiNo.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Se han actualizado las políticas.", Toast.LENGTH_SHORT).show();
                ActuaPolitica.setEnabled(false);
            }
        });
    }

    public class BuscarHabilitaPorIdRest extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestaurantePoliticas.this);
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
                String IdRest = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BHABR+SP+IdRest+CRLF).getBytes());
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

    public class BuscarPoliticas extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestaurantePoliticas.this);
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
            int conteo = 0;
            String Politica = "";
            ArrayList<String> temp = new ArrayList<>();
            try {
                String IdPoliticas = arg0[0];
                String [] camps = IdPoliticas.split(" ");
                while(camps.length>conteo){
                    Politica = camps[conteo];
                    if(Politica.isEmpty()==false){
                        InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                        cliente = new Socket();
                        cliente.connect(direccion);
                        BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        OutputStream os = cliente.getOutputStream();
                        respuesta = bis.readLine();
                        Log.d("Saludo", respuesta);
                        os.write((BPOL+SP+Politica+CRLF).getBytes());
                        os.flush();
                        respuesta = bis.readLine();
                        Log.d("Respuesta Serv Comensal", respuesta);
                        bis.close();
                        os.close();
                        cliente.close();
                        temp.add(conteo,respuesta);
                    }
                    conteo++;
                }
                respuesta = "";
                for(int h = 0; conteo > h; h++){
                    respuesta += temp.get(h)+" ";
                }

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

    public class InsertarPolitica extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestaurantePoliticas.this);
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
                String politicasino = arg0[0];
                String usuariosino  = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IPOL+SP+politicasino+"-"+usuariosino+CRLF).getBytes());
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
                Toast.makeText(getApplicationContext(), "Se han creado sus políticas, con ID "+respuesta, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class InsertarHabilita extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestaurantePoliticas.this);
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
                String idpolitica = arg0[0];
                String idrest     = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IHAB+SP+idpolitica+SP+idrest+CRLF).getBytes());
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

    public class ActualizarLasPoliticas extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestaurantePoliticas.this);
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
                String politicasino = arg0[0];
                String usuariosino  = arg0[1];
                String idpoliticas  = arg0[2];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((ACTP+SP+politicasino+SP+usuariosino+SP+idpoliticas+CRLF).getBytes());
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
