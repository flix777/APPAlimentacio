package com.example.felix.appalimentacio.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.felix.appalimentacio.Adapter.RequestHandler;
import com.example.felix.appalimentacio.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class activity_afegirRecepta extends AppCompatActivity {

    private static int SELECT_PICTURE = 300;
    static final int REQUEST_IMAGE_CAPTURE = 1;
EditText eNom, eDescricio, eInstruccio, ePersonas;
    String us;
    TextView resultatt;
    ImageView image;
    Bitmap bmp;
    String base64Image;
    String nom;
    Switch switchvisible;
    private static final int SELECT_FILE = 1;
    int contView=3;
    ArrayAdapter adapterI;

    LinearLayout list;
    Map<Integer, View> map = new HashMap<Integer, View>();
    Map<Integer, Spinner> mapSpinner = new HashMap<Integer, Spinner>();
    Map<Integer, EditText> mapET = new HashMap<Integer, EditText>();
    Map<Integer, AutoCompleteTextView> mapAT = new HashMap<Integer, AutoCompleteTextView>();
    ArrayAdapter adapterSp;

    int idrecepta=-1;
    ArrayAdapter adapter;
    static ArrayList dadesr = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir_recepta);

        eNom=(EditText)findViewById(R.id.nRecepta);
        eDescricio = (EditText)findViewById(R.id.dRecepta);
        eInstruccio = (EditText)findViewById(R.id.iRecepta);
        ePersonas = (EditText)findViewById(R.id.pRecepta);
        us=getIntent().getExtras().getString("userConectat");
        resultatt = (TextView) findViewById(R.id.resultatAfegir);
        image = (ImageView)findViewById(R.id.mostrarimg);
        switchvisible = (Switch)findViewById(R.id.switchVis);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 30, 0, 0);

        list = (LinearLayout)findViewById(R.id.Container);
        map.put(1, getLayoutInflater().inflate(R.layout.activity_afegir_ingredient, null));
        list.addView(map.get(1), layoutParams);
        mapAT.put(1, (AutoCompleteTextView) map.get(1).findViewById(R.id.autoCompleteTipusAf));
        mapET.put(1,(EditText) map.get(1).findViewById(R.id.editpesAf));
        mapSpinner.put(1, (Spinner) map.get(1).findViewById(R.id.spinnerEstadoAf));
        textCanviat(mapAT.get(1), mapSpinner.get(1));

        map.put(2, getLayoutInflater().inflate(R.layout.activity_afegir_ingredient, null));
        list.addView(map.get(2), layoutParams);
        mapAT.put(2, (AutoCompleteTextView) map.get(2).findViewById(R.id.autoCompleteTipusAf));
        mapET.put(2,(EditText) map.get(2).findViewById(R.id.editpesAf));
        mapSpinner.put(2, (Spinner) map.get(2).findViewById(R.id.spinnerEstadoAf));
        textCanviat(mapAT.get(2), mapSpinner.get(2));

        exportIngredients();

    }

    public void textCanviat(final AutoCompleteTextView autoCT, final Spinner spinner){
        autoCT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                nom = autoCT.getText().toString().replace(" ", "_");
                exportarEstats(nom, spinner);


            }
        });
    }

    public void afegirContainerIngredient(View view){

        map.put(contView, getLayoutInflater().inflate(R.layout.activity_afegir_ingredient, null));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 30, 0, 0);

        list.addView(map.get(contView), layoutParams);

        mapAT.put(contView, (AutoCompleteTextView) map.get(contView).findViewById(R.id.autoCompleteTipusAf));
        ArrayAdapter adapter = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1, dadesr);
        mapAT.get(contView).setAdapter(adapter);
        //textCanviat(mapAT.get(contView));

        mapET.put(contView,(EditText) map.get(contView).findViewById(R.id.editpesAf));

        mapSpinner.put(contView, (Spinner) map.get(contView).findViewById(R.id.spinnerEstadoAf));
        textCanviat(mapAT.get(contView), mapSpinner.get(contView));
        contView++;

    }


