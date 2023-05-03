package com.example.projectfactory;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class eventoinfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventoinfo);

        // Get the event information passed from the ListaDeEventos activity
        String eventoNome = getIntent().getStringExtra("evento_nome");
        String eventoDescricao = getIntent().getStringExtra("evento_descricao");
        String eventoData = getIntent().getStringExtra("evento_data");
        String eventoStart = getIntent().getStringExtra("evento_start");
        String eventoEnd = getIntent().getStringExtra("evento_end");

        // Lookup views for data population
        TextView nome = findViewById(R.id.evento_nome);
        TextView descricao = findViewById(R.id.evento_descricao);
        TextView data = findViewById(R.id.evento_data);
        TextView end = findViewById(R.id.evento_end);
        TextView start = findViewById(R.id.evento_start);

        // Populate the data into the views
        nome.setText(eventoNome);
        descricao.setText(eventoDescricao);
        data.setText(eventoData);
        end.setText(eventoEnd);
        start.setText(eventoStart);
    }
}
