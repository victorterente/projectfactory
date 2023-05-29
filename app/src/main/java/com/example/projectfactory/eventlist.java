package com.example.projectfactory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

public class eventlist extends AppCompatActivity {

    private ListView listViewEventos;
    private List<String> eventosList;
    private List<String> eventoIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventlist);

        listViewEventos = findViewById(R.id.listViewEventos);
        eventosList = new ArrayList<>();
        eventoIdList = new ArrayList<>();

        new FetchEventosTask().execute("https://projectfactory.fly.dev/api/eventos");

        listViewEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventoSelecionado = eventosList.get(position);
                String eventoIdSelecionado = eventoIdList.get(position);
                Toast.makeText(eventlist.this, "Evento selecionado: " + eventoSelecionado, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(eventlist.this, eventoinfo.class);
                intent.putExtra("evento_id", eventoIdSelecionado);
                startActivity(intent);
            }
        });
    }

    private class FetchEventosTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = null;
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                result = stringBuilder.toString();

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject evento = jsonArray.getJSONObject(i);
                        String nomeEvento = evento.getString("evento_nome");
                        String idEvento = evento.getString("evento_id");
                        eventosList.add(nomeEvento);
                        eventoIdList.add(idEvento);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(eventlist.this, android.R.layout.simple_list_item_1, eventosList);
                    listViewEventos.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(eventlist.this, "Erro ao obter eventos.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
