package com.example.felix.appalimentacio.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.felix.appalimentacio.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class activity_login extends AppCompatActivity {
    EditText user, password;
    String userc=" ";
    TextView resultat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.pass);

        resultat=(TextView)findViewById(R.id.resultat);

    }

    public void onClickRegistre(View view){
        Intent i = new Intent(this, activity_registre.class);
        startActivity(i);
    }

    public void onClickLogin(View view){
        final String us=user.getText().toString();
        final String pass=password.getText().toString();

        Thread tr = new Thread(){

            @Override
            public void run() {
                final String resultatC = login(us, pass);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                          if(resultatC.equals("correcta")){
                              entrarMenu();

                        } else {
                            resultat.setText("Login Incorrecta");
                        }
                    }
                });

            }
        };
    tr.start();

    }

    public void entrarMenu(){
        Intent i = new Intent(this, activity_menu.class);
        i.putExtra("userConectat",userc);
        startActivity(i);
    }



    public String login(String us, String pass ) {

        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int res = 0;

        try {
           // url = new URL("http://10.0.2.2/ComprovarLogin/index.php?username="+us+"&password="+pass);
            url = new URL("http://alimentacionapp.com/login.php?username="+us+"&password="+pass);
            HttpURLConnection connexio = (HttpURLConnection)url.openConnection();
            connexio.setDoInput(true);
            resposta = connexio.getResponseCode();

            resul = new StringBuilder();
            if (resposta == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connexio.getInputStream()));
                String line;

                for(int i=0;i<2;i++){
                    if((line = br.readLine()) != null){
                        if(i==0){
                            resul.append(line);
                        }
                        if(i==1){
                            userc=line;
                        }
                    }
                }


                br.close();
            }
        } catch (Exception e) {        }


return resul.toString();


    }


}
