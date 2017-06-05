package com.example.felix.appalimentacio.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.felix.appalimentacio.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_cataleg extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String us;
    TextView textv;

    List<Map<String, String>> data2 = new ArrayList<Map<String, String>>();;
    ListView listView;
    ArrayList<String> nomRec= new ArrayList<String>();
    ArrayList<String> dtrr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cataleg);
        us=getIntent().getExtras().getString("userConectat");
        textv = (TextView)findViewById(R.id.pr2);
        listView = (ListView)findViewById(R.id.list2);
        mostrarMRecepta();
    }

    public void crearAdapter(List<Map<String, String>> dataf){
        SimpleAdapter adapter = new SimpleAdapter(this,dataf, android.R.layout.simple_list_item_2,  new String[]{"nom", "descripcio"}, new int[]{android.R.id.text1,android.R.id.text2});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    public void mostrarMRecepta(){

        Thread tr = new Thread(){

            @Override
            public void run() {
                dtrr=dadesMReceptes();
                for (int i=0; i< dtrr.size(); i++) {

                    String[] nomdes = dtrr.get(i).split(":");
                    Map<String, String> datum=new HashMap<String, String>(2);
                    datum.put("nom",nomdes[0]);
                    datum.put("descripcio", nomdes[1]);
                    data2.add(datum);
                }

                for (int i=0; i< dtrr.size(); i++) {
                    String[] nomdes = dtrr.get(i).split(":");
                    nomRec.add(nomdes[0]);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        crearAdapter(data2);

                    }
                });

            }
        };
        tr.start();


    }

    public ArrayList<String> dadesMReceptes() {

        ArrayList<String> dta = new ArrayList<String>();
        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int cont = 0;
        List<Map<String, String>> data3 = new ArrayList<Map<String, String>>();
        try {

            //url = new URL("http://10.0.2.2/veureMReceptes/cataleg.php");
            url = new URL("http://alimentacionapp.com/cataleg.php");
            HttpURLConnection connexio1 = (HttpURLConnection) url.openConnection();

            resposta = connexio1.getResponseCode();

            if (resposta == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connexio1.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    dta.add(line);

                }

                br.close();
            }
        } catch (Exception e) {
        }
        return dta;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, activity_veureRecepta.class);
        i.putExtra("userConectat", us);
        i.putExtra("nomRecepta", nomRec.get(position));
        startActivity(i);
    }


}