public String buscarIdRecepta(String us, String nRecepta ) {

    URL url;
    String linea;
    int resposta = 0;
    StringBuilder resul = null;
    int res = 0;
    String dades="";

    try {

        //   url = new URL("http://10.0.2.2/veureRecepta/index.php?username="+us+"&nomRecepta="+nRecepta);
        url = new URL("http://alimentacionapp.com/buscarIdRecepta.php?username="+us+"&nomRecepta="+nRecepta);

        HttpURLConnection connexio1 = (HttpURLConnection)url.openConnection();
        resposta = connexio1.getResponseCode();
        if (resposta == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connexio1.getInputStream()));
            //String line;
     dades=br.readLine();

            br.close();
        }
    } catch (Exception e) {        }

    return dades;

}

    public void onClickAfegirReceptaF(View view){

        nom = eNom.getText().toString();
        final String descripcio = eDescricio.getText().toString();
        final String instruccio = eInstruccio.getText().toString();
        final String personas = ePersonas.getText().toString();
        final String visible;
        if(switchvisible.isChecked()){
             visible = "true";
        }else{
            visible = "false";        }


        Thread tr = new Thread(){

            @Override
            public void run() {
                final String resultatC = afegirRecepta(us, nom, descripcio, instruccio, personas, visible );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  int r = dadesJSON(resultatC);
                        //if (r > 0) {
                        if(resultatC.equals("correcta")){
                          if(bmp!=null) {
                              uploadImage();

                          }
                            while(idrecepta==-1){
                                buscarID(us, nom);
                            }
                            insertIng();
                            resultatt.setText("Recepta Registrada");
                        } else {
                            resultatt.setText("No s'ha pogut crear la recepta");
                        }
                    }
                });

            }
        };

        tr.start();
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


    public void buscarID(final String usuari2, final String nomRecepta2){
        Thread tr = new Thread(){

            @Override
            public void run() {

final String id1 = buscarIdRecepta(usuari2, nomRecepta2);
if(comprobarStringNum(id1)==true){
    idrecepta = Integer.parseInt(id1);
}

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  int r = dadesJSON(resultatC);
                        //if (r > 0) {
                        if(idrecepta==-1){
                            resultatt.setText("No s'ha pogut crear la recepta");
                         //   buscarID(usuari2, nomRecepta2);

                        } else {
                            resultatt.setText("Recepta Registrada");
                        }
                    }
                });

            }
        };
        tr.start();
    }



/*
    protected void onPhotoTaken() {
        // Log message
        Log.i("SonaSys", "onPhotoTaken");
        taken = true;
        imgCapFlag = true;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        bitmap = BitmapFactory.decodeFile(path, options);
        image.setImageBitmap(bitmap);


    }
    */


