package com.example.mkuma359.english;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;


public class SearchPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Intent intent = getIntent();
        String word = intent.getStringExtra("inputWord");

        TextView usageLabel = findViewById(R.id.textViewLable2);

        TextView meaningLable = findViewById(R.id.textViewMeaningLable);
        TextView actualWord = findViewById(R.id.textViewLable1);


        actualWord.setText(word);
        usageLabel.setText("Usage");
        meaningLable.setText("हिंदी में अर्थ");




        TextView partsOfSpeech = findViewById(R.id.textViewParts);
        TextView textViewMeaning = findViewById(R.id.textViewMeaning);
        TextView textViewUsage = findViewById(R.id.textViewUsage);

        String result = "Response";


        MyRunnable myRunnable = new MyRunnable(word, "res", textViewMeaning, textViewUsage, partsOfSpeech);
        Thread thread = new Thread(myRunnable);
        thread.start();


        //textViewMeaning.setText(result);
        //textViewUsage.setText(myRunnable.response);

    }
}

class MyRunnable implements Runnable {

    public String wordToSearch;
    public String response;
    TextView textViewMeaning;
    TextView textViewUsage;
    TextView partsOfSpeech;


    public MyRunnable(String wordToSearch, String response, TextView textViewMeaning, TextView textViewUsage, TextView partsOfSpeech)
    {
        this.wordToSearch = wordToSearch;
        this.response = response;
        this.textViewMeaning = textViewMeaning;
        this.textViewUsage = textViewUsage;
        this.partsOfSpeech = partsOfSpeech;
    }
    public void run(){

        try
        {
            URL url = new URL("http://54.245.161.53/learn/rest/WordService/word/search?word="+wordToSearch);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(100000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            InputStream response = conn.getInputStream();


            BufferedInputStream bis = new BufferedInputStream(response);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            int result1 = bis.read();
            while(result1 != -1) {
                buf.write((byte) result1);
                result1 = bis.read();
            }

            System.out.println("MANISH LOG**********************");
            System.out.println(buf.toString("UTF-8"));

            String res = buf.toString("UTF-8");
            this.response = res;

            JSONObject reader = new JSONObject(res);
            String meaning = reader.getString("meaning");
            String partOf = reader.getString("partsOfSpeech");
            JSONArray commonUsageInHindi = reader.getJSONArray("commonUsagesInHindi");

            String commonUsageInHindiString = "";

            for(int i =0 ; i<commonUsageInHindi.length(); i++)
            {
                commonUsageInHindiString = commonUsageInHindiString +"\n"+commonUsageInHindi.get(i).toString();
            }


            textViewUsage.setText(commonUsageInHindiString);
            textViewMeaning.setText(meaning);
            partsOfSpeech.setText(partOf);

        }
        catch(Exception ex)
        {
            System.out.println("MANISH");
            ex.printStackTrace();
            this.response = ex.getMessage();
        }
    }
}