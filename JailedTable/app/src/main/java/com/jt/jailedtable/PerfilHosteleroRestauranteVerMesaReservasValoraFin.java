package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class PerfilHosteleroRestauranteVerMesaReservasValoraFin extends AppCompatActivity implements ProtocoloData {

    String IDReserva;
    String IDRestaurante;
    String IDMesa;

    String ResultadoBuscarReservaIDUsuario;
    String ResultadoBuscarComensalPorIDUsuario;
    String ValorNegativoDelUsuario;
    String ValorPositivoDelUsuario;

    ArrayList<String> MostrarUsuarioInfo = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhosteleromirestmismesasvalorarfinal);
        this.setTitle(R.string.perfilhost_mirestaurante_infomesareservasvalorarfin_header1);
        Bundle bundle = getIntent().getExtras();

        final Button BotonValorarNegativamente = (Button) findViewById(R.id.perfil_host_mirestinfomesasreservasvalorafin_botonVNeg);
        final Button BotonValorarPositivamente = (Button) findViewById(R.id.perfil_host_mirestinfomesasreservasvalorafin_botonVPos);
        ListView ListaViewInfoComensalReserva = (ListView) findViewById(R.id.perfil_host_mirestinfomesasreservasvalorafin_lwinfo);

        if (bundle != null) {
            IDReserva = bundle.getString("NumReservaValorar");
            IDMesa = bundle.getString("NumMesaValorar");
            IDRestaurante = bundle.getString("NumRestValorar");
        }

        BuscarReserva Reservation = new BuscarReserva();
        ConexionBuscarComensal Comensal = new ConexionBuscarComensal();
        final ActualizarVNegativa UpdateVNeg = new ActualizarVNegativa();
        final ActualizarVPositiva UpdateVPos = new ActualizarVPositiva();
        final BorraReserva DeleteReserv = new BorraReserva();

        try {
            ResultadoBuscarReservaIDUsuario = Reservation.execute(IDRestaurante,IDMesa,IDReserva).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(ResultadoBuscarReservaIDUsuario.isEmpty()==false){
            try {
                ResultadoBuscarComensalPorIDUsuario = Comensal.execute(ResultadoBuscarReservaIDUsuario).get();
                String [] partir = ResultadoBuscarComensalPorIDUsuario.split(" ");
                if(partir.length==11){
                    String usuarioid = partir[0];
                    String nombre    = partir[1];
                    String apellidos = partir[2];
                    String numtlf    = partir[3];
                    String contacto  = partir[4];
                    ValorPositivoDelUsuario = partir[5];
                    ValorNegativoDelUsuario = partir[6];
                    String comunidad = partir[7];
                    String provincia = partir[8];
                    String localidad = partir[9];
                    String password  = partir[10];
                    String PerfilMostrar =  "Nombre: "+nombre+"\n"+"Primer Apellido: "+apellidos+"\n"+"Núm de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto+"\n"+"Localización: "+comunidad+" "+provincia+" "+localidad+"\n"+"Número de la Reserva: "+IDReserva;
                    MostrarUsuarioInfo.add(PerfilMostrar);
                } if (partir.length==12){
                    String usuarioid = partir[0];
                    String nombre1   = partir[1];
                    String nombre2   = partir[2];
                    String apellidos = partir[3];
                    String numtlf    = partir[4];
                    String contacto  = partir[5];
                    ValorPositivoDelUsuario  = partir[6];
                    ValorNegativoDelUsuario  = partir[7];
                    String comunidad = partir[8];
                    String provincia = partir[9];
                    String localidad = partir[10];
                    String password  = partir[11];
                    String PerfilMostrar =  "Nombre: "+nombre1+" "+nombre2+"\n"+"Primer Apellido: "+apellidos+"\n"+"Núm de Teléfono: "+numtlf+"\n"+"Contacto: "+contacto+"\n"+"Localización: "+comunidad+" "+provincia+" "+localidad+"\n"+"Número de la Reserva: "+IDReserva;;
                    MostrarUsuarioInfo.add(PerfilMostrar);
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
            String PerfilMostrar =  "No puede valorar la reserva número "+IDReserva;
            MostrarUsuarioInfo.add(PerfilMostrar);
            BotonValorarNegativamente.setEnabled(false);
            BotonValorarPositivamente.setEnabled(false);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, MostrarUsuarioInfo);
        ListaViewInfoComensalReserva.setAdapter(adapter);

        BotonValorarNegativamente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                int VNegTemp = Integer.parseInt(ValorNegativoDelUsuario)+1;
                String VNegActualizadoEnviar = Integer.toString(VNegTemp);
                UpdateVNeg.execute(VNegActualizadoEnviar,ResultadoBuscarReservaIDUsuario);
                DeleteReserv.execute(IDReserva);
                BotonValorarNegativamente.setEnabled(false);
                BotonValorarPositivamente.setEnabled(false);
            }
        });

        BotonValorarPositivamente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                int VPosTemp = Integer.parseInt(ValorPositivoDelUsuario)+1;
                String VPosActualizadoEnviar = Integer.toString(VPosTemp);
                UpdateVPos.execute(VPosActualizadoEnviar,ResultadoBuscarReservaIDUsuario);
                DeleteReserv.execute(IDReserva);
                BotonValorarPositivamente.setEnabled(false);
                BotonValorarNegativamente.setEnabled(false);
            }
        });
    }

    public class BuscarReserva extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservasValoraFin.this);
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
                String IDRest = arg0[0];
                String IDMes = arg0[1];
                String IDResv = arg0[2];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BRESERVPIDS+SP+IDRest+SP+IDMes+SP+IDResv+CRLF).getBytes());
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

    public class ConexionBuscarComensal extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservasValoraFin.this);
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
                String UsuarioComensalID = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BC+SP+UsuarioComensalID+CRLF).getBytes());
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

    public class ActualizarVPositiva extends AsyncTask<String, Void, Void> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservasValoraFin.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected Void doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String VPositiva = arg0[0];
                String IDusuario = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((AVPCOM+SP+VPositiva+SP+IDusuario+CRLF).getBytes());
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
            return null;
        }

        @Override
        protected void onPostExecute(Void respuesta) {
            super.onPostExecute(respuesta);
            pdia.dismiss();
        }
    }

    public class ActualizarVNegativa extends AsyncTask<String, Void, Void> {
        ProgressDialog pdia = null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservasValoraFin.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected Void doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String VNegativa = arg0[0];
                String IDusuario = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((AVNCOM+SP+VNegativa+SP+IDusuario+CRLF).getBytes());
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
            return null;
        }

        @Override
        protected void onPostExecute(Void respuesta) {
            super.onPostExecute(respuesta);
            pdia.dismiss();
        }
    }

    public class BorraReserva extends AsyncTask<String, Void, Void> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservasValoraFin.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected Void doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String IDReserva = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((DELRESERV+SP+IDReserva+CRLF).getBytes());
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
            return null;
        }

        @Override
        protected void onPostExecute(Void respuesta) {
            super.onPostExecute(respuesta);
            pdia.dismiss();
        }
    }
}