public void onClickImportar(View view){

final CharSequence[] opt = {"Tomar Foto", "Galeria", "Cancelar"};
    final AlertDialog.Builder builder = new AlertDialog.Builder(activity_afegirRecepta.this);
    builder.setTitle("Importar Imagen");
    builder.create();


    builder.setItems(opt, new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(opt[which] == "Tomar Foto"){

                Log.i("camera", "startCameraActivity()");
                File file = new File("/image");
                Uri outputFileUri = Uri.fromFile(file);
                Intent intent = new Intent(
                        android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(intent, 1);
            }
            if(opt[which] == "Galeria") {
                /*
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 2);
                */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Seleccione una imagen"),SELECT_FILE);
            }


            if(opt[which] == "Cancelar"){
            dialog.dismiss();
            }


        }
    });
    builder.show();
}



    public String afegirRecepta(String us, String nom, String descripcio, String instruccio, String personas, String visible ) {

        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int res = 0;


        try {
           // url = new URL("http://10.0.2.2/afegirRecepta/index.php?username="+us+"&nomRecepta="+nom+"&descripcio="+descripcio+"&instruccio="+instruccio+"&personas="+personas+"&visible="+visible);
            url = new URL("http://alimentacionapp.com/afegirRecepta.php?username="+us+"&nomRecepta="+nom+"&descripcio="+descripcio+"&instruccio="+instruccio+"&personas="+personas+"&visible="+visible);
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


    public String afegirIngredient(String aliment, String estat, float cantitat, int idRecepta ) {

        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul2 = null;
        int res = 0;


        try {
            // url = new URL("http://10.0.2.2/afegirRecepta/index.php?username="+us+"&nomRecepta="+nom+"&descripcio="+descripcio+"&instruccio="+instruccio+"&personas="+personas+"&visible="+visible);
            url = new URL("http://alimentacionapp.com/insertarIngredients.php?aliment="+aliment+"&estat="+estat+"&pes="+cantitat+"&id="+idRecepta);
            HttpURLConnection connexioi = (HttpURLConnection)url.openConnection();

            resposta = connexioi.getResponseCode();

            resul2 = new StringBuilder();
            if (resposta == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connexioi.getInputStream()));

                String line;
                while ((line = br.readLine()) != null) {

                    resul2.append(line);

                }
                br.close();
            }

        } catch (Exception e) {        }


        return resul2.toString();

    }


    public static int nearest2pow(int value) {
        return value == 0 ? 0
                : (32 - Integer.numberOfLeadingZeros(value - 1)) / 2;
    }

    public Bitmap scaleImage(BitmapFactory.Options options, Uri uri,
                             int targetWidth) {
      //  if (options == null)
        //    options = generalOptions;
        Bitmap bitmap = null;
        double ratioWidth = ((float) targetWidth) / (float) options.outWidth;
        double ratioHeight = ((float) targetWidth) / (float) options.outHeight;
        double ratio = Math.min(ratioWidth, ratioHeight);
        int dstWidth = (int) Math.round(ratio * options.outWidth);
        int dstHeight = (int) Math.round(ratio * options.outHeight);
        ratio = Math.floor(1.0 / ratio);
        int sample = nearest2pow((int) ratio);

        options.inJustDecodeBounds = false;
        if (sample <= 0) {
            sample = 1;
        }
        options.inSampleSize = (int) sample;
        options.inPurgeable = true;
        try {
            InputStream is;
            is = this.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is, null, options);
            if (sample > 1)
                bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight,
                        true);
            is.close();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmap;
    }

    public void exportIngredients(){

        Thread tr = new Thread(){

            @Override
            public void run() {
               dadesr= dadesIngredients();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  int r = dadesJSON(resultatC);
                        //if (r > 0) {
                        if(dadesr.get(0).equals("incorrecte")){


                        } else {
                            adapterI = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1, dadesr);
                            mapAT.get(1).setAdapter(adapterI);
                            mapAT.get(2).setAdapter(adapterI);
                         //   ArrayAdapter adapter = new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_1, dadesr);
                          //  autoCompleteText.setAdapter(adapter);

/*

                            trecepta.setText(dadesr[2]);
                            drecepta.setText(dadesr[3]);
                            irecepta.setText(dadesr[4]);
                            byte[] decodedBytes = Base64.decode(dadesr[5], 0);
  */


                        }
                    }
                });

            }


        };
        tr.start();
    }




    public ArrayList<String> dadesIngredients() {

        URL url;
        int resposta = 0;
        StringBuilder resul = null;
        int res = 0;
        ArrayList<String> dades1 = new ArrayList<String>();

        try {

            // url = new URL("http://10.0.2.2/afegirRecepta/buscarIngredient.php?tipus="+tipus);
            //url = new URL("http://10.0.2.2/afegirRecepta/buscarIngredient.php");
            url = new URL("http://alimentacionapp.com/buscarIngredient.php");

            HttpURLConnection connexio1 = (HttpURLConnection)url.openConnection();
            resposta = connexio1.getResponseCode();
            if (resposta == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connexio1.getInputStream()));
                String line;

                int k = 0;
                while ((line = br.readLine()) != null){

                    if(dades1.contains(line)){

                    }else {
                        dades1.add(line);
                    }

                    k++;
                }

                br.close();
            }
        } catch (Exception e) {        }

        return dades1;
    }


    public void exportarEstats(final String nomI, final Spinner spinner){

        Thread tr = new Thread(){

            @Override
            public void run() {
                final ArrayList dadesre;
                dadesre= dadesEstats(nomI);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  int r = dadesJSON(resultatC);
                        //if (r > 0) {
                        if(dadesre.size() != 0) {
                            if (dadesre.get(0).equals("incorrecte") == false) {


                                //} else {


                                adapterSp = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, dadesre);
                                spinner.setAdapter(adapterSp);
                                // spinnerEstado.setAdapter(adapter);
                            }
                        }
                    }
                });

            }


        };
        tr.start();
    }



    public ArrayList<String> dadesEstats(String nom) {

        URL url;
        String linea;
        int resposta = 0;
        StringBuilder resul = null;
        int res = 0;
        //   String dades[]=new String[];
        ArrayList<String> dades1 = new ArrayList<String>();

        try {

            // url = new URL("http://10.0.2.2/afegirRecepta/buscarIngredient.php?tipus="+tipus);
           // url = new URL("http://10.0.2.2/afegirRecepta/buscarEstats.php?nom="+nom);
            url = new URL("http://alimentacionapp.com/buscarEstats.php?nom="+nom);

            HttpURLConnection connexio1 = (HttpURLConnection)url.openConnection();

            //connexio1.setDoInput(true);


            resposta = connexio1.getResponseCode();

            if (resposta == HttpURLConnection.HTTP_OK) {

                BufferedReader br = new BufferedReader(new InputStreamReader(connexio1.getInputStream()));

                String line;

                int k = 0;
                while ((line = br.readLine()) != null){

                    dades1.add(line);

                    k++;
                }

                br.close();
            }
        } catch (Exception e) {        }


        return dades1;

    }

    private void insertIng() {
        class InsertIng extends AsyncTask<Void, String, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected String doInBackground(Void... params) {

                String result = null;

                int num=getSizeIngredients();
                for(int i = 1; i<=num;i++){
                    ArrayList<String> ing1 = recepta(i);
                    HashMap<String,String> data1 = new HashMap<>();
                    data1.put("aliment", ing1.get(0));
                    data1.put("estat", ing1.get(1));
                    data1.put("pes", ing1.get(2));
                    data1.put("id", ing1.get(3));
                    // String result = rh.sendPostRequest("http://10.0.2.2/afegirRecepta/insertarImatge.php",data1);
                    result = rh.sendPostRequest("http://alimentacionapp.com/insertarIng.php",data1);

                }
             
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity_afegirRecepta.this, "Guardando Receta", "Please wait...", true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("correcte")){
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
        }
        InsertIng ui = new InsertIng();
        ui.execute();
    }
    
    public int getSizeIngredients(){
        return mapAT.size();
    }


