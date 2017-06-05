package com.example.felix.appalimentacio.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.example.felix.appalimentacio.Adapter.ListCompraAdapter;
import com.example.felix.appalimentacio.Adapter.RequestHandler;
import com.example.felix.appalimentacio.Model.ItemListCompraModel;
import com.example.felix.appalimentacio.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class activity_veureLlistaCompra extends AppCompatActivity {
String us;
    String startDate;
    String endDate;
    int idLlista;
    String idIng;
    String checkedB;

    ArrayList<String> ingredientsLlista = new ArrayList<String>();
    ListView veureLlistaCompra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veure_llista_compra);
        us=getIntent().getExtras().getString("userConectat");
        idLlista = Integer.parseInt(getIntent().getExtras().getString("idLlista"));
        startDate=getIntent().getExtras().getString("startDate");
        endDate=getIntent().getExtras().getString("endDate");
        veureLlistaCompra = (ListView)findViewById(R.id.listVeureLListaCompra);
         dadesIngredientsLlista();
    }


    public void dadesIngredientsLlista() {

        Thread tr = new Thread() {

            @Override
            public void run() {
                final ArrayList<String> dtrr = ingredientsLlista();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!dtrr.get(0).equals("incorrecte")) {
                            ingredientsLlista = dtrr;
                          mostrarLlistaCompra();
                        }


                    }
                });

            }
        };
        tr.start();
    }

    public ArrayList<String> ingredientsLlista() {

        ArrayList<String> dta = new ArrayList<String>();
        URL url;
        int resposta = 0;
        StringBuilder resul = null;

        try {

            url = new URL("http://alimentacionapp.com/dadesIngredientsLlista.php?idLlista=" + idLlista);

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

    public void mostrarLlistaCompra(){
        ArrayList<ItemListCompraModel> data = new ArrayList<ItemListCompraModel>();
        ItemListCompraModel item = new ItemListCompraModel();
        for(int i = 0; i<ingredientsLlista.size(); i++){
            final String[] dadesIngLl = ingredientsLlista.get(i).split("/");
            String[] dadesIng = dadesIngLl[1].split(":");
            Boolean boolCheck=false;
            if(dadesIngLl[2].equals("1")) {
                boolCheck = true;
            }
            item = new ItemListCompraModel(dadesIng[0],dadesIng[2], boolCheck);
            data.add(item);
            ListView listview = (ListView)findViewById(R.id.listVeureLListaCompra);
            ListCompraAdapter adapter = new ListCompraAdapter(this, data, R.layout.list_compra_item);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean checked=true;
                    CheckBox checkboxIng =(CheckBox) view.findViewById(R.id.checkboxIngredient);
                    if(checkboxIng.isChecked()){
                        checked=false;
                    }
                    checkboxIng.setChecked(checked);
                    String[] dadesIngLl1 = ingredientsLlista.get(position).split("/");
                    //checkedIngredient(dadesIngLl[0],checked);
                    idIng=dadesIngLl1[0];
                    checkedB=String.valueOf(checked);
                   cambiarCheckboxIngredient();
                }
            });
           // listview.setOnItemClickListener(this);

        }
    }

    private void checkedIngredient(final String idIng, final boolean checked) {
        class CheckedIngredient extends AsyncTask<Void, String, String> {
            RequestHandler rh = new RequestHandler();


            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                HashMap<String,String> data1 = new HashMap<>();
                data1.put("id", idIng);
                data1.put("check",String.valueOf(checked));
                result = rh.sendPostRequest("http://alimentacionapp.com/checkIngredient.php",data1);

                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("correcta")){

                }


            }
        }
        CheckedIngredient ui = new CheckedIngredient();
        ui.execute();
    }


    public String cambiarCheckbox( ) {

        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int res = 0;


        try {
            // url = new URL("http://10.0.2.2/afegirRecepta/index.php?username="+us+"&nomRecepta="+nom+"&descripcio="+descripcio+"&instruccio="+instruccio+"&personas="+personas+"&visible="+visible);
            url = new URL("http://alimentacionapp.com/checkIngredient?id="+idIng+"&check="+checkedB);
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

    public void cambiarCheckboxIngredient( ){


        Thread tr = new Thread(){

            @Override
            public void run() {
                final String resultatC = cambiarCheckbox();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  int r = dadesJSON(resultatC);
                        //if (r > 0) {
                        if(resultatC.equals("correcta")){

                        }
                    }
                });

            }
        };

        tr.start();
    }


}