package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Intent;
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

public class PerfilHosteleroRestauranteVerMesaReservas extends AppCompatActivity implements ProtocoloData {

    String NumMesaBuscada;
    String IDRestaurante;
    String MesaBuscada;
    String MesaMostrar;
    String ReservaMostrar;
    String IDENTMesa;

    ArrayList<String> MesaToShow = new ArrayList<String>();
    ArrayList<String> salida = new ArrayList<String>();
    ArrayList<String> ReservaToShow = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestmismesasreservas);
        this.setTitle(R.string.perfilhost_mirestaurante_mismesas_reservas_titulo);
        Bundle bundle = getIntent().getExtras();

        Button BotonaValorar = (Button) findViewById(R.id.perfil_host_mirestinfomesasreservas_botonValorar);
        ListView ListaViewDeLaMesa = (ListView) findViewById(R.id.perfil_host_mirestinfomesasreservas_listviewLaMesa);
        ListView ListaViewReservas = (ListView) findViewById(R.id.perfil_host_mirestinfomesasreservas_listviewReservasMesa);

        if (bundle != null) {
            NumMesaBuscada = bundle.getString("NumMesaDelRestauanteHost");
            IDRestaurante = bundle.getString("IDRestDelRestauanteHost");
        }

        BuscarLaMesa LaMesa = new BuscarLaMesa();
        BuscarIDMesa IDLaMesa = new BuscarIDMesa();
        BuscarlasReservas BuscandoReservas = new BuscarlasReservas();

        try {
            MesaBuscada = LaMesa.execute(NumMesaBuscada,IDRestaurante).get();
            IDENTMesa = IDLaMesa.execute(NumMesaBuscada,IDRestaurante).get();
            salida = BuscandoReservas.execute(IDRestaurante,IDENTMesa).get();
            if(MesaBuscada.isEmpty()==false){
                String[] camposMesa = MesaBuscada.split(" ");
                String CosteNoShow  = camposMesa[0];
                String NumMesa      = camposMesa[1];
                String NumeroSillas = camposMesa[2];

                MesaMostrar = "Número de Mesa: "+NumMesa+"\n"+"Número de Sillas: "+NumeroSillas+"\n"+"Coste por No Show: "+CosteNoShow;

                MesaToShow.add(MesaMostrar);
            }else{
                MesaMostrar = "No existe la mesa buscada para este restaurante.";
                MesaToShow.add(MesaMostrar);
            }

            if(salida.isEmpty()==false){
                for (int x=0; x<salida.size(); x++) {
                    String salida2 = salida.get(x);
                    String[] result2 = salida2.split(" ");
                    String ID_Reserva = result2[0];
                    String ID_Usuario = result2[1];
                    String Fecha_Reserva = result2[2];
                    ReservaMostrar = "Número de Reserva: "+ID_Reserva+"\n"+"Número de Comensal: "+ID_Usuario+"\n"+"Fecha de la Reserva: "+Fecha_Reserva;
                    ReservaToShow.add(x,ReservaMostrar);
                }
            }else{
                ReservaMostrar = "No hay reservas para este mesa.";
                ReservaToShow.add(ReservaMostrar);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, MesaToShow);
        ListaViewDeLaMesa.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, ReservaToShow);
        ListaViewReservas.setAdapter(adapter2);

        BotonaValorar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Valoración.", Toast.LENGTH_SHORT).show();
                Intent IValora= new Intent(PerfilHosteleroRestauranteVerMesaReservas.this, PerfilHosteleroRestauranteVerMesaReservasValora.class);
                IValora.putExtra("IDdelaMesa", IDENTMesa);
                IValora.putExtra("IDdelRestauranteValora", IDRestaurante);
                startActivity(IValora);
            }
        });
    }

    public class BuscarLaMesa extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservas.this);
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

    public class BuscarIDMesa extends AsyncTask<String, Void, String> {
    ProgressDialog pdia = null;

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservas.this);
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
            String NumeroMesa = arg0[0];
            String IdRest = arg0[1];
            InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
            cliente = new Socket();
            cliente.connect(direccion);
            BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            OutputStream os = cliente.getOutputStream();
            respuesta = bis.readLine();
            Log.d("Saludo", respuesta);
            os.write((BMESNMIR+SP+NumeroMesa+SP+IdRest+CRLF).getBytes());
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

    public class BuscarlasReservas extends AsyncTask<String, Void, ArrayList <String>> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservas.this);
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
                String IDRest = arg0[0];
                String IDMesa = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BRESVIRIM+SP+IDRest+SP+IDMesa+CRLF).getBytes());
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
                    //String vacio = "";
                    //mensajes.add(vacio);
                    System.out.println("No hay reservas");
                }

            } catch (IOException err){
                err.printStackTrace();
                respuesta = "IOException: " + err.toString();
            }//Fin del catch.
            return mensajes;
        }

        @Override
        protected void onPostExecute(ArrayList <String> reserv) {
            super.onPostExecute(reserv);
            pdia.dismiss();
        }
    }
}