public ArrayList<String> recepta(int k){
    ArrayList<String> ing = new ArrayList();
    ing.add(mapAT.get(k).getText().toString());
    ing.add(mapSpinner.get(k).getSelectedItem().toString());
    ing.add(mapET.get(k).getText().toString());
    ing.add(String.valueOf(idrecepta));

    return ing;
    }


    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Uri selectedImageUri = null;
        Uri selectedImage;

        String filePath = null;
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    selectedImage = imageReturnedIntent.getData();

                    if (requestCode == SELECT_FILE) {

                        //   if (selectedPath != null) {
                        InputStream imageStream = null;
                        try {
                            imageStream = getContentResolver().openInputStream(
                                    selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        // Transformamos la URI de la imagen a inputStream y este a un Bitmap
                        bmp = BitmapFactory.decodeStream(imageStream);

                        // Ponemos nuestro bitmap en un ImageView que tengamos en la vista

                        image.setImageBitmap(bmp);

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos); // Could be Bitmap.CompressFormat.PNG or Bitmap.CompressFormat.WEBP
                        byte[] bai = baos.toByteArray();


                        base64Image = Base64.encodeToString(bai, Base64.DEFAULT);
                        //String message = (ResponseServer.getMessage()).replaceAll("\n", "<br>");
                        // base64Image = Html.fromHtml(base64Image).toString();
                        // base64Image = base64Image.replace("\n", "<br />");
                    }
                    //    }
                }
                break;
        }
    }
    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity_afegirRecepta.this, "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);


                HashMap<String,String> data1 = new HashMap<>();
                data1.put("imatge", uploadImage);
                data1.put("user", us);
                data1.put("nomr", nom);
                // String result = rh.sendPostRequest("http://10.0.2.2/afegirRecepta/insertarImatge.php",data1);
                String result = rh.sendPostRequest("http://alimentacionapp.com/insertarImatge.php",data1);

                return result;
            }


        }
        UploadImage ui = new UploadImage();
        ui.execute(bmp);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }


}
