package com.jt.jailedtable;

import android.app.ProgressDialog;
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

public class PerfilComRestInfoMenusPlatos extends AppCompatActivity implements ProtocoloData {
    String IDMenuSeleccionado;
    String IDPlatosSalidaCompone;
    String IDAlergenoSalidaDispone;
    String PlatosSalida;
    String CostePlato;
    String NombrePlato;
    String AlergenosSalida;
    ArrayList<String> PlatosMostrar = new ArrayList<String>();
    ArrayList<String> AlergenosMostrar = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilcomrestinfomenusplatos);
        this.setTitle(R.string.perfilcom_restinformacionmenusplatos_cabeceratitle);
        Bundle bundle = getIntent().getExtras();
        ListView ListaViewPlatos = (ListView) findViewById(R.id.perfil_com_restinfomenusplatos_listviewPlatos);
        ListView ListaViewAlergenos = (ListView) findViewById(R.id.perfil_com_restinfomenusplatos_listviewAlergenos);

        if (bundle != null) {
            IDMenuSeleccionado = bundle.getString("IDMenuDeRestauante");
        }

        BuscarCompone            BuscaCompone = new BuscarCompone();
        BuscarPlatosPorIdPlato   BuscaPlato   = new BuscarPlatosPorIdPlato();
        BuscarDispone            BuscaDispone = new BuscarDispone();
        BuscarAlergenoPorIdAlerg BuscaAlerg  = new BuscarAlergenoPorIdAlerg();

        try {
            IDPlatosSalidaCompone = BuscaCompone.execute(IDMenuSeleccionado).get();
            if(IDPlatosSalidaCompone.isEmpty()==true){
                String PlatoMostrar ="No hay platos para este menú.";
                PlatosMostrar.add(PlatoMostrar);
            }else{
                PlatosSalida = BuscaPlato.execute(IDPlatosSalidaCompone).get();
                String[] result2 = PlatosSalida.split("&");
                if (result2.length==1 && PlatosSalida.isEmpty()==false){
                    String plato = result2[0];
                    CostePlato = plato.substring(0,PlatosSalida.indexOf(" ")).trim();
                    NombrePlato = plato.substring(PlatosSalida.indexOf(" ")).trim();
                    String PlatoMostrar = "Coste del Plato: "+CostePlato+"\n"+"Nombre del Plato: "+NombrePlato;
                    PlatosMostrar.add(PlatoMostrar);
                } if (result2.length>1) {
                    for (int x=0; x<result2.length; x++) {
                        String plato = result2[x];
                        CostePlato = plato.substring(0,PlatosSalida.indexOf(" ")).trim();
                        NombrePlato = plato.substring(PlatosSalida.indexOf(" ")).trim();
                        String PlatoMostrar = "Coste del Plato: "+CostePlato+"\n"+"Nombre del Plato: "+NombrePlato;
                        PlatosMostrar.add(x,PlatoMostrar);
                    }
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, PlatosMostrar);
        ListaViewPlatos.setAdapter(adapter2);

        try {
            IDAlergenoSalidaDispone = BuscaDispone.execute(IDMenuSeleccionado).get();
            if(IDAlergenoSalidaDispone.isEmpty()==true){
                String AlergenoMostrar ="El restaurante no ha definido alérgenos para este menú";
                AlergenosMostrar.add(AlergenoMostrar);
            }else {
                AlergenosSalida = BuscaAlerg.execute(IDAlergenoSalidaDispone).get();
                String[] result3 = AlergenosSalida.split("&");
                if (result3.length==1 && AlergenosSalida.isEmpty()==false){
                    String alergeno = result3[0];
                    String AlergenoShow = "Alérgenos Definidos:"+"\n"+alergeno;
                    AlergenosMostrar.add(AlergenoShow);
                } if (result3.length>1) {
                    for (int h=0; h<result3.length; h++) {
                        String alergeno = result3[h];
                        String AlergenoShow = "Alérgenos Definidos:"+"\n"+alergeno;
                        AlergenosMostrar.add(h,AlergenoShow);
                    }
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, AlergenosMostrar);
        ListaViewAlergenos.setAdapter(adapter3);
    }

    public class BuscarCompone extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfoMenusPlatos.this);
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
                String IddelMenu = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BCOMPM+SP+IddelMenu+CRLF).getBytes());
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

    public class BuscarPlatosPorIdPlato extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfoMenusPlatos.this);
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
            String Plato = "";
            ArrayList <String> temp = new ArrayList<>();
            try {
                String IdMenus = arg0[0];
                String [] camps = IdMenus.split(" ");
                while(camps.length>conteo){
                    Plato = camps[conteo];
                    if(Plato.isEmpty()==false){
                        InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                        cliente = new Socket();
                        cliente.connect(direccion);
                        BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        OutputStream os = cliente.getOutputStream();
                        respuesta = bis.readLine();
                        Log.d("Saludo", respuesta);
                        os.write((BP+SP+Plato+CRLF).getBytes());
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
                    respuesta += temp.get(h);
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

    public class BuscarDispone extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfoMenusPlatos.this);
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
                String IddelMenu = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BDISPM+SP+IddelMenu+CRLF).getBytes());
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

    public class BuscarAlergenoPorIdAlerg extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfoMenusPlatos.this);
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
            String Alergeno = "";
            ArrayList <String> temp = new ArrayList<>();
            try {
                String IdMenus = arg0[0];
                String [] camps = IdMenus.split(" ");
                while(camps.length>conteo){
                    Alergeno = camps[conteo];
                    if(Alergeno.isEmpty()==false){
                        InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                        cliente = new Socket();
                        cliente.connect(direccion);
                        BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        OutputStream os = cliente.getOutputStream();
                        respuesta = bis.readLine();
                        Log.d("Saludo", respuesta);
                        os.write((BA+SP+Alergeno+CRLF).getBytes());
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
                    respuesta += temp.get(h)+"&";
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
}
