package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class PerfilHosteleroMisRest extends AppCompatActivity implements ProtocoloData {

    ListView BusquedaRestListView;
    String UserIDHostelero;
    String RestauranteMostrar = "";
    ArrayList<String> Restaurantes = new ArrayList<String>();
    ArrayList<String> MensjMostrar = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    Boolean ExisteRestauranteHostelero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmisrest);
        this.setTitle(R.string.perfilhost_misrestaurantes_header);

        SharedPreferences prefs = getSharedPreferences("UsuarioIdHostelero", Context.MODE_PRIVATE);
        UserIDHostelero = prefs.getString("IDHostelero","");

        Button BotonBusquedaRest = (Button) findViewById(R.id.perfil_host_misrestaurantes_botonbuscar);
        BusquedaRestListView = (ListView) findViewById(R.id.perfil_host_misrestaurantes_listview);

        ConexionMostrarRestaurantes buscarRestaurantes = new ConexionMostrarRestaurantes();
        try {
            Restaurantes = buscarRestaurantes.execute(UserIDHostelero).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(Restaurantes.isEmpty()==false){
            for (int x=0; x<Restaurantes.size(); x++) {
                String Restaurante2 = Restaurantes.get(x);
                String camps[] = Restaurante2.split(" ");
                if (camps.length>6){
                    String idRest = camps[0];
                    String nombrerest = camps[1];
                    String nombrerest2 = camps[2];
                    String comunidadrest = camps[3];
                    String provinciarest = camps[4];
                    String localidadrest = camps[5];
                    String numtlfrest = camps[6];
                    RestauranteMostrar = "Nombre del Restaurante: "+nombrerest+" "+nombrerest2+"\n"+"Número del Restaurante: "+idRest+"\n"+"Localización: "+comunidadrest+" "+provinciarest+" "+localidadrest+"\n"+"Número de Teléfono: "+numtlfrest;
                }else{
                    String idRest = camps[0];
                    String nombrerest = camps[1];
                    String comunidadrest = camps[2];
                    String provinciarest = camps[3];
                    String localidadrest = camps[4];
                    String numtlfrest = camps[5];
                    RestauranteMostrar = "Nombre del Restaurante: "+nombrerest+"\n"+"Número del Restaurante: "+idRest+"\n"+"Localización: "+comunidadrest+" "+provinciarest+" "+localidadrest+"\n"+"Número de Teléfono: "+numtlfrest;
                }

                MensjMostrar.add(x,RestauranteMostrar);

                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MensjMostrar);
                BusquedaRestListView.setAdapter(adapter);
            }
        }

        BotonBusquedaRest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View){
                EditText numRest = (EditText) findViewById(R.id.perfil_host_misrestaurantes_edittextNumRest);
                String numeroRestaurante2 = numRest.getText().toString();

                if(numeroRestaurante2.isEmpty() == false) {
                    ExisteRestPorIdHosteIdRest ExisteRestBuscado = new ExisteRestPorIdHosteIdRest();
                    try {
                        ExisteRestauranteHostelero = ExisteRestBuscado.execute(numeroRestaurante2,UserIDHostelero).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(ExisteRestauranteHostelero == true){
                        Toast.makeText(getApplicationContext(), "Información del restaurante con número "+numeroRestaurante2, Toast.LENGTH_SHORT).show();
                        Intent numeroRest = new Intent(PerfilHosteleroMisRest.this, PerfilHosteleroRestaurante.class);
                        numeroRest.putExtra("IdRestHostBuscado", numeroRestaurante2);
                        startActivity(numeroRest);
                    }else{
                        Toast.makeText(getApplicationContext(), "No existe el restaurante con número "+numeroRestaurante2+" en su cuenta.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No ha introducido ningún número.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_host_rest_busqueda, menu);
        MenuItem searchViewItem = menu.findItem(R.id.perfil_host_rest_menu_search);
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

    public class ConexionMostrarRestaurantes extends AsyncTask<String, Void, ArrayList <String>> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroMisRest.this);
            pdia.setIndeterminate(true);
            pdia.setMessage("Trabajando en ello.");
            pdia.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected ArrayList<String> doInBackground(String... arg0) {
            Socket cliente = null;
            String respuesta = null;
            ArrayList<String> mensajes = new ArrayList<String>();
            try {
                String IdHoste = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP, Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BRESTH+SP+IdHoste+CRLF).getBytes());
                os.flush();
                respuesta = bis.readLine();
                Log.d("Respuesta Serv Comensal", respuesta);
                bis.close();
                os.close();
                cliente.close();
                String[] result = respuesta.split("&");
                if (result.length == 1 && respuesta.isEmpty() == false) {
                    String restaurantes = respuesta;
                    System.out.println(restaurantes);
                    restaurantes = respuesta.substring(0, respuesta.indexOf("&"));
                    System.out.println(restaurantes);
                    mensajes.add(restaurantes);
                }
                if (result.length > 1) {
                    for (int x = 0; x < result.length; x++) {
                        System.out.println(result[x]);
                        String camps[] = result[x].split(" ");
                        if (camps.length>6){
                            String campo1 = camps[0];
                            String campo2 = camps[1];
                            String campo3 = camps[2];
                            String campo4 = camps[3];
                            String campo5 = camps[4];
                            String campo6 = camps[5];
                            String campo7 = camps[6];
                            mensajes.add(result[x]);
                        }else{
                            String campo1 = camps[0];
                            String campo2 = camps[1];
                            String campo3 = camps[2];
                            String campo4 = camps[3];
                            String campo5 = camps[4];
                            String campo6 = camps[5];
                            mensajes.add(result[x]);
                        }
                    }
                }
                if (result.length == 1 && respuesta.isEmpty() == true) {
                    System.out.println("Aún no has creado ningún restaurante.");
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

    public class ExisteRestPorIdHosteIdRest extends AsyncTask<String, Void, Boolean> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroMisRest.this);
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
                String IdRest1 = arg0[0];
                String IdHost1 = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BRESTPIDS+SP+IdRest1+SP+IdHost1+CRLF).getBytes());
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
}