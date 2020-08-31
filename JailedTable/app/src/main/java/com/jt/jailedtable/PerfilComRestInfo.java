package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
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

public class PerfilComRestInfo extends AppCompatActivity implements ProtocoloData {

    String UserIDComensal;
    String IDRestBusq;
    String salidaBuscarRest;
    String RestauranteMostrar;
    ArrayList<String> salidaBuscarMesa = new ArrayList<String>();
    ArrayList<String> MesasMostrando = new ArrayList<String>();
    String salidaBuscarHabilita;
    String salidaBuscarPoliticas;
    Integer ValoracionNegativaComensal;
    String MesaMostrar;
    String IDMesaBuscado;
    CalendarView Calendario;
    String fecha = "vacio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilcomrestinfo);
        this.setTitle(R.string.perfilcom_restinformacion_cabeceratitle);
        Bundle bundle = getIntent().getExtras();
        SharedPreferences prefs = getSharedPreferences("UsuarioIdComensal", Context.MODE_PRIVATE);
        UserIDComensal = prefs.getString("IDComensal","");

        ListView listview;
        Calendario = (CalendarView)findViewById(R.id.perfil_com_restinfo_calendario);
        listview = (ListView) findViewById(R.id.perfil_com_restinfo_listviewMesa);
        final Button BotonReserva = (Button) findViewById(R.id.perfil_com_restinfo_botonreservar);
        ImageButton BotonMenus = (ImageButton) findViewById(R.id.perfil_com_restinfo_botonmenus);

        if (bundle != null){
             IDRestBusq = bundle.getString("idrestaurantebusq");
        }

        BuscarRestaurante       BuscarRest        = new BuscarRestaurante();
        BuscarMesas             BuscarTable       = new BuscarMesas();
        BuscarHabilitaPorIdRest BuscarHabilita    = new BuscarHabilitaPorIdRest();
        BuscarPoliticas         BuscarPolitic     = new BuscarPoliticas();
        BuscarValoracionNegComensal BuscarVNeg    = new BuscarValoracionNegComensal();
        final ConexionCrearReserva    InsertarReserva   = new ConexionCrearReserva();
        final BuscarMesaSaberIDRest   ObtenerIDMesa     = new BuscarMesaSaberIDRest();

        try {
            salidaBuscarRest  = BuscarRest.execute(IDRestBusq).get();
            salidaBuscarMesa  = BuscarTable.execute(IDRestBusq).get();

            salidaBuscarHabilita  = BuscarHabilita.execute(IDRestBusq).get();
            if(salidaBuscarHabilita.isEmpty()==false){
                salidaBuscarPoliticas = BuscarPolitic.execute(salidaBuscarHabilita).get();
            }
            ValoracionNegativaComensal = BuscarVNeg.execute(UserIDComensal).get();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(salidaBuscarRest.isEmpty()==false){
            String[] resultadoSalidaBuscarRest = salidaBuscarRest.split(" ");
                if (resultadoSalidaBuscarRest.length>6){
                    String IDUsuarioHostelero  = resultadoSalidaBuscarRest[0];
                    String NombreRest1         = resultadoSalidaBuscarRest[1];
                    String NombreRest2         = resultadoSalidaBuscarRest[2];
                    String ComunidadRest       = resultadoSalidaBuscarRest[3];
                    String ProvinciaRest       = resultadoSalidaBuscarRest[4];
                    String LocalidadRest       = resultadoSalidaBuscarRest[5];
                    String NumTlfRest          = resultadoSalidaBuscarRest[6];

                    RestauranteMostrar = "Información del Restaurante: "+NombreRest1+" "+NombreRest2+"\n"+"Número de Teléfono: "+NumTlfRest;
                    TextView InfoRestSolo = (TextView) findViewById(R.id.perfil_com_restinfo_text1);
                    InfoRestSolo.setText(RestauranteMostrar);
                }else{
                    String IDUsuarioHostelero = resultadoSalidaBuscarRest[0];
                    String NombreRest         = resultadoSalidaBuscarRest[1];
                    String ComunidadRest      = resultadoSalidaBuscarRest[2];
                    String ProvinciaRest      = resultadoSalidaBuscarRest[3];
                    String LocalidadRest      = resultadoSalidaBuscarRest[4];
                    String NumTlfRest         = resultadoSalidaBuscarRest[5];

                    RestauranteMostrar = "Información del Restaurante: "+NombreRest+"\n"+"Número de Teléfono: "+NumTlfRest;
                    TextView InfoRestSolo = (TextView) findViewById(R.id.perfil_com_restinfo_text1);
                    InfoRestSolo.setText(RestauranteMostrar);
                }
        }

        if(salidaBuscarMesa.isEmpty()==false){
            for (int x=0; x<salidaBuscarMesa.size(); x++) {
                String salidaBuscarMesa2 = salidaBuscarMesa.get(x);
                String[] camposMesa = salidaBuscarMesa2.split(" ");
                String IDMesa       = camposMesa[0];
                String CosteNoShow  = camposMesa[1];
                String NumMesa      = camposMesa[2];
                String NumeroSillas = camposMesa[3];

                MesaMostrar = "Número de Mesa: "+NumMesa+"\n"+"Número de Sillas: "+NumeroSillas+"\n"+"Coste Asociado al No Show: "+CosteNoShow;

                MesasMostrando.add(x,MesaMostrar);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, MesasMostrando);
                listview.setAdapter(adapter);
            }
        }

        Calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                fecha = year+"-"+(month + 1)+"-"+dayOfMonth;
                    Toast.makeText(getApplicationContext(), fecha, Toast.LENGTH_LONG).show();
            }
        });

        BotonReserva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                BuscarExisteMesa        ExistenciaMesa    = new BuscarExisteMesa();
                BuscarExisteReserva     ExistenciaReserva = new BuscarExisteReserva();
                Boolean ExisteMesa = null;
                Boolean ExisteReserva = null;
                EditText numeromesa = (EditText) findViewById(R.id.perfil_com_restinfo_nummesareservar);
                String NumMesa = numeromesa.getText().toString();
                String ParaExisteMesa = NumMesa+" "+IDRestBusq;

                if(fecha.startsWith("vacio")==false){
                    if(NumMesa.isEmpty() == false) {
                        try {
                            ExisteMesa  = ExistenciaMesa.execute(ParaExisteMesa).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(ExisteMesa==true){
                            if(fecha.isEmpty()==false){
                                try {
                                    IDMesaBuscado = ObtenerIDMesa.execute(NumMesa,IDRestBusq).get();
                                    String DateRestMesa = fecha+" "+IDRestBusq+" "+IDMesaBuscado;
                                    ExisteReserva = ExistenciaReserva.execute(DateRestMesa).get();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if(ExisteReserva == false){
                                    String partes [] = salidaBuscarPoliticas.split(" ");
                                    String PoliticasSiNo   = partes[0];
                                    String UsuariosNegSiNo = partes[1];
                                    if(PoliticasSiNo.equalsIgnoreCase("true")==true && UsuariosNegSiNo.equalsIgnoreCase("true")==true){
                                        if(ValoracionNegativaComensal > 0){
                                            Toast.makeText(getApplicationContext(), "Usuarios con valoración negativa no pueden reservar en este restaurante, contacte con el local para más información", Toast.LENGTH_SHORT).show();
                                            BotonReserva.setEnabled(false);
                                        }else{
                                            String ReservaDefinitiva = fecha+" "+UserIDComensal+" "+IDMesaBuscado+" "+IDRestBusq;
                                            InsertarReserva.execute(ReservaDefinitiva);
                                            BotonReserva.setEnabled(false);
                                        }
                                    }
                                    if(PoliticasSiNo.equalsIgnoreCase("true")==true && UsuariosNegSiNo.equalsIgnoreCase("false")==true){
                                        String ReservaDefinitiva = fecha+" "+UserIDComensal+" "+IDMesaBuscado+" "+IDRestBusq;
                                        InsertarReserva.execute(ReservaDefinitiva);
                                        BotonReserva.setEnabled(false);
                                    }
                                    if(PoliticasSiNo.equalsIgnoreCase("false")==true){
                                        String ReservaDefinitiva = fecha+" "+UserIDComensal+" "+IDMesaBuscado+" "+IDRestBusq;
                                        InsertarReserva.execute(ReservaDefinitiva);
                                        BotonReserva.setEnabled(false);
                                    }
                                } if (ExisteReserva == true){
                                    Toast.makeText(getApplicationContext(), "No es posible reservar en este restaurante, para el "+fecha+" en la mesa "+NumMesa, Toast.LENGTH_SHORT).show();
                                    BotonReserva.setEnabled(false);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Por favor, seleccione una fecha del calendario.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if (ExisteMesa == false){
                            Toast.makeText(getApplicationContext(), "La mesa "+NumMesa+" no existe en este restaurante", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Por favor, introduzca un número de mesa.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor, seleccione una fecha del calendario.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BotonMenus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Menús del Restaurante.", Toast.LENGTH_SHORT).show();
                Intent aMenus = new Intent(PerfilComRestInfo.this, PerfilComRestInfoMenus.class);
                aMenus.putExtra("IDdelRestaurante", IDRestBusq);
                startActivity(aMenus);
            }
        });
    }

    public class BuscarRestaurante extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
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

    public class BuscarMesas extends AsyncTask<String, Void, ArrayList<String>> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
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
                String IdRest = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BMESR+SP+IdRest+CRLF).getBytes());
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
                    System.out.println("No hay mesas");
                }
            } catch (IOException err){
                err.printStackTrace();
                respuesta = "IOException: " + err.toString();
            }
            return mensajes;
        }

        @Override
        protected void onPostExecute(ArrayList <String> mesas) {
            super.onPostExecute(mesas);
            pdia.dismiss();
        }
    }

    public class BuscarExisteMesa extends AsyncTask<String, Void, Boolean> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
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
                String IdMesIdRest = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BMESNR+SP+IdMesIdRest+CRLF).getBytes());
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

    public class BuscarExisteReserva extends AsyncTask<String, Void, Boolean> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
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
                String FechaRestMesa  = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BRESVFRM+SP+FechaRestMesa+CRLF).getBytes());
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

    public class BuscarHabilitaPorIdRest extends AsyncTask<String, Void, String> {

        ProgressDialog pdia = null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

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
            pdia = new ProgressDialog(PerfilComRestInfo.this);
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
            ArrayList <String> temp = new ArrayList<>();
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

    public class BuscarValoracionNegComensal extends AsyncTask<String, Void, Integer> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected Integer doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            Integer salida = null;
            try {
                String IdComensal = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BCOMVNEG+SP+IdComensal+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                bis.close();
                os.close();
                cliente.close();
                salida = Integer.parseInt(respuesta);
            } catch (IOException err){
                err.printStackTrace();
                respuesta = "IOException: " + err.toString();
            }
            return salida;
        }

        @Override
        protected void onPostExecute(Integer salida) {
            super.onPostExecute(salida);
            pdia.dismiss();
        }
    }

    public class ConexionCrearReserva extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Creando Reserva.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... arg0){
            Socket cliente   = null;
            String respuesta = null;
            try {
                String ReservaEntrada = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IRESV+SP+ReservaEntrada+CRLF).getBytes());
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
                Toast.makeText(getApplicationContext(), "Se ha realizado su reserva, con ID "+respuesta, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class BuscarMesaSaberIDRest extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfo.this);
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

}
