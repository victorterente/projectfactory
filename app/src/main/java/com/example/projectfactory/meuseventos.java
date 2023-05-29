package com.example.projectfactory;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class meuseventos extends AppCompatActivity {
    private ListView eventListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meuseventos);

        eventListView = findViewById(R.id.eventListView);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);

        String apiUrl = "https://projectfactory.fly.dev/api/inscricoes/user/" + userId;

        new FetchEventsTask().execute(apiUrl);
    }

    private class FetchEventsTask extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... urls) {
            List<String> eventNames = new ArrayList<>();

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONArray jsonArray = new JSONArray(response.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String eventName = jsonObject.getString("evento_nome");
                    eventNames.add(eventName);
                }

                reader.close();
                inputStream.close();
                connection.disconnect();

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return eventNames;
        }

        @Override
        protected void onPostExecute(List<String> eventNames) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    meuseventos.this,
                    android.R.layout.simple_list_item_1,
                    eventNames
            );

            eventListView.setAdapter(adapter);
        }
    }
}
