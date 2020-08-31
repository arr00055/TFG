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

public class PerfilHosteleroRestaurante extends AppCompatActivity implements ProtocoloData {

    String UserIDHostelero;
    String IDRestPasadoDelIntent;
    String ElRestaurante;
    String RestauranteMostrar;
    ListView ListaMuestraRestaurante;
    ArrayList<String> ElRestauranteMostrar = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostrestaurante);
        this.setTitle(R.string.perfilhost_restaurante_cabecera);
        Bundle bundle = getIntent().getExtras();
        SharedPreferences prefs = getSharedPreferences("UsuarioIdHostelero", Context.MODE_PRIVATE);
        UserIDHostelero = prefs.getString("IDHostelero","");
        if (bundle != null) {
            IDRestPasadoDelIntent = bundle.getString("IdRestHostBuscado");
        }

        ListaMuestraRestaurante = (ListView) findViewById(R.id.perfil_host_mirestaurante_listviewInfoRest);

        Button  BotonCreaMenu = (Button) findViewById(R.id.perfil_host_mirestaurante_boton1);
        Button  BotonMisMenus = (Button) findViewById(R.id.perfil_host_mirestaurante_boton2);
        Button  BotonCreaMesa = (Button) findViewById(R.id.perfil_host_mirestaurante_boton3);
        Button  BotonMisMesas = (Button) findViewById(R.id.perfil_host_mirestaurante_boton4);
        Button  BotonPolitica = (Button) findViewById(R.id.perfil_host_mirestaurante_boton5);

        BuscarRestaurante BusacarElRestaurante = new BuscarRestaurante();
        try {
            ElRestaurante = BusacarElRestaurante.execute(IDRestPasadoDelIntent).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ElRestaurante.isEmpty()==false){
            String[] resultadoSalidaBuscarRest = ElRestaurante.split(" ");
            if (resultadoSalidaBuscarRest.length>6){
                String NombreRest1         = resultadoSalidaBuscarRest[1];
                String NombreRest2         = resultadoSalidaBuscarRest[2];
                String NumTlfRest          = resultadoSalidaBuscarRest[6];

                RestauranteMostrar = "Información del Restaurante:"+"\n"+"Nombre del Restaurante: "+NombreRest1+" "+NombreRest2+"\n"+"Número del Restaurante: "+IDRestPasadoDelIntent+"\n"+"Número de Teléfono: "+NumTlfRest;
                ElRestauranteMostrar.add(RestauranteMostrar);
            }else{
                String NombreRest         = resultadoSalidaBuscarRest[1];
                String NumTlfRest         = resultadoSalidaBuscarRest[5];

                RestauranteMostrar = "Información del Restaurante:"+"\n"+"Nombre del Restaurante: "+NombreRest+"\n"+"Número del Restaurante: "+IDRestPasadoDelIntent+"\n"+"Número de Teléfono: "+NumTlfRest;
                ElRestauranteMostrar.add(RestauranteMostrar);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, ElRestauranteMostrar);
            ListaMuestraRestaurante.setAdapter(adapter);
        }


        BotonCreaMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Creación de Menú.", Toast.LENGTH_SHORT).show();
                Intent IcrearMenu = new Intent(PerfilHosteleroRestaurante.this, PerfilHosteleroRestauranteCreaMenu.class);
                IcrearMenu.putExtra("IdRestHostaCreaMenu", IDRestPasadoDelIntent);
                startActivity(IcrearMenu);
            }
        });

        BotonMisMenus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a sus Menús.", Toast.LENGTH_SHORT).show();
                Intent IverMenu = new Intent(PerfilHosteleroRestaurante.this, PerfilHosteleroRestauranteVerMenu.class);
                IverMenu.putExtra("IdRestHostaMisMenus", IDRestPasadoDelIntent);
                startActivity(IverMenu);
            }
        });

        BotonCreaMesa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a Creación de Mesa.", Toast.LENGTH_SHORT).show();
                Intent IcreaMesa = new Intent(PerfilHosteleroRestaurante.this, PerfilHosteleroRestauranteCreaMesa.class);
                IcreaMesa.putExtra("IdRestHostaCreaMesa", IDRestPasadoDelIntent);
                startActivity(IcreaMesa);
            }
        });

        BotonMisMesas.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a sus Mesas.", Toast.LENGTH_SHORT).show();
                Intent IverMesa = new Intent(PerfilHosteleroRestaurante.this, PerfilHosteleroRestauranteVerMesa.class);
                IverMesa.putExtra("IdRestHostaMisMesas", IDRestPasadoDelIntent);
                startActivity(IverMesa);
            }
        });

        BotonPolitica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                Toast.makeText(getApplicationContext(), "Accediendo a sus Políticas.", Toast.LENGTH_SHORT).show();
                Intent IPolitica = new Intent(PerfilHosteleroRestaurante.this, PerfilHosteleroRestaurantePoliticas.class);
                IPolitica.putExtra("IdRestHostaPoliticas", IDRestPasadoDelIntent);
                startActivity(IPolitica);
            }//Fin onClick.
        });
    }


    public class BuscarRestaurante extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestaurante.this);
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
}
