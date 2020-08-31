package com.jt.jailedtable;

import android.app.ProgressDialog;
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

public class PerfilHosteleroRestauranteVerMenuCreaAlerg extends AppCompatActivity implements ProtocoloData {

    String IDdelMenuParaCrearAlergenos;
    String SalidaAlergenosDispone;
    String AlergenosEnviar = "";
    String ResultadoBuscarAlergPorIDAlerg ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfilhostmirestmismenuscreaalerg);
        this.setTitle(R.string.perfilhost_mirestaurante_mimenu_crearalerg_textocabecera);
        Bundle bundle = getIntent().getExtras();

        final Button CrearAlergeno     = (Button) findViewById(R.id.perfil_host_mirest_vermenu_crearalerg_botoncreaalerg);
        final Button ActuaAlergeno     = (Button) findViewById(R.id.perfil_host_mirest_vermenu_crearalerg_botonactualizaalerg);

        if (bundle != null) {
            IDdelMenuParaCrearAlergenos = bundle.getString("IdMenuHostCreaAlerg");
        }

        BuscarAlergenosDispone AlergenoBuscado  = new BuscarAlergenosDispone();
        BuscarEnAlergenos      NombreAlergenos  = new BuscarEnAlergenos();
        final InsertarAlergenoHost   InsertarAlergeno = new InsertarAlergenoHost();
        final ActualizarLosAlergenos ActualizaAlerg   = new ActualizarLosAlergenos();

        try {
            SalidaAlergenosDispone = AlergenoBuscado.execute(IDdelMenuParaCrearAlergenos).get();
            if (SalidaAlergenosDispone.isEmpty()==false) {
                CheckBox gluten            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_1_gluten  );
                CheckBox crustaceo         = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_2_crustaceo);
                CheckBox huevos            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_3_huevos);
                CheckBox pescado           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_4_pescado);
                CheckBox cacahuetes        = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_5_cacahuetes);
                CheckBox soja              = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_6_soja);
                CheckBox lacteo            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_7_lacteo);
                CheckBox molusco           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_8_molusco);
                CheckBox apio              = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_9_apio);
                CheckBox mostaza           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_10_mostaza);
                CheckBox sesamo            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_11_sesamo);
                CheckBox sulfitos          = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_12_sulfitos);
                CheckBox altramuces        = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_13_altramuces);
                CheckBox frutascascara     = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_14_frutcascara);
                CrearAlergeno.setEnabled(false);
                ResultadoBuscarAlergPorIDAlerg = NombreAlergenos.execute(SalidaAlergenosDispone).get();
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
                CheckBox gluten            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_1_gluten  );
                CheckBox crustaceo         = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_2_crustaceo);
                CheckBox huevos            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_3_huevos);
                CheckBox pescado           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_4_pescado);
                CheckBox cacahuetes        = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_5_cacahuetes);
                CheckBox soja              = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_6_soja);
                CheckBox lacteo            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_7_lacteo);
                CheckBox molusco           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_8_molusco);
                CheckBox apio              = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_9_apio);
                CheckBox mostaza           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_10_mostaza);
                CheckBox sesamo            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_11_sesamo);
                CheckBox sulfitos          = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_12_sulfitos);
                CheckBox altramuces        = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_13_altramuces);
                CheckBox frutascascara     = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_14_frutcascara);
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
                ActualizaAlerg.execute(AlergenosEnviar,SalidaAlergenosDispone);
                ActuaAlergeno.setEnabled(false);
            }
        });

        CrearAlergeno.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View) {
                CheckBox gluten            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_1_gluten  );
                CheckBox crustaceo         = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_2_crustaceo);
                CheckBox huevos            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_3_huevos);
                CheckBox pescado           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_4_pescado);
                CheckBox cacahuetes        = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_5_cacahuetes);
                CheckBox soja              = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_6_soja);
                CheckBox lacteo            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_7_lacteo);
                CheckBox molusco           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_8_molusco);
                CheckBox apio              = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_9_apio);
                CheckBox mostaza           = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_10_mostaza);
                CheckBox sesamo            = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_11_sesamo);
                CheckBox sulfitos          = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_12_sulfitos);
                CheckBox altramuces        = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_13_altramuces);
                CheckBox frutascascara     = (CheckBox)findViewById(R.id.perfil_host_crea_alergeno_14_frutcascara);
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

    public class BuscarAlergenosDispone extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuCreaAlerg.this);
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
                String IdDelMenu = arg0[0];
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BDISPM+SP+IdDelMenu+CRLF).getBytes());
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

    public class InsertarAlergenoHost extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuCreaAlerg.this);
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
                os.write((IAM+SP+alergenos+"-"+IDdelMenuParaCrearAlergenos+CRLF).getBytes());
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

    public class ActualizarLosAlergenos extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuCreaAlerg.this);
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
                os.write((ACTALERGH+SP+alergenos+"-"+idalergeno+CRLF).getBytes());
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

    public class BuscarEnAlergenos extends AsyncTask<String, Void, String> {
        ProgressDialog pdia = null;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(PerfilHosteleroRestauranteVerMenuCreaAlerg.this);
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
                String IdAlergeno = arg0[0];
                String [] camps = IdAlergeno.split(" ");
                InetSocketAddress direccion = new InetSocketAddress(Direccion_IP,Puerto_TCP);
                cliente = new Socket();
                cliente.connect(direccion);
                BufferedReader bis = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                OutputStream os = cliente.getOutputStream();
                respuesta = bis.readLine();
                Log.d("Saludo", respuesta);
                os.write((BA+SP+IdAlergeno+CRLF).getBytes());
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
