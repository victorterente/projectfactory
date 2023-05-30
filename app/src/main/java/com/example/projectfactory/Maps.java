package com.example.projectfactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private PolylineOptions polylineOptions;
    private Map<Marker, String> markerEventIdMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng lisbonLatLng = new LatLng(38.7223, -9.1393); // Coordinates for Lisbon
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(lisbonLatLng, 11); // Zoom level 11
        googleMap.moveCamera(cameraUpdate);

        // Initialize the marker-eventId map
        markerEventIdMap = new HashMap<>();

        // Fetch markers from the API
        fetchMarkers();
    }

    private void fetchMarkers() {
        new Thread(() -> {
            try {
                URL url = new URL("https://projectfactory.fly.dev/api/eventos");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Read the response from the API
                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder stringBuilder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    stringBuilder.append(scanner.nextLine());
                }
                String response = stringBuilder.toString();

                // Parse the response to obtain marker coordinates
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    final int index = i; // Create a final variable to capture the value of 'i'

                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    double startLat = jsonObject.getDouble("evento_lat1");
                    double startLng = jsonObject.getDouble("evento_long1");
                    double endLat = jsonObject.getDouble("evento_lat2");
                    double endLng = jsonObject.getDouble("evento_long2");
                    String eventoNome = jsonObject.getString("evento_nome");
                    String eventoId = jsonObject.getString("evento_id");

                    LatLng startLatLng = new LatLng(startLat, startLng);
                    LatLng endLatLng = new LatLng(endLat, endLng);

                    // Create marker options and add markers to the map
                    final MarkerOptions startMarkerOptions = createMarkerOptions(startLatLng, eventoNome + " Inicio");
                    final MarkerOptions endMarkerOptions = createMarkerOptions(endLatLng, "Evento " + (index + 1) + " Fim");

                    runOnUiThread(() -> {
                        Marker startMarker = googleMap.addMarker(startMarkerOptions);
                        googleMap.addMarker(endMarkerOptions);

                        // Create route between the markers
                        drawRoute(startLatLng, endLatLng, getColorForIndex(index));

                        // Save the evento_id as the marker's tag
                        startMarker.setTag(eventoId);
                        // Add the marker and evento_id to the map
                        markerEventIdMap.put(startMarker, eventoId);
                    });
                }

                // Set a custom info window adapter for the markers
                googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

                // Set a marker click listener
                googleMap.setOnMarkerClickListener(marker -> {
                    // Open the custom info window for the marker
                    marker.showInfoWindow();
                    // Return true to indicate that the marker click is handled
                    return true;
                });
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private MarkerOptions createMarkerOptions(LatLng latLng, String title) {
        return new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(getColorForTitle(title)));
    }

    private float getColorForTitle(String title) {
        if (title.contains("Inicio")) {
            return BitmapDescriptorFactory.HUE_BLUE;
        } else if (title.contains("Fim")) {
            return BitmapDescriptorFactory.HUE_RED;
        } else {
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }

    private int getColorForIndex(int index) {
        int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA};
        return colors[index % colors.length];
    }

    private void drawRoute(LatLng startLatLng, LatLng endLatLng, int color) {
        polylineOptions = new PolylineOptions()
                .add(startLatLng)
                .add(endLatLng)
                .width(10)
                .color(color)
                .geodesic(true);

        googleMap.addPolyline(polylineOptions);
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View windowView;

        CustomInfoWindowAdapter() {
            windowView = LayoutInflater.from(Maps.this).inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (marker.getTitle() != null && marker.getTitle().startsWith("Evento")) {
                TextView eventNameTextView = windowView.findViewById(R.id.eventNameTextView);
                eventNameTextView.setText(marker.getTitle());

                windowView.setOnClickListener(v -> {
                    // Get the evento_id from the marker
                    String eventId = markerEventIdMap.get(marker);
                    if (eventId != null) {
                        // Start the EventInfoActivity with the evento_id
                        Intent intent = new Intent(Maps.this, EventInfoActivity.class);
                        intent.putExtra("evento_id", eventId);
                        startActivity(intent);
                    }
                });
            }
            return windowView;
        }
    }
}
