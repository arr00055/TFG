package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class PerfilComRestaurantes extends AppCompatActivity implements ProtocoloData {

    ListView listView;
    ArrayList<String> Restaurantes;
    ArrayAdapter<String> adapter;
    String RestauranteMostrar = "";
    ArrayList<String> MensjMostrar = new ArrayList<String>();
    Boolean ComprobarExisteRestPorIDRest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilcomrestaurantes);
        this.setTitle(R.string.perfilcomensal_rest_cabecera);

        Button botonrest = (Button) findViewById(R.id.perfil_com_rest_boton);
        listView = (ListView) findViewById(R.id.perfil_com_restaurante_listview);

        ConexionMostrarRestaurantes buscarRestaurantes = new ConexionMostrarRestaurantes();
        try {
            Restaurantes = buscarRestaurantes.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(Restaurantes.isEmpty()==false){
            for (int x=0; x<Restaurantes.size(); x++) {
                String Restaurante2 = Restaurantes.get(x);
                String camps[] = Restaurante2.split(" ");
                if (camps.length>7){
                    String idRest = camps[0];
                    String campo2 = camps[1];
                    String nombrerest = camps[2];
                    String nombrerest2 = camps[3];
                    String comunidadrest = camps[4];
                    String provinciarest = camps[5];
                    String localidadrest = camps[6];
                    String numtlfrest = camps[7];
                    RestauranteMostrar = "Nombre del Restaurante: "+nombrerest+" "+nombrerest2+"\n"+"Número del Restaurante: "+idRest+"\n"+"Localización: "+comunidadrest+" "+provinciarest+" "+localidadrest+"\n"+"Número de Teléfono: "+numtlfrest;
                }else{
                    String idRest = camps[0];
                    String campo2 = camps[1];
                    String nombrerest = camps[2];
                    String comunidadrest = camps[3];
                    String provinciarest = camps[4];
                    String localidadrest = camps[5];
                    String numtlfrest = camps[6];
                    RestauranteMostrar = "Nombre del Restaurante: "+nombrerest+"\n"+"Número del Restaurante: "+idRest+"\n"+"Localización: "+comunidadrest+" "+provinciarest+" "+localidadrest+"\n"+"Número de Teléfono: "+numtlfrest;
                }

                MensjMostrar.add(x,RestauranteMostrar);

                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MensjMostrar);
                listView.setAdapter(adapter);
            }
        }

        botonrest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View){
                EditText numRest = (EditText) findViewById(R.id.perfil_com_rest_TextBusq);
                String numeroRestaurante = numRest.getText().toString();

                if(numeroRestaurante.isEmpty() == false) {
                    ExisteRestPorIDRest ExistenciaRest = new ExisteRestPorIDRest();
                    try {
                        ComprobarExisteRestPorIDRest = ExistenciaRest.execute(numeroRestaurante).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(ComprobarExisteRestPorIDRest == true){
                        Toast.makeText(getApplicationContext(), "Información del restaurante.", Toast.LENGTH_SHORT).show();
                        Intent numeroRest = new Intent(PerfilComRestaurantes.this, PerfilComRestInfo.class);
                        numeroRest.putExtra("idrestaurantebusq", numeroRestaurante);
                        startActivity(numeroRest);
                    }else{
                        Toast.makeText(getApplicationContext(), "No existe el restaurante con número "+numeroRestaurante+" en su cuenta.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "No ha introducido ningún número.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }//Fin del onCreate.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rest_busqueda, menu);
        MenuItem searchViewItem = menu.findItem(R.id.perfil_rest_menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
             if(Restaurantes.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    //Toast.makeText(PerfilComRestaurantes.this, "No se encontraron coincidencias.",Toast.LENGTH_LONG).show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public class ExisteRestPorIDRest extends AsyncTask<String, Void, Boolean> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestaurantes.this);
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
                String IdRestaBuscar = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BREST+SP+IdRestaBuscar+CRLF).getBytes());
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

    public class ConexionMostrarRestaurantes extends AsyncTask<Void, Void, ArrayList <String>> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComRestaurantes.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            Socket cliente = null;
            String respuesta = null;
            ArrayList<String> mensajes = new ArrayList<String>();
            try {
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP, Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((MRS+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                bis.close();
                os.close();
                cliente.close();
                String[] result = respuesta.split("&");
                if (result.length == 1 && respuesta.isEmpty() == false) {
                    String restaurantes = respuesta;
                    restaurantes = respuesta.substring(0, respuesta.indexOf("&"));
                    mensajes.add(restaurantes);
                }
                if (result.length > 1) {
                    for (int x = 0; x < result.length; x++) {
                        String camps[] = result[x].split(" ");
                        if (camps.length>7){
                            String campo1 = camps[0];
                            String campo2 = camps[1];
                            String campo3 = camps[2];
                            String campo4 = camps[3];
                            String campo5 = camps[4];
                            String campo6 = camps[5];
                            String campo7 = camps[6];
                            String campo8 = camps[7];
                            mensajes.add(result[x]);
                        }else{
                            String campo1 = camps[0];
                            String campo2 = camps[1];
                            String campo3 = camps[2];
                            String campo4 = camps[3];
                            String campo5 = camps[4];
                            String campo6 = camps[5];
                            String campo7 = camps[6];
                            mensajes.add(result[x]);
                        }
                    }
                }
                if (result.length == 1 && respuesta.isEmpty() == true) {
                    System.out.println("No hay restaurantes");
                }

            } catch (IOException err) {
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
}