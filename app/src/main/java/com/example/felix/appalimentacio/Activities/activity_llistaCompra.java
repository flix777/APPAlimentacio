package com.example.felix.appalimentacio.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.felix.appalimentacio.R;
import com.example.felix.appalimentacio.Adapter.RequestHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class activity_llistaCompra extends AppCompatActivity {
ArrayList<String> eventsUser = new ArrayList<String>();
    ArrayList<String> llistesUser= new ArrayList<String>();
ArrayList<Integer> idsRecepta= new ArrayList<Integer>();
    ArrayList<String> receptesLlista= new ArrayList<String>();
    ArrayList<String> ingredientsLlista= new ArrayList<String>();
    String us;
    int year, month, day;
    String strStartDate;
    String strEndDate;
    Calendar calendar;
    TextView textDate1;
    TextView textDate2;
ListView listviewLlistes;
    EditText textNomLlista;
    ProgressDialog loading;
    String nomLlista;
    int idLlista;
    int wait=0;
    boolean prDades=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_compra);
        us=getIntent().getExtras().getString("userConectat");
listviewLlistes = (ListView)findViewById(R.id.listLlistaCompra);
        textDate1 = (TextView) findViewById(R.id.textDate1);
        textDate2 = (TextView) findViewById(R.id.textDate2);
