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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class PerfilHosteleroRestauranteVerMesaReservasValora extends AppCompatActivity implements ProtocoloData {

    String IDRestaurante;
    String IDMesa;
    String fecha;
    String UsuarioPuedeValorarse;
    ArrayList<String> SalidaBuscarReserva = new ArrayList<String>();
    ArrayList<String> ReservasParaValorar = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhosteleromirestmismesasvalorar);
        this.setTitle(R.string.perfilhost_mirestaurante_mismesas_reservas_valorar_titulo);
        Bundle bundle = getIntent().getExtras();

        Button BotonPasoValorar = (Button) findViewById(R.id.perfil_host_mirestinfomesasreservasvalora_Boton);
        ListView ListaViewReservPuedeValorar = (ListView) findViewById(R.id.perfil_host_mirestinfomesasreservasvalora_listviewReservas);

        if (bundle != null) {
            IDMesa = bundle.getString("IDdelaMesa");
            IDRestaurante = bundle.getString("IDdelRestauranteValora");
        }

        Date fec = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        fecha = df.format(fec);

        BuscarlasReservas Reserva = new BuscarlasReservas();
        try {
            SalidaBuscarReserva = Reserva.execute(IDRestaurante,IDMesa).get();
            if(SalidaBuscarReserva.isEmpty()==false){
                for (int x=0; x<SalidaBuscarReserva.size(); x++) {
                    String salida2 = SalidaBuscarReserva.get(x);
                    String[] result2 = salida2.split(" ");
                    String ID_Reserva = result2[0];
                    String ID_Usuario = result2[1];
                    String Fecha_Reserva = result2[2];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c = Calendar.getInstance();
                    try {
                        c.setTime(sdf.parse(Fecha_Reserva));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    c.add(Calendar.DATE, 1);
                    String Fecha_Reserva_Compara = sdf.format(c.getTime());
                    if(fecha.equals(Fecha_Reserva_Compara)==true){
                        UsuarioPuedeValorarse = "Número del Comensal "+ID_Usuario+"\n"+"Número de la Reserva "+ID_Reserva;
                        ReservasParaValorar.add(UsuarioPuedeValorarse);
                    }
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_com_reserv, ReservasParaValorar);
            ListaViewReservPuedeValorar.setAdapter(adapter);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BotonPasoValorar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                EditText numeroreserva = (EditText) findViewById(R.id.perfil_host_mirestinfomesasreservasvalora_etNumReserva);
                String NumReserva = numeroreserva.getText().toString();
                if(NumReserva.isEmpty() == false) {
                    Toast.makeText(getApplicationContext(), "Accediendo a valorar la reserva número "+NumReserva, Toast.LENGTH_SHORT).show();
                    Intent Valorando = new Intent(PerfilHosteleroRestauranteVerMesaReservasValora.this, PerfilHosteleroRestauranteVerMesaReservasValoraFin.class);
                    Valorando.putExtra("NumReservaValorar",NumReserva);
                    Valorando.putExtra("NumRestValorar",IDRestaurante);
                    Valorando.putExtra("NumMesaValorar",IDMesa);
                    startActivity(Valorando);
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor, introduzca un número de reserva.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class BuscarlasReservas extends AsyncTask<String, Void, ArrayList<String>> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMesaReservasValora.this);
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
                String IDRest = arg0[0];
                String IDMesa = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BRESVIRIM+SP+IDRest+SP+IDMesa+CRLF).getBytes());
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
                        System.out.println(mensajes.get(x));
                    }
                }   if(result.length==1 && respuesta.isEmpty()==true) {
                    //String vacio = "";
                    //mensajes.add(vacio);
                    //System.out.println("No hay reservas");
                    UsuarioPuedeValorarse = "No hay reservas pendientes de valoración.";
                    ReservasParaValorar.add(UsuarioPuedeValorarse);
                }

            } catch (IOException err){
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
