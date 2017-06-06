package com.example.felix.appalimentacio.Activities;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.felix.appalimentacio.R;
import com.example.felix.appalimentacio.Adapter.RequestHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_veureRecepta extends AppCompatActivity {
    String resultF = null;
    ArrayList<String> dadesIng;
    String us="";
    String nRecepta="";
    int idrepceta=-1;
    TextView trecepta, drecepta, irecepta;
    ImageView imageRec;
    TableLayout table;
    LinearLayout list;

    TextView cal, pr, gr, hc, h20, cen;

    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = ProgressDialog.show(activity_veureRecepta.this, "Mostrando Receta", "Please wait...", true, true);
        setContentView(R.layout.activity_veure_recepta);
        trecepta=(TextView)findViewById(R.id.nomrecepta);
        drecepta=(TextView)findViewById(R.id.desrecepta);
        irecepta=(TextView)findViewById(R.id.instrecepta);
        imageRec = (ImageView)findViewById(R.id.imgVeureR);
        table = (TableLayout) findViewById(R.id.taulaIng);
        list = (LinearLayout)findViewById(R.id.veureIngredients);
        us=getIntent().getExtras().getString("userConectat");
        nRecepta=getIntent().getExtras().getString("nomRecepta");
        mostrarRecepta();

    }

    public void mostrarIngredients(){

        for(int i=0;i<dadesIng.size();i++) {

            View viewIng = getLayoutInflater().inflate(R.layout.veure_ingredients_recepta, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0, 30, 0, 0);

            list.addView(viewIng, layoutParams);
            String[] dadesS = dadesIng.get(i).split(":");
            TextView nomI = (TextView) viewIng.findViewById(R.id.nomIng);
            nomI.setText(dadesS[0]);
            TextView estatI = (TextView) viewIng.findViewById(R.id.estatIng);
            estatI.setText(dadesS[1]);
            TextView canitatI = (TextView) viewIng.findViewById(R.id.cantitatIng);
            canitatI.setText(dadesS[2]);

        }
    }

    public void mostrarTaula(){
        int NUM_COLS=3;
        int NUM_ROWS=dadesIng.size();

if(dadesIng.size()>0) {


    for (int i = 0; i < NUM_ROWS; i++) {
        TableRow tableRow = new TableRow(this);
        TableRow.LayoutParams layoutP = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutP.setMargins(0,0,2,0);
        table.addView(tableRow);
        String[] dadesS = dadesIng.get(i).split(":");

        for (int j = 0; j < NUM_COLS; j++) {

            TextView text = new TextView(this);
            text.setText(dadesS[j]);

            tableRow.addView(text);

        }
    }
}

    }



    public void mostrarComposicio(){

        String[] dadesC = resultF.split("/");
        ArrayList<Float> composicioFinal = new ArrayList<Float>();

        int numIng = dadesC.length;

        for(int i=0; i<numIng; i++){
            String[]dIng = dadesIng.get(i).split(":");
            float cant = Float.parseFloat(dIng[2]);
            String[] dadesCC = dadesC[i].split(":");
            for(int k=0; k<dadesCC.length; k++){
                if(i==0){
                    composicioFinal.add(k, (Float.parseFloat(dadesCC[k])/100)*cant);

                }else{

                    composicioFinal.set(k, composicioFinal.get(k)+(Float.parseFloat(dadesCC[k])/100)*cant);
                }
            }
        }

        cal=(TextView)findViewById(R.id.cal);
        cal.setText(String.valueOf(composicioFinal.get(0)));
        pr=(TextView)findViewById(R.id.pr);
        pr.setText(String.valueOf(composicioFinal.get(1)));
        gr=(TextView)findViewById(R.id.gr);
        gr.setText(String.valueOf(composicioFinal.get(2)));
        hc=(TextView)findViewById(R.id.hc);
        hc.setText(String.valueOf(composicioFinal.get(3)));
        h20=(TextView)findViewById(R.id.h20);
        h20.setText(String.valueOf(composicioFinal.get(4)));
        cen=(TextView)findViewById(R.id.cen);
        cen.setText(String.valueOf(composicioFinal.get(5)));

    }

    public void mostrarRecepta(){

            Thread tr = new Thread(){

            @Override
            public void run() {
                final String[] dadesr= dadesRecepta(us, nRecepta);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  int r = dadesJSON(resultatC);
                        //if (r > 0) {
                        if(dadesr[0].equals("incorrecte")){


                        } else {
                            idrepceta = Integer.parseInt(dadesr[0]);
                            trecepta.setText(dadesr[2]);
                            drecepta.setText(dadesr[3]);
                            irecepta.setText(dadesr[4]);
                            if(dadesr[5]!=null) {
                                byte[] decodedBytes = Base64.decode(dadesr[5], 0);
                                imageRec.setImageBitmap(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
                            }
                            dadesIngredientsRecepta();

                        }
                    }
                });

            }
        };
        tr.start();
    }


    public String[] dadesRecepta(String us, String nRecepta ) {

        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int res = 0;
        String dades[]=new String[6];

        try {

         //   url = new URL("http://10.0.2.2/veureRecepta/index.php?username="+us+"&nomRecepta="+nRecepta);
            url = new URL(   "http://alimentacionapp.com/veureRecepta.php?username="+us+"&nomRecepta="+nRecepta);

            HttpURLConnection connexio1 = (HttpURLConnection)url.openConnection();

            resposta = connexio1.getResponseCode();

         if (resposta == HttpURLConnection.HTTP_OK) {


             BufferedReader br = new BufferedReader(new InputStreamReader(connexio1.getInputStream()));

             String line;


             int k = 0;
             while ((line = br.readLine()) != null){
                 if(k<5){
                     dades[k]= line;
                 }else{
                     if(k == 5) {
                         dades[5] = line;
                     }else{
                         dades[5] += line;
                     }
                 }

                 k++;
             }

                br.close();
            }
        } catch (Exception e) {        }


        return dades;



    }


    public void dadesIngredientsRecepta(){



        Thread tr = new Thread(){

            @Override
            public void run() {

               dadesIng=ingredientsRecepta();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(dadesIng.size()>0) {
                            mostrarIngredients();
                            dadesComposicioIng();
                        }else{
                            LinearLayout layout = (LinearLayout)findViewById(R.id.composicioRecepta);
                            layout.removeAllViews();
                            loading.dismiss();
                        }
                    }
                });
            }
        };
        tr.start();


    }

    public ArrayList<String> ingredientsRecepta() {

        ArrayList<String> dta = new ArrayList<String>();
        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int cont = 0;
        List<Map<String, String>> data3 = new ArrayList<Map<String, String>>();
        try {

            //  url = new URL("http://10.0.2.2/veureMReceptes/?username="+us);
            url = new URL(   "http://alimentacionapp.com/dadesIngredientsRecepta.php?idrecepta="+idrepceta);

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


    private void dadesComposicioIng() {
        class dadesComposicioIngredients extends AsyncTask<Void, String, String> {

           // ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                ;
                for(int i = 0; i<dadesIng.size();i++){
                    String[] dadesS = dadesIng.get(i).split(":");
                    HashMap<String,String> data1 = new HashMap<>();
                    data1.put("aliment", dadesS[0]);
                    data1.put("estat", dadesS[1]);
                    // String result = rh.sendPostRequest("http://10.0.2.2/afegirRecepta/insertarImatge.php",data1);
                    result = rh.sendPostRequest("http://alimentacionapp.com/composicioIngredient.php",data1);
                   if(resultF==null){
                       resultF=result;
                   }else{
                       resultF= resultF +"/"+result;
                   }
                }

                return resultF;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mostrarComposicio();
                loading.dismiss();
             //   if(s.equals("correcte")){
               //     Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
               // }
            }
        }
        dadesComposicioIngredients ui = new dadesComposicioIngredients();
        ui.execute();
    }



}
