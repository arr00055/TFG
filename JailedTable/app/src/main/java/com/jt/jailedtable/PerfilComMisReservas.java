package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PerfilComMisReservas extends AppCompatActivity implements ProtocoloData {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilcommisreservas);
        this.setTitle(R.string.perfilcomensal_reservas_cabecera);
        SharedPreferences prefs = getSharedPreferences("UsuarioIdComensal", Context.MODE_PRIVATE);
        String UserIDComensal2 = prefs.getString("IDComensal","");

        String restaurante = "";
        String mesa = "";
        String ReservaMostrar = "";
        ListView listview;
        ArrayList<String> salida = null;
        listview = (ListView) findViewById(R.id.perfil_com_reservas_list);
        ArrayList<String> MensjMostrar = new ArrayList<String>();

        ConexionBuscarReservas buscarresev = new ConexionBuscarReservas();
        try {
            salida = buscarresev.execute(UserIDComensal2).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(salida.isEmpty()==false){
            for (int x=0; x<salida.size(); x++) {
                String salida2 = salida.get(x);
                String[] result2 = salida2.split(" ");
                String campo1 = result2[0];
                String datereserv = result2[1];
                String campo3 = result2[2];
                String campo4 = result2[3];
                BuscarRestaurante buscarrest = new BuscarRestaurante();
                BuscarMesa buscamesa = new BuscarMesa();
                try {
                    restaurante = buscarrest.execute(campo4).get();
                    mesa = buscamesa.execute(campo3).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String[] result3 = restaurante.split(" ");
                String camp1 = result3[0];
                String nombrerest = result3[1];
                String camp3 = result3[2];
                String camp4 = result3[3];
                String camp5 = result3[4];
                String numtlfrest = result3[5];

                String[] result4 = mesa.split(" ");
                String ca1 = result4[0];
                String costenoshow = result4[1];
                String nummesa = result4[2];
                String numsillas = result4[3];

                ReservaMostrar = "Fecha de la Reserva: "+datereserv+"\n"+"Nombre del Restaurante: "+nombrerest+"\n"+"Número de Teléfono: "+numtlfrest+"\n"+"Número de mesa: "+nummesa+"\n"+"Número de sillas: "+numsillas+"\n"+"Coste Asociado al No Show: "+costenoshow;

                MensjMostrar.add(x,ReservaMostrar);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, MensjMostrar);
                listview.setAdapter(adapter);

            }
        }

    }

    public class ConexionBuscarReservas extends AsyncTask<String, Void, ArrayList <String>> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComMisReservas.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected ArrayList <String> doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            ArrayList<String> mensajes = new ArrayList<String>();
            try {
                String UsuarioComensalID = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BRESVC+SP+UsuarioComensalID+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                bis.close();
                os.close();
                cliente.close();
                String[] result = respuesta.split("&");
                if (result.length==1 && respuesta.isEmpty()==false){
                    String reserva = respuesta;
                    reserva = respuesta.substring(0,respuesta.indexOf("&"));
                    mensajes.add(reserva);
                } if (result.length>1) {
                    for (int x=0; x<result.length; x++) {
                        mensajes.add(result[x]);
                    }
                }   if(result.length==1 && respuesta.isEmpty()==true) {
                }

            } catch (IOException err){
                err.printStackTrace();
                respuesta = "IOException: " + err.toString();
            }
            return mensajes;
        }

        @Override
        protected void onPostExecute(ArrayList <String> reserv) {
            super.onPostExecute(reserv);
            pdia.dismiss();
        }
    }

    public class BuscarRestaurante extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComMisReservas.this);
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
                os.write((BREST+SP+IdRest+CRLF).getBytes());
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

    public class BuscarMesa extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComMisReservas.this);
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
                String Idmesa = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BMESA+SP+Idmesa+CRLF).getBytes());
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