textNomLlista=(EditText) findViewById(R.id.nomLlista);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        dadesEvents();
        dadesLlistes();
    }

    public void receptesIngredientsLlista(){
        String sD="";
        String eD="";
        for(int i = 0; i<llistesUser.size(); i++){
            String[] llistaUser1 = llistesUser.get(i).split(":");
            if(llistaUser1[3].equals(nomLlista)){
                 sD=llistaUser1[1];
                eD=llistaUser1[2];
                break;
            }
        }

            for(int j=0; j<eventsUser.size(); j++){
                String[] events= eventsUser.get(j).split(":");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date startDateLlista = null;

                try {
                    startDateLlista = simpleDateFormat.parse(sD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date endDateLlista = null;
                try {
                    endDateLlista = simpleDateFormat.parse(eD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date dateEvent = null;
                try {
                    dateEvent = simpleDateFormat.parse(events[1]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if((dateEvent.after(startDateLlista) || dateEvent.equals(startDateLlista) && (dateEvent.before(endDateLlista) || dateEvent.equals(endDateLlista)))) {

                    receptesLlista.add(events[0]);

                }

            }
            if(receptesLlista.size()>0){
                buscarIdsReceptes();

            }

        }


    @SuppressWarnings("deprecation")
    public void setDate1(View view) {
        showDialog(999);

    }

    @SuppressWarnings("deprecation")
    public void setDate2(View view) {
        showDialog(998);

    }

    public void dadesLlistes(){

        Thread tr = new Thread(){

            @Override
            public void run() {
                final ArrayList<String> dtrr=dadesLlistesUser();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!dtrr.get(0).equals("incorrecte")){
                            llistesUser=dtrr;

                            ArrayList<String> dadesLlista=new ArrayList<String>();
//  String[] dadesLlista=new String[llistesUser.size()];
                            for(int i=0; i<llistesUser.size();i++){
                                String[] dadesLlistesUser = llistesUser.get(i).split(":");
                                dadesLlista.add(dadesLlistesUser[3]+":  Desde "+dadesLlistesUser[1]+" hasta "+dadesLlistesUser[2]);
                                //                          dadesLlista[i]="Desde "+dadesLlistesUser[1]+" hasta"+dadesLlistesUser[2];
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity_llistaCompra.this, android.R.layout.simple_list_item_1,dadesLlista);
                            listviewLlistes.setAdapter(arrayAdapter);
                            listviewLlistes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    onClicItemLListesCompra(position);

                                }
                            });
if(prDades==true){
    receptesIngredientsLlista();
}


                        }


                    }
                });

            }
        };
        tr.start();
    }


    public void onClicItemLListesCompra(int position){
        String[] dates = llistesUser.get(position).split(":");
        Intent i = new Intent(this, activity_veureLlistaCompra.class);
        i.putExtra("userConectat", us);
        i.putExtra("idLlista", dates[0]);
        startActivity(i);
    }


    public ArrayList<String> dadesLlistesUser() {

        ArrayList<String> dta = new ArrayList<String>();
        URL url;
        int resposta = 0;
        StringBuilder resul = null;

        try {

            url = new URL("http://alimentacionapp.com/dadesLListaCompra.php?username="+us);

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
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener1, year, month, day);
        }
        if (id == 998) {
            return new DatePickerDialog(this,
                    myDateListener2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener1 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    strStartDate=String.valueOf(arg3)+"/"+String.valueOf(arg2)+"/"+String.valueOf(arg1);
                    textDate1.setText(strStartDate);
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                 //   showDate(arg1, arg2+1, arg3);
                }
            };
    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    strEndDate=String.valueOf(arg3)+"/"+String.valueOf(arg2)+"/"+String.valueOf(arg1);
                    textDate2.setText(strEndDate);
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    //   showDate(arg1, arg2+1, arg3);
                }
            };


    public void onClicInsertarLLista(View view){
        nomLlista=textNomLlista.getText().toString();
        int cont =llistesUser.size();
        insertLLista();
        prDades=true;
        dadesLlistes();

    }

    private void insertLLista() {
        class InsertLLista extends AsyncTask<Void, String, String> {
            RequestHandler rh = new RequestHandler();


            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                HashMap<String,String> data1 = new HashMap<>();
                data1.put("user", us);
                data1.put("startDate",strStartDate);
                data1.put("endDate", strEndDate);
                data1.put("nomLlista", nomLlista);
                result = rh.sendPostRequest("http://alimentacionapp.com/insertLListaCompra.php",data1);

                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity_llistaCompra.this, "Creando Lista", "Please wait...", true, true);


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("correcta")){
wait++;
                }


            }
        }
        InsertLLista ui = new InsertLLista();
        ui.execute();
    }



    private void insertIngredientsLLista() {
        class InsertIngredientsLLista extends AsyncTask<Void, String, String> {

            RequestHandler rh = new RequestHandler();

            @Override
            protected String doInBackground(Void... params) {
String idLlista = null;
                String result = null;
                for(int i = 0; i<llistesUser.size(); i++){
                    String[] llistaUser1 = llistesUser.get(i).split(":");
                    if(llistaUser1[3].equals(nomLlista)){
                        idLlista=llistaUser1[0];
                    }
                }
for(int j=0; j<ingredientsLlista.size();j++){
    HashMap<String,String> data1 = new HashMap<>();
    data1.put("ingredient", ingredientsLlista.get(j));
    data1.put("idLlista", idLlista);
    result = rh.sendPostRequest("http://alimentacionapp.com/insertIngredientLlista.php",data1);
}
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                loading.dismiss();

            }
        }
        InsertIngredientsLLista ui = new InsertIngredientsLLista();
        ui.execute();
    }

    public void dadesEvents() {

        Thread tr = new Thread() {

            @Override
            public void run() {
                final ArrayList<String> dtrr = dadesEventsUser();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!dtrr.get(0).equals("incorrecte")) {
                            eventsUser = dtrr;
                        }


                    }
                });

            }
        };
        tr.start();
    }

    public ArrayList<String> dadesEventsUser() {

        ArrayList<String> dta = new ArrayList<String>();
        URL url;
        int resposta = 0;
        StringBuilder resul = null;

        try {

            url = new URL("http://alimentacionapp.com/dadesEvents.php?username=" + us);

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

    private void buscarIdsReceptes() {
        class BuscarIdsRceptes extends AsyncTask<Void, String, String> {

            // ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected String doInBackground(Void... params) {

                String result = "";

                for(int i = 0; i<receptesLlista.size();i++) {
                    HashMap<String, String> data1 = new HashMap<>();
                    data1.put("username", us);
                    data1.put("nomrecepta", receptesLlista.get(i));
                    result = rh.sendPostRequest("http://alimentacionapp.com/buscarIdReceptaP.php", data1);
                    if(comprobarStringNum(result)){
                        idsRecepta.add(Integer.parseInt(result));
                    }

                }

                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //    loading = ProgressDialog.show(activity_afegirRecepta.this, "Guardando Receta", "Please wait...", true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                buscarIngredientsRecepta();
            }

        }
        BuscarIdsRceptes ui = new BuscarIdsRceptes();
        ui.execute();
    }

    public boolean comprobarStringNum(String string){
        int cpr;
        try{
            cpr = Integer.parseInt(string);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    private void buscarIngredientsRecepta() {
        class BuscarIdsRceptes extends AsyncTask<Void, String, String> {

            // ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                for(int i = 0; i<idsRecepta.size();i++) {
                    HashMap<String, String> data1 = new HashMap<>();
                    data1.put("idrecepta", String.valueOf(idsRecepta.get(i)));
                    // String result = rh.sendPostRequest("http://10.0.2.2/afegirRecepta/insertarImatge.php",data1);
                    result = rh.sendPostRequest("http://alimentacionapp.com/buscarIngredientsRecepta.php", data1);
                    if (result != null) {
                        String[] arResult = result.split("/");
                        for(int k=0;k<arResult.length; k++){
                            ingredientsLlista.add(arResult[k]);
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //    loading = ProgressDialog.show(activity_afegirRecepta.this, "Guardando Receta", "Please wait...", true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                insertIngredientsLLista();


            }
        }
        BuscarIdsRceptes ui = new BuscarIdsRceptes();
        ui.execute();
    }

}
