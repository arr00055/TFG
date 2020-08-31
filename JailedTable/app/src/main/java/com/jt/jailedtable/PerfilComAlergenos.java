package com.jt.jailedtable;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class PerfilComAlergenos extends AppCompatActivity implements ProtocoloData {
    String UserIDComensal1 = "";
    String SalidaAlergenosSelecciona;
    String AlergenosEnviar = "";
    String ResultadoBuscarAlergPorIDAlerg ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilcomalergenos);
        this.setTitle(R.string.perfilcomensal_alergeno_cabecera2);
        SharedPreferences prefs = getSharedPreferences("UsuarioIdComensal", Context.MODE_PRIVATE);
        UserIDComensal1 = prefs.getString("IDComensal","");

        final Button CrearAlergeno     = (Button) findViewById(R.id.alergeno_boton_crear);
        final Button ActuaAlergeno     = (Button) findViewById(R.id.alergeno_boton_actualizar);

        ConexionBuscarComensalSelecciona     AlergenoBuscado  = new ConexionBuscarComensalSelecciona();
        ConexionSacarAlergeno                NombreAlergenos  = new ConexionSacarAlergeno();
        final ConexionInsertarAlergenoUser   InsertarAlergeno = new ConexionInsertarAlergenoUser();
        final ActualizarAlergenosComensal    ActualizaAlerg   = new ActualizarAlergenosComensal();

        try {
            SalidaAlergenosSelecciona = AlergenoBuscado.execute(UserIDComensal1).get();
            if (SalidaAlergenosSelecciona.isEmpty()==false) {
                CheckBox gluten            = (CheckBox)findViewById(R.id.alergeno_1_gluten);
                CheckBox crustaceo         = (CheckBox)findViewById(R.id.alergeno_2_crustaceo);
                CheckBox huevos            = (CheckBox)findViewById(R.id.alergeno_3_huevos);
                CheckBox pescado           = (CheckBox)findViewById(R.id.alergeno_4_pescado);
                CheckBox cacahuetes        = (CheckBox)findViewById(R.id.alergeno_5_cacahuetes);
                CheckBox soja              = (CheckBox)findViewById(R.id.alergeno_6_soja);
                CheckBox lacteo            = (CheckBox)findViewById(R.id.alergeno_7_lacteo);
                CheckBox molusco           = (CheckBox)findViewById(R.id.alergeno_8_molusco);
                CheckBox apio              = (CheckBox)findViewById(R.id.alergeno_9_apio);
                CheckBox mostaza           = (CheckBox)findViewById(R.id.alergeno_10_mostaza);
                CheckBox sesamo            = (CheckBox)findViewById(R.id.alergeno_11_sesamo);
                CheckBox sulfitos          = (CheckBox)findViewById(R.id.alergeno_12_sulfitos);
                CheckBox altramuces        = (CheckBox)findViewById(R.id.alergeno_13_altramuces);
                CheckBox frutascascara     = (CheckBox)findViewById(R.id.alergeno_14_frutcascara);
                CrearAlergeno.setEnabled(false);
                ResultadoBuscarAlergPorIDAlerg = NombreAlergenos.execute(SalidaAlergenosSelecciona).get();
                String [] TrocearNombreAlergenos = ResultadoBuscarAlergPorIDAlerg.split(" ");
                for(int busc = 0; busc<TrocearNombreAlergenos.length; busc++){
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Gluten")){
                        gluten.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Crustaceo")){
                        crustaceo.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Huevos")){
                        huevos.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Pescado")){
                        pescado.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Cacahuetes")){
                        cacahuetes.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Soja")){
                        soja.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Lacteos")){
                        lacteo.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Moluscos")){
                        molusco.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Apio")){
                        apio.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Mostaza")){
                        mostaza.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Sesamo")){
                        sesamo.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Sulfitos")){
                        sulfitos.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Altramuces")){
                        altramuces.setChecked(true);
                    }
                    if(TrocearNombreAlergenos[busc].equalsIgnoreCase("Cascaras")){
                        frutascascara.setChecked(true);
                    }
                }
            }else{
                ActuaAlergeno.setEnabled(false);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ActuaAlergeno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                CheckBox gluten            = (CheckBox)findViewById(R.id.alergeno_1_gluten);
                CheckBox crustaceo         = (CheckBox)findViewById(R.id.alergeno_2_crustaceo);
                CheckBox huevos            = (CheckBox)findViewById(R.id.alergeno_3_huevos);
                CheckBox pescado           = (CheckBox)findViewById(R.id.alergeno_4_pescado);
                CheckBox cacahuetes        = (CheckBox)findViewById(R.id.alergeno_5_cacahuetes);
                CheckBox soja              = (CheckBox)findViewById(R.id.alergeno_6_soja);
                CheckBox lacteo            = (CheckBox)findViewById(R.id.alergeno_7_lacteo);
                CheckBox molusco           = (CheckBox)findViewById(R.id.alergeno_8_molusco);
                CheckBox apio              = (CheckBox)findViewById(R.id.alergeno_9_apio);
                CheckBox mostaza           = (CheckBox)findViewById(R.id.alergeno_10_mostaza);
                CheckBox sesamo            = (CheckBox)findViewById(R.id.alergeno_11_sesamo);
                CheckBox sulfitos          = (CheckBox)findViewById(R.id.alergeno_12_sulfitos);
                CheckBox altramuces        = (CheckBox)findViewById(R.id.alergeno_13_altramuces);
                CheckBox frutascascara     = (CheckBox)findViewById(R.id.alergeno_14_frutcascara);
                if(gluten.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Gluten";
                }
                if(crustaceo.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Crustaceo";
                }
                if(huevos.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Huevos";
                }
                if(pescado.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Pescado";
                }
                if(cacahuetes.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Cacahuetes";
                }
                if(soja.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Soja";
                }
                if(lacteo.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Lacteos";
                }
                if(molusco.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Moluscos";
                }
                if(apio.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Apio";
                }
                if(mostaza.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Mostaza";
                }
                if(sesamo.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Sesamo";
                }
                if(sulfitos.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Sulfitos";
                }
                if(altramuces.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Altramuces";
                }
                if(frutascascara.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Cascaras";
                }
                Toast.makeText(getApplicationContext(), "Enviando Alérgenos.", Toast.LENGTH_SHORT).show();
                ActualizaAlerg.execute(AlergenosEnviar,SalidaAlergenosSelecciona);
                ActuaAlergeno.setEnabled(false);
            }
        });

        CrearAlergeno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                CheckBox gluten            = (CheckBox)findViewById(R.id.alergeno_1_gluten);
                CheckBox crustaceo         = (CheckBox)findViewById(R.id.alergeno_2_crustaceo);
                CheckBox huevos            = (CheckBox)findViewById(R.id.alergeno_3_huevos);
                CheckBox pescado           = (CheckBox)findViewById(R.id.alergeno_4_pescado);
                CheckBox cacahuetes        = (CheckBox)findViewById(R.id.alergeno_5_cacahuetes);
                CheckBox soja              = (CheckBox)findViewById(R.id.alergeno_6_soja);
                CheckBox lacteo            = (CheckBox)findViewById(R.id.alergeno_7_lacteo);
                CheckBox molusco           = (CheckBox)findViewById(R.id.alergeno_8_molusco);
                CheckBox apio              = (CheckBox)findViewById(R.id.alergeno_9_apio);
                CheckBox mostaza           = (CheckBox)findViewById(R.id.alergeno_10_mostaza);
                CheckBox sesamo            = (CheckBox)findViewById(R.id.alergeno_11_sesamo);
                CheckBox sulfitos          = (CheckBox)findViewById(R.id.alergeno_12_sulfitos);
                CheckBox altramuces        = (CheckBox)findViewById(R.id.alergeno_13_altramuces);
                CheckBox frutascascara     = (CheckBox)findViewById(R.id.alergeno_14_frutcascara);
                if(gluten.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Gluten";
                }
                if(crustaceo.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Crustaceo";
                }
                if(huevos.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Huevos";
                }
                if(pescado.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Pescado";
                }
                if(cacahuetes.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Cacahuetes";
                }
                if(soja.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Soja";
                }
                if(lacteo.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Lacteos";
                }
                if(molusco.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Moluscos";
                }
                if(apio.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Apio";
                }
                if(mostaza.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Mostaza";
                }
                if(sesamo.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Sesamo";
                }
                if(sulfitos.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Sulfitos";
                }
                if(altramuces.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Altramuces";
                }
                if(frutascascara.isChecked()==true){
                    AlergenosEnviar = AlergenosEnviar+" "+"Cascaras";
                }
                Toast.makeText(getApplicationContext(), "Enviando Alérgenos.", Toast.LENGTH_SHORT).show();
                InsertarAlergeno.execute(AlergenosEnviar);
                CrearAlergeno.setEnabled(false);
            }
        });
    }

    public class ConexionInsertarAlergenoUser extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComAlergenos.this);
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
                String alergenos = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((IAC+SP+alergenos+"-"+UserIDComensal1+CRLF).getBytes());
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

    public class ConexionBuscarComensalSelecciona extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComAlergenos.this);
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
                String UsuarioComensalID = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BSIDC+SP+UsuarioComensalID+CRLF).getBytes());
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

    public class ConexionSacarAlergeno extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComAlergenos.this);
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
                String AlergenoID = arg0[0];
                Log.d("AlergenoID", AlergenoID);
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BA+SP+AlergenoID+CRLF).getBytes());
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

    public class ActualizarAlergenosComensal extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilComAlergenos.this);
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
                String alergenos = arg0[0];
                String idalergeno  = arg0[1];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((ACTALERGC+SP+alergenos+"-"+idalergeno+CRLF).getBytes());
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
