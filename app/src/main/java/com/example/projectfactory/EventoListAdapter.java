
package com.example.projectfactory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventoListAdapter extends ArrayAdapter<Evento> {

    private Context context;

    public EventoListAdapter(Context context, ArrayList<Evento> eventos) {
        super(context, 0, eventos);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Evento evento = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.eventoinfo, parent, false);
        }

        // Lookup view for data population
        TextView eventoNome = convertView.findViewById(R.id.evento_nome);

        // Populate the data into the template view using the data object
        eventoNome.setText(evento.getNome());

        // Set the click listener for the event name
        eventoNome.setOnClickListener(v -> {
            // Start the EventoInfo activity with the event information
            Intent intent = new Intent(context, eventoinfo.class);
            intent.putExtra("evento_nome", evento.getNome());
            intent.putExtra("evento_descricao", evento.getDescricao());
            intent.putExtra("evento_data", evento.getData());
            intent.putExtra("evento_end", evento.getEnd());
            intent.putExtra("evento_start", evento.getStart());
            context.startActivity(intent);
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
