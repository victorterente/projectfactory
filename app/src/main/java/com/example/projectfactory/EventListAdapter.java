package com.example.projectfactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EventListAdapter extends ArrayAdapter<Event> {

    public EventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listadeeventos, parent, false);
        }

        // Get the event at this position
        Event event = getItem(position);

        // Set the text of the event name TextView
        TextView eventNameTextView = convertView.findViewById(R.id.event_name_text_view);
        eventNameTextView.setText(event.getNome());

        return convertView;
    }
}
