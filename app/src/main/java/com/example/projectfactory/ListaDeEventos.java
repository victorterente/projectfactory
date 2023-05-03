package com.example.projectfactory;

import android.os.Bundle;
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

import android.os.Bundle;
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

                    Evento event = new Evento(eventName, eventDescricao, eventData, eventEnd, eventStart);
                    events.add(event);
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
