package com.example.mkuma359.english;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EnglishToHindiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english_to_hindi);

        Intent intent = getIntent();
        String word = intent.getStringExtra("inputWord");


        //take all the element
        TextView inputWord = findViewById(R.id.inputWord);
        TextView partsOfSpeechLable = findViewById(R.id.textView2);
        TextView partsOfSpeechValue = findViewById(R.id.textView9);
        TextView meaningInHindiLable = findViewById(R.id.meaningInHindi);
        TextView meaningInHindiMeaning = findViewById(R.id.textView3);
        TextView usageLable = findViewById(R.id.textView7);
        TextView usage1 = findViewById(R.id.textView8);
        TextView usage2 = findViewById(R.id.textView10);

        String result = "Response";


        SearchPageRunnable myRunnable = new SearchPageRunnable(word, inputWord, partsOfSpeechValue, meaningInHindiMeaning, usage1, usage2);
        Thread thread = new Thread(myRunnable);
        thread.start();

       // runOnUiThread(myRunnable);
    }

}


class SearchPageRunnable implements Runnable {

    public String wordToSearch;
    TextView inputWord;
    TextView partsOfSpeechValue;
    TextView meaningInHindiMeaning;
    TextView usage1;
    TextView usage2;



    public SearchPageRunnable(String wordToSearch, TextView inputWord,  TextView partsOfSpeechValue, TextView meaningInHindiMeaning, TextView usage1, TextView usage2)
    {
        this.wordToSearch = wordToSearch;
        this.inputWord = inputWord;
        this.partsOfSpeechValue = partsOfSpeechValue;
        this.meaningInHindiMeaning = meaningInHindiMeaning;
        this.usage1 = usage1;
        this.usage2 = usage2;
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
            JSONObject reader = new JSONObject(res);
            String meaning = reader.getString("meaning");
            String partsOfSpeech = reader.getString("partsOfSpeech");

            JSONArray commonUsageInHindi = reader.getJSONArray("commonUsagesInHindi");

            String commonUsageInHindiString1 = "";
            String commonUsageInHindiString2 = "";

            if(commonUsageInHindi.length()>0)
            {
                commonUsageInHindiString1 = commonUsageInHindi.get(0).toString();

                if(commonUsageInHindi.length()>1)
                    commonUsageInHindiString2 = commonUsageInHindi.get(1).toString();
            }


            inputWord.setText(wordToSearch+"s");
            usage1.setText(commonUsageInHindiString1);
            usage2.setText(commonUsageInHindiString2);
            meaningInHindiMeaning.setText(meaning);
            partsOfSpeechValue.setText(partsOfSpeech);


        }
        catch(Exception ex)
        {
            System.out.println("Exception by Manish");
            ex.printStackTrace();
        }
    }
}