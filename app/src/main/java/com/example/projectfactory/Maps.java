package com.example.projectfactory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Maps extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private PolylineOptions polylineOptions;

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

        LatLng marker1LatLng = new LatLng(38.703264, -9.166665); // Coordinates for marker 1
        LatLng marker2LatLng = new LatLng(38.692976, -9.215044); // Coordinates for marker 2
        LatLng marker3LatLng = new LatLng(38.704887, -9.156958); // Coordinates for marker 3
        LatLng marker4LatLng = new LatLng(38.706982, -9.133180); // Coordinates for marker 4
        LatLng marker5LatLng = new LatLng(38.700346, -9.213880); // Coordinates for marker 5
        LatLng marker6LatLng = new LatLng(38.707665, -9.196821); // Coordinates for marker 6
        LatLng marker7LatLng = new LatLng(38.701887, -9.196843); // Coordinates for marker 7
        LatLng marker8LatLng = new LatLng(38.709210, -9.168583); // Coordinates for marker 8
        LatLng marker9LatLng = new LatLng(38.720727, -9.170396); // Coordinates for marker 9
        LatLng marker10LatLng = new LatLng(38.711693, -9.154785); // Coordinates for marker 10

        MarkerOptions markerOptions1 = new MarkerOptions()
                .position(marker1LatLng)
                .title("Evento 1 Inicio")
                .snippet("Voltinha de Bike")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        MarkerOptions markerOptions2 = new MarkerOptions()
                .position(marker2LatLng)
                .title("Evento 1 Fim")
                .snippet("Voltinha de Bike")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        MarkerOptions markerOptions3 = new MarkerOptions()
                .position(marker3LatLng)
                .title("Evento 2 Inicio")
                .snippet("Voltinha de Troti")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        MarkerOptions markerOptions4 = new MarkerOptions()
                .position(marker4LatLng)
                .title("Evento 2 Fim")
                .snippet("Voltinha de Troti")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        MarkerOptions markerOptions5 = new MarkerOptions()
                .position(marker5LatLng)
                .title("Evento 3 Inicio")
                .snippet("Voltinha Matinal")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        MarkerOptions markerOptions6 = new MarkerOptions()
                .position(marker6LatLng)
                .title("Evento 3 Fim")
                .snippet("Voltinha Matinal")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        MarkerOptions markerOptions7 = new MarkerOptions()
                .position(marker7LatLng)
                .title("Evento 4 Inicio")
                .snippet("Voltinha da Tarde")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        MarkerOptions markerOptions8 = new MarkerOptions()
                .position(marker8LatLng)
                .title("Evento 4 Fim")
                .snippet("Voltinha da Tarde")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        MarkerOptions markerOptions9 = new MarkerOptions()
                .position(marker9LatLng)
                .title("Evento 5 Inicio")
                .snippet("Voltinha Crianças")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        MarkerOptions markerOptions10 = new MarkerOptions()
                .position(marker10LatLng)
                .title("Evento 5 Fim")
                .snippet("Voltinha Crianças")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        googleMap.addMarker(markerOptions1);
        googleMap.addMarker(markerOptions2);
        googleMap.addMarker(markerOptions3);
        googleMap.addMarker(markerOptions4);
        googleMap.addMarker(markerOptions5);
        googleMap.addMarker(markerOptions6);
        googleMap.addMarker(markerOptions7);
        googleMap.addMarker(markerOptions8);
        googleMap.addMarker(markerOptions9);
        googleMap.addMarker(markerOptions10);

        // Create routes between markers with the same color
        drawRoute(marker1LatLng, marker2LatLng, Color.BLUE);
        drawRoute(marker3LatLng, marker4LatLng, Color.RED);
        drawRoute(marker5LatLng, marker6LatLng, Color.GREEN);
        drawRoute(marker7LatLng, marker8LatLng, Color.YELLOW);
        drawRoute(marker9LatLng, marker10LatLng, Color.MAGENTA);
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
}
