package com.example.felix.appalimentacio.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.felix.appalimentacio.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class activity_registre extends AppCompatActivity {
TextView user, password, mail, nom, resultat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre);
        user=(EditText)findViewById(R.id.ruser);
        password=(EditText)findViewById(R.id.rpass);
        mail=(EditText)findViewById(R.id.rcorreu);
        nom=(EditText)findViewById(R.id.rnom);

        resultat=(TextView)findViewById(R.id.rresultat);
    }

    public void onClickRegistreConta(View view){
        final String us=user.getText().toString();
        final String pass=password.getText().toString();
        final String email=mail.getText().toString();
        final String nomc=nom.getText().toString();

        Thread tr = new Thread(){

            @Override
            public void run() {
                final String resultatC = registre(us, pass, email, nomc );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(resultatC.equals("correcta")){
                            resultat.setText("Conta Creada");
                        } else {
                            resultat.setText("No s'ha pogut crear la conta");
                        }
                    }
                });

            }
        };
        tr.start();

    }

    public String registre(String us, String pass, String mail, String nom ) {

        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int res = 0;

        try {
         //   url = new URL("http://10.0.2.2/Registre/index.php?username="+us+"&password="+pass+"&correu="+mail+"&nom="+nom);
            url = new URL("http://alimentacionapp.com/registre.php?username="+us+"&password="+pass+"&correu="+mail+"&nom="+nom);

            HttpURLConnection connexio = (HttpURLConnection)url.openConnection();
            resposta = connexio.getResponseCode();

            resul = new StringBuilder();
            if (resposta == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connexio.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    resul.append(line);

                }
                br.close();
            }
        } catch (Exception e) {        }


        return resul.toString();


    }



}
