package com.example.felix.appalimentacio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.felix.appalimentacio.R;

public class activity_menu extends AppCompatActivity {
String userc=" ";
    TextView usermc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        userc=getIntent().getExtras().getString("userConectat");
        usermc=(TextView)findViewById(R.id.usermC);
        usermc.setText(userc);
    }


    public void onClicMRecetas(View view){
        Intent i = new Intent(this, activity_receptas.class);
        i.putExtra("userConectat", userc);
        startActivity(i);
    }

    public void onClicAfegirRecetas(View view){
        Intent i = new Intent(this, activity_afegirRecepta.class);
        i.putExtra("userConectat", userc);
        startActivity(i);
    }
    public void onClicCataleg(View view){
        Intent i = new Intent(this, activity_cataleg.class);
        i.putExtra("userConectat", userc);
        startActivity(i);
    }

    public void onClicPerfil(View view){
        Intent i = new Intent(this, activity_perfil.class);
        i.putExtra("userConectat", userc);
        startActivity(i);
    }

    public void onClicCalendari(View view){
        Intent i = new Intent(this, activity_calendar.class);
        i.putExtra("userConectat", userc);
        startActivity(i);
    }

    public void onClicLListaCompra(View view){
        Intent i = new Intent(this, activity_llistaCompra.class);
        i.putExtra("userConectat", userc);
        startActivity(i);
    }

}
