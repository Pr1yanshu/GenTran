/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.justjava;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.R.attr.key;
import static com.example.android.justjava.R.id.spinner;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Spinner spinner1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // progressBar.setVisibility(View.GONE);

        progressBar=(ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);


       /* String datapath = getFilesDir() + "/tesseract/";
        String language = "eng";
        checkFile(new File(datapath + "tessdata/"));*/
    }

    //private ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar1);

    public void submitOrder(View view) {
        // Get user's name
        progressBar.setVisibility(View.VISIBLE);
        Translation translation = new Translation();
        translation.execute();


    }


    private class Translation extends AsyncTask<Void,String,String> {
        EditText nameField = (EditText) findViewById(R.id.name_field);
        Editable nameEditable = nameField.getText();
        String text = nameEditable.toString();

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        String lang = spinner.getSelectedItem().toString();

        Spinner spinner1 = (Spinner)findViewById(R.id.spinner1);
        String lang1 = spinner1.getSelectedItem().toString();



        @Override
        protected String doInBackground(Void... params) {
            String lex = "en";
            String lex1 = "en";

            if(lang.equals("French")){
                 lex = "fr";
            }
            if(lang.equals("English")){
                lex = "en";
            }
            else if(lang.equals("Portuguese")){
                 lex = "pt";
            }
            else if(lang.equals("Italian")){
                 lex = "it";
            }
            else if(lang.equals("Russian")){
                 lex = "ru";
            }
            else if(lang.equals("Hindi")){
                 lex = "hi";
            }
            else if(lang.equals("Arabic")){
                 lex = "ar";
            }
            else if(lang.equals("German")){
                 lex = "de";
            }


            if(lang1.equals("French")){
                lex1 = "fr";
            }
            if(lang1.equals("English")){
                lex1 = "en";
            }
            else if(lang1.equals("Portuguese")){
                lex1 = "pt";
            }
            else if(lang1.equals("Italian")){
                lex1 = "it";
            }
            else if(lang1.equals("Russian")){
                lex1 = "ru";
            }
            else if(lang1.equals("Hindi")){
                lex1 = "hi";
            }
            else if(lang1.equals("Arabic")){
                lex1 = "ar";
            }
            else if(lang1.equals("German")){
                lex1 = "de";
            }

            String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?lang="+lex1+"-"+lex+"&key=trnsl.1.1.20170503T142713Z.427261940079d3fb.6b2bd0e9585a181a6990eb3fd8b3d25f2bd0580a&text="+text;
            OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response==null)
            {
                return null;}
            try {
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("test",result);
            try {

                JSONObject result_object= new JSONObject(result);
                String translated_text = result_object.getString("text").replace('[',' ').replace(']',' ').replace('"',' ');
                Log.v("dex",translated_text);
                TextView texi = (TextView)findViewById(R.id.translated_text);
                texi.setText(translated_text);
                progressBar.setVisibility(View.GONE);



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    /*private void copyFiles() {
        try {

            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";

            //get access to AssetManager
            AssetManager assetManager = getAssets();

            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);

            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        //The directory exists, but there is no data file in it
        if (dir.exists()) {
            String datafilepath = datapath + "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles();
            }
        }
    }*/
}
