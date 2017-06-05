package com.example.felix.appalimentacio.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.felix.appalimentacio.R;

public class activity_perfil extends AppCompatActivity {
Spinner esport, genere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        esport = (Spinner)findViewById(R.id.sport);
        genere =(Spinner)findViewById(R.id.genere);
       final String[] array = getResources().getStringArray(R.array.genere);
        final String[] arrays = getResources().getStringArray(R.array.activitat);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array);
        genere.setAdapter(arrayAdapter);
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arrays);
        esport.setAdapter(arrayAdapter1);


    }
}
