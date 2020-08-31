package com.jt.jailedtable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CrearCuenta1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearcuenta1);
        this.setTitle(R.string.crearcuenta1_texto);

        ImageButton botonduda = (ImageButton) findViewById(R.id.crearcuenta1_info);
        Button botoncrearcuenta1 = (Button) findViewById(R.id.crearcuenta1_boton1);
        final CheckBox checkBoxComensal = (CheckBox) findViewById(R.id.crearcuenta1_cbox_1);
        final CheckBox checkBoxHostelero = (CheckBox) findViewById(R.id.crearcuenta1_cbox_2);

        botonduda.setOnClickListener(new View.OnClickListener() {
        public void onClick(View View){
        Toast.makeText(getApplicationContext(), "Información.", Toast.LENGTH_SHORT).show();
        Intent e = new Intent(CrearCuenta1.this, CrearCuenta1Info.class);
        startActivity(e);
        }
        });

        botoncrearcuenta1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View){
                if (checkBoxComensal.isChecked() && checkBoxHostelero.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "Seleccionado Comensal", Toast.LENGTH_SHORT).show();
                    Intent g = new Intent(CrearCuenta1.this, CrearComensal.class);
                    startActivity(g);
                } if(checkBoxHostelero.isChecked() && checkBoxComensal.isChecked() == false) {
                    Toast.makeText(getApplicationContext(), "Seleccionado Hostelero.", Toast.LENGTH_SHORT).show();
                    Intent h = new Intent(CrearCuenta1.this, CrearHostelero.class);
                    startActivity(h);
                } if (checkBoxComensal.isChecked() && checkBoxHostelero.isChecked()){
                    Toast.makeText(getApplicationContext(), "Debe elegir solo una opción.", Toast.LENGTH_SHORT).show();
                } if(checkBoxComensal.isChecked() == false && checkBoxHostelero.isChecked() == false){
                    Toast.makeText(getApplicationContext(), "Debe elegir una opción.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
