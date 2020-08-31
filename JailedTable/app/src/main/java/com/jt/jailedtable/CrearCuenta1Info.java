package com.jt.jailedtable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CrearCuenta1Info extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearcuenta1_info);
        this.setTitle(R.string.crearcuenta1info_textocabecera);

        ImageButton botonregreso = (ImageButton) findViewById(R.id.crearcuenta1info_boton);

        botonregreso.setOnClickListener(new View.OnClickListener() {
            public void onClick(View View){
                Toast.makeText(getApplicationContext(), "Volviendo atrás.", Toast.LENGTH_SHORT).show();
                Intent f = new Intent(CrearCuenta1Info.this, CrearCuenta1.class);
                startActivity(f);
            }
        });
    }
}
