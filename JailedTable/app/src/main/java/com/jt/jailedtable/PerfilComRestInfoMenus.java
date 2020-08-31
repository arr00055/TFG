package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class PerfilComRestInfoMenus extends AppCompatActivity implements ProtocoloData {

    String IDRest;
    String IDMenusSalidaEstablece;
    String MenusSalida;
    String NumeroMenu;
    String NombreMenu;

    ArrayList<String> MenusMostrar = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilcomrestinfomenus);
        Bundle bundle = getIntent().getExtras();
        Button BotonIntroducir = (Button) findViewById(R.id.perfil_com_restinfomenus_boton);
        ListView ListaViewMenus = (ListView) findViewById(R.id.perfil_com_restinfomenus_listviewMenus);
        this.setTitle(R.string.perfilcomensal_restinfomenus_cabecera1);

        if (bundle != null) {
            IDRest = bundle.getString("IDdelRestaurante");
        }

        BuscarEstablece BuscarEstablece   = new BuscarEstablece();
        BuscarMenusPorIdMenu BuscaMenu    = new BuscarMenusPorIdMenu();

        try {
            IDMenusSalidaEstablece = BuscarEstablece.execute(IDRest).get();
            if(IDMenusSalidaEstablece.isEmpty()==true){
                String MenuMostrar ="No hay menús para este restaurante";
                MenusMostrar.add(MenuMostrar);
            }else{
                MenusSalida = BuscaMenu.execute(IDMenusSalidaEstablece).get();
                String[] result = MenusSalida.split("&");
                if (result.length==1 && MenusSalida.isEmpty()==false){
                    String Menu = result[0];
                    NumeroMenu = Menu.substring(0,MenusSalida.indexOf(" ")).trim();
                    NombreMenu = Menu.substring(MenusSalida.indexOf(" ")).trim();
                    String MenuMostrar = "Número del Menú: "+NumeroMenu+"\n"+"Nombre del Menú: "+NombreMenu;
                    MenusMostrar.add(MenuMostrar);
                } if (result.length>1) {
                    for (int x=0; x<result.length; x++) {
                        String Menu = result[x];
                        NumeroMenu = Menu.substring(0,MenusSalida.indexOf(" ")).trim();
                        NombreMenu = Menu.substring(MenusSalida.indexOf(" ")).trim();
                        String MenuMostrar = "Número del Menú: "+NumeroMenu+"\n"+"Nombre del Menú: "+NombreMenu;
                        MenusMostrar.add(x,MenuMostrar);
                    }
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, MenusMostrar);
        ListaViewMenus.setAdapter(adapter);

        BotonIntroducir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText numermenu = (EditText) findViewById(R.id.perfil_com_restinfomenus_nummenu);
                String NunMenu = numermenu.getText().toString();
                if(NunMenu.isEmpty() == false) {
                    Toast.makeText(getApplicationContext(), "Accediendo a los Platos del Menú Seleccionado.", Toast.LENGTH_SHORT).show();
                    Intent aPlatos = new Intent(PerfilComRestInfoMenus.this, PerfilComRestInfoMenusPlatos.class);
                    aPlatos.putExtra("IDMenuDeRestauante",NunMenu);
                    startActivity(aPlatos);
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor, introduzca un número de menú.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class BuscarEstablece extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfoMenus.this);
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
                        os.write((BESTR+SP+IdRest+CRLF).getBytes());
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

    public class BuscarMenusPorIdMenu extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestInfoMenus.this);
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
            String Menu = "";
            ArrayList <String> temp = new ArrayList<>();
            try {
                String IdMenus = arg0[0];
                String [] camps = IdMenus.split(" ");
                while(camps.length>conteo){
                    Menu = camps[conteo];
                    if(Menu.isEmpty()==false){
                        InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                        cliente = new Socket();
                        cliente.connect(direccion);
                        BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        OutputStream os = cliente.getOutputStream();
                        respuesta = bis.readLine();
                        Log.d("Saludo", respuesta);
                        os.write((BM+SP+Menu+CRLF).getBytes());
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
}
