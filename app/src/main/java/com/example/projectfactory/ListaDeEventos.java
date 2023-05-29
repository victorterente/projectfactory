package com.example.projectfactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaDeEventos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listadeeventos);

        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        ListView eventList = findViewById(R.id.event_list);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://projectfactory.fly.dev/api/eventos";

        JsonArrayRequest request = new JsonArrayRequest(url, response -> {
            List<Evento> events = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject eventObject = response.getJSONObject(i);
                    String eventName = eventObject.getString("evento_nome");
                    String eventDescricao = eventObject.getString("evento_descricao");
                    String eventData = eventObject.getString("evento_data");
                    String eventEnd = eventObject.getString("evento_end");
                    String eventStart = eventObject.getString("evento_start");
                    String eventid = eventObject.getString("evento_id");

                    Log.d("ListaDeEventos", "Event ID: " + eventid);

                    Evento event = new Evento(eventName, eventDescricao, eventData, eventEnd, eventStart, eventid);
                    events.add(event);

                    eventList.setOnItemClickListener((parent, view, position, id) -> {
                        Evento clickedEvent = events.get(position);
                        Intent intent = new Intent(ListaDeEventos.this, eventoinfo.class);
                        intent.putExtra("evento_nome", clickedEvent.getNome());
                        intent.putExtra("evento_descricao", clickedEvent.getDescricao());
                        intent.putExtra("evento_data", clickedEvent.getData());
                        intent.putExtra("evento_start", clickedEvent.getStart());
                        intent.putExtra("evento_end", clickedEvent.getEnd());


                        startActivity(intent);
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            EventoListAdapter adapter = new EventoListAdapter(ListaDeEventos.this, (ArrayList<Evento>) events);
            eventList.setAdapter(adapter);
        }, error -> {
            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        queue.add(request);
    }
}
