package com.example.felix.appalimentacio.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
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

public class activity_calendar extends AppCompatActivity {

    int yearS;
    int monthS;
    int dayOfMonthS;
    TextView events;
    String[] mreceptes;
    String us;
    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> eventsUser;

CalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        us=getIntent().getExtras().getString("userConectat");
        events = (TextView)findViewById(R.id.eventsCalendar) ;
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.afegirEventRecepta);
        calendarView = (CalendarView)findViewById(R.id.calendar);
        dadesEvents();
        receptesAutoComplete();

calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

    yearS=year;
    monthS=month;
    dayOfMonthS=dayOfMonth;
        if(eventsUser!=null){
    events.setText("");
    for(int i = 0; i<eventsUser.size(); i++){
        String[] aEvents = eventsUser.get(i).split(":");
        String[] date = aEvents[1].split("/");
        if(yearS == Integer.valueOf(date[2]) && monthS==Integer.valueOf(date[1]) && dayOfMonthS==Integer.valueOf(date[0])){
            events.setText(events.getText()+"\r\n"+aEvents[0]);

        }
    }
}

    }


});
    }

    public void receptesAutoComplete(){

        Thread tr = new Thread(){

            @Override
            public void run() {
                ArrayList<String> dtrr=dadesMReceptes();
                final ArrayList<String> dadesr = new ArrayList<>();
                for (int i=0; i< dtrr.size(); i++) {
                    String[] nomdes = dtrr.get(i).split(":");
                    dadesr.add(nomdes[0]);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                      ArrayAdapter adapterI = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1, dadesr);
                        autoCompleteTextView.setAdapter(adapterI);

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

            url = new URL("http://alimentacionapp.com/misRecetas.php?username="+us);

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
        //return data3;

    }


    public void dadesEvents(){

        Thread tr = new Thread(){

            @Override
            public void run() {
                final ArrayList<String> dtrr=dadesEventsUser();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                if(!dtrr.get(0).equals("incorrecte")){
                    eventsUser=dtrr;
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

            url = new URL("http://alimentacionapp.com/dadesEvents.php?username="+us);

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

    public void onClicInsertarEvent(View view){insertEvent();}

    private void insertEvent() {
        class InsertEvent extends AsyncTask<Void, String, String> {

             ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            String textAutoComplete = autoCompleteTextView.getText().toString();
            String dateEvent = dayOfMonthS+"/"+monthS+"/"+yearS;


            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                    HashMap<String,String> data1 = new HashMap<>();
                    data1.put("user", us);
                    data1.put("recepta",textAutoComplete);
                    data1.put("data", dateEvent);
                    result = rh.sendPostRequest("http://alimentacionapp.com/insertEvent.php",data1);

                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity_calendar.this, "Guardando Evento", "Please wait...", true, true);


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                events.setText(events.getText()+"\r\n"+textAutoComplete);
                dadesEvents();
                loading.dismiss();
                //   if(s.equals("correcte")){
                //     Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                // }
            }
        }
        InsertEvent ui = new InsertEvent();
        ui.execute();
    }


}
