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

public class PerfilHosteleroRestauranteVerMenuVerPlatos extends AppCompatActivity implements ProtocoloData {

    String IDdelMenuParaVerPlatos;
    String IDPlatosSalidaCompone;
    String PlatosSalida;
    String CostePlato;
    String NombrePlato;

    ArrayList<String> PlatosMostrar = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestmismenusverplatos);
        this.setTitle(R.string.perfilhost_mirestaurante_mimenu_verrplato_textocabeceraplato);
        Bundle bundle = getIntent().getExtras();
        ListView ListaViewTodosLosPlatos = (ListView) findViewById(R.id.perfil_host_mirest_vermenu_verplato_lwplatos);

        if (bundle != null) {
            IDdelMenuParaVerPlatos = bundle.getString("IdMenuHostVerPlatos");
        }

        BuscarEnCompone        BuscandoCompone = new BuscarEnCompone();
        BuscarPlatosConIdPlato BuscandoPlatos  = new BuscarPlatosConIdPlato();
        try {
            IDPlatosSalidaCompone = BuscandoCompone.execute(IDdelMenuParaVerPlatos).get();
            if(IDPlatosSalidaCompone.isEmpty()==true){
                String PlatoMostrar ="No hay platos creados en el menú.";
                PlatosMostrar.add(PlatoMostrar);
            }else{
                PlatosSalida = BuscandoPlatos.execute(IDPlatosSalidaCompone).get();
                String[] result2 = PlatosSalida.split("&");
                if (result2.length==1 && PlatosSalida.isEmpty()==false){
                    String plato = result2[0];
                    CostePlato = plato.substring(0,PlatosSalida.indexOf(" ")).trim();
                    NombrePlato = plato.substring(PlatosSalida.indexOf(" ")).trim();
                    String PlatoMostrar = "Nombre del Plato: "+NombrePlato+"\n"+"Coste del Plato: "+CostePlato;
                    PlatosMostrar.add(PlatoMostrar);
                } if (result2.length>1) {
                    for (int x=0; x<result2.length; x++) {
                        String plato = result2[x];
                        CostePlato = plato.substring(0,PlatosSalida.indexOf(" ")).trim();
                        NombrePlato = plato.substring(PlatosSalida.indexOf(" ")).trim();
                        String PlatoMostrar = "Nombre del Plato: "+NombrePlato+"\n"+"Coste del Plato: "+CostePlato;
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
        ListaViewTodosLosPlatos.setAdapter(adapter2);

    }

    public class BuscarEnCompone extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuVerPlatos.this);
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
                String IdMenu = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BCOMPM+SP+IdMenu+CRLF).getBytes());
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

    public class BuscarPlatosConIdPlato extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuVerPlatos.this);
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
                    System.out.println("Exit: "+respuesta);
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
