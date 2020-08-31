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

public class PerfilHosteleroRestauranteVerMenuPlatos extends AppCompatActivity implements ProtocoloData {
    String IDdelMenuParaRestHost;
    String ResultadoMenuBuscado;
    String NumeroMenu;
    String NombreMenu;

    ArrayList<String> MenuMostrar = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestmismenusplatos);
        this.setTitle(R.string.perfilhost_mirestaurante_mimenu_textocabecera1);
        Bundle bundle = getIntent().getExtras();
        ListView ListaViewMenu = (ListView) findViewById(R.id.perfil_host_mirestmimenu_listviewMenu);
        Button  BotonCreaPlato = (Button) findViewById(R.id.perfil_host_mirestmimenu_botonCrearPlato);
        Button  BotonMisPlatos = (Button) findViewById(R.id.perfil_host_mirestmimenu_botonVerPlatos);
        Button  BotonCreaAlerg = (Button) findViewById(R.id.perfil_host_mirestmimenu_botonDefinirAlerg);

        if (bundle != null) {
            IDdelMenuParaRestHost = bundle.getString("IDMenuDelRestauanteHost");
        }

        BuscarMenuPorIdMenu UnMenu = new BuscarMenuPorIdMenu();
        try {
            ResultadoMenuBuscado = UnMenu.execute(IDdelMenuParaRestHost).get();
            String[] result = ResultadoMenuBuscado.split("&");
            if (result.length==1 && ResultadoMenuBuscado.isEmpty()==false){
                String Menu = result[0];
                NumeroMenu = Menu.substring(0,ResultadoMenuBuscado.indexOf(" ")).trim();
                NombreMenu = Menu.substring(ResultadoMenuBuscado.indexOf(" ")).trim();
                String MiMenu = "Número del Menú: "+NumeroMenu+"\n"+"Nombre del Menú: "+NombreMenu;
                MenuMostrar.add(MiMenu);
            }else{
                String MiMenu = "El menú con número: "+IDdelMenuParaRestHost+" no existe para este restaurante.";
                MenuMostrar.add(MiMenu);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, MenuMostrar);
        ListaViewMenu.setAdapter(adapter);

        BotonCreaPlato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Creación de Plato.", Toast.LENGTH_SHORT).show();
                Intent IcrearPlato = new Intent(PerfilHosteleroRestauranteVerMenuPlatos.this, PerfilHosteleroRestauranteVerMenuCreaPlatos.class);
                IcrearPlato.putExtra("IdMenuHostCreaPlato", IDdelMenuParaRestHost);
                startActivity(IcrearPlato);
            }
        });

        BotonMisPlatos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a los Platos del Menú.", Toast.LENGTH_SHORT).show();
                Intent IverPlato = new Intent(PerfilHosteleroRestauranteVerMenuPlatos.this, PerfilHosteleroRestauranteVerMenuVerPlatos.class);
                IverPlato.putExtra("IdMenuHostVerPlatos", IDdelMenuParaRestHost);
                startActivity(IverPlato);
            }
        });

        BotonCreaAlerg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Alérgenos del Menú.", Toast.LENGTH_SHORT).show();
                Intent IcreaAlerg = new Intent(PerfilHosteleroRestauranteVerMenuPlatos.this, PerfilHosteleroRestauranteVerMenuCreaAlerg.class);
                IcreaAlerg.putExtra("IdMenuHostCreaAlerg", IDdelMenuParaRestHost);
                startActivity(IcreaAlerg);
            }
        });
    }

    public class BuscarMenuPorIdMenu extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuPlatos.this);
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
                String IdMenus = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BM+SP+IdMenus+CRLF).getBytes());
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
