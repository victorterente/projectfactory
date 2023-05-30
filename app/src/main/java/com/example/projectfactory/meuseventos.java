package com.example.projectfactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;

public class meuseventos extends AppCompatActivity {
    private ListView eventListView;
    private ArrayAdapter<String> adapter;
    private HashMap<String, Integer> eventIdsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meuseventos);

        eventListView = findViewById(R.id.eventListView);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);

        String apiUrl = "https://projectfactory.fly.dev/api/inscricoes/user/" + userId;

        adapter = new EventListAdapter();
        eventListView.setAdapter(adapter);

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventName = (String) parent.getItemAtPosition(position);
                int eventId = getEventIdByName(eventName);
                String apiUrl = "https://projectfactory.fly.dev/api/dados/user/evento/" + userId + "/" + eventId;

                // Start the activity to show event data
                Intent intent = new Intent(meuseventos.this, EventDetailsActivity.class);
                intent.putExtra("apiUrl", apiUrl);
                startActivity(intent);
            }
        });

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
                    int eventId = jsonObject.getInt("evento_id");
                    eventNames.add(eventName);
                    eventIdsMap.put(eventName, eventId);
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
            adapter.clear();
            adapter.addAll(eventNames);
        }
    }

    private class EventListAdapter extends ArrayAdapter<String> {
        public EventListAdapter() {
            super(meuseventos.this, R.layout.list_item_event, R.id.eventNameTextView, new ArrayList<>());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            String eventName = getItem(position);

            TextView eventNameTextView = view.findViewById(R.id.eventNameTextView);
            eventNameTextView.setText(eventName);

            Button removeButton = view.findViewById(R.id.removeButton);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle remove button click
                    removeEvent(eventName);
                }
            });

            return view;
        }
    }

    private void removeEvent(String eventName) {
        // Remove the event from the list and update the adapter
        adapter.remove(eventName);
        adapter.notifyDataSetChanged();
    }

    private int getEventIdByName(String eventName) {
        return eventIdsMap.get(eventName);
    }
}
