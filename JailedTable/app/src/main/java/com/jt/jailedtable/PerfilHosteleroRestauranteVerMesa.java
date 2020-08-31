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

public class PerfilHosteleroRestauranteVerMesa extends AppCompatActivity implements ProtocoloData {

    String IDdelRestaurante;
    String MesaMostrar;

    ArrayList<String> salidaBuscarMesa = new ArrayList<String>();
    ArrayList<String> MesasMostrando = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestmismesas);
        this.setTitle(R.string.perfilhost_mirestaurante_infomesas_header1);
        Bundle bundle = getIntent().getExtras();

        final Button BotonIntroducir = (Button) findViewById(R.id.perfil_host_mirestinfomesas_botonNumMesa);
        ListView ListaViewMesas = (ListView) findViewById(R.id.perfil_host_mirestinfomesas_listviewMesas);

        if (bundle != null) {
            IDdelRestaurante = bundle.getString("IdRestHostaMisMesas");
        }

        BuscarMesas BuscandoMesas = new BuscarMesas();
        try {
            salidaBuscarMesa  = BuscandoMesas.execute(IDdelRestaurante).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            }
        }else{
            MesaMostrar = "No hay mesas creadas para este restaurante.";
            MesasMostrando.add(MesaMostrar);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, MesasMostrando);
        ListaViewMesas.setAdapter(adapter);

        BotonIntroducir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText nummesa = (EditText) findViewById(R.id.perfil_host_mirestinfomesas_etNumMesa);
                String NumMesa = nummesa.getText().toString();
                if(NumMesa.isEmpty() == false) {
                        Toast.makeText(getApplicationContext(), "Accediendo a la mesa número "+NumMesa, Toast.LENGTH_SHORT).show();
                        Intent HaciaReservas = new Intent(PerfilHosteleroRestauranteVerMesa.this, PerfilHosteleroRestauranteVerMesaReservas.class);
                        HaciaReservas.putExtra("NumMesaDelRestauanteHost",NumMesa);
                        HaciaReservas.putExtra("IDRestDelRestauanteHost",IDdelRestaurante);
                        startActivity(HaciaReservas);
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor, introduzca un número de mesa.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class BuscarMesas extends AsyncTask<String, Void, ArrayList<String>> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesa.this);
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
}
