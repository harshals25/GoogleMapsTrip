package com.example.harshal.inclass09;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TripClass tripClass;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        setTitle("Path Activity");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        InputStream is = getResources().openRawResource(R.raw.trip);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        Gson gson = new Gson();

        tripClass = gson.fromJson(jsonString,TripClass.class);
        Log.d("demo","Values of res " + tripClass);

   }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng> points = tripClass.points; // list of latlng
        Log.d("demo","points value " + points);

        LatLng start = points.get( 0 );
        LatLng end = points.get( points.size() - 1 );

        LatLng startMarker = start;
        mMap.addMarker(new MarkerOptions().position(startMarker).title("Start"));
        //mMap.moveCamera( CameraUpdateFactory.newLatLng(startMarker));

        LatLng endMarker = end;
        mMap.addMarker(new MarkerOptions().position(endMarker).title("End"));
        //mMap.moveCamera( CameraUpdateFactory.newLatLng(endMarker));
        for (int i = 0; i < points.size() - 1; i++) {
            LatLng src = points.get(i);
            LatLng dest = points.get(i + 1);

            // LatLng latLngcurrent = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d("demo", "value of src " + src);
            Log.d("demo", "value of dest " + dest);

            // mMap is the Map Object
            Polyline line = mMap.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(src.latitude, src.longitude),
                            new LatLng(dest.latitude, dest.longitude)
                    ).width(5).color(Color.BLUE).geodesic(true)
            );



            builder = new LatLngBounds.Builder();
            for(LatLng point: points){
                builder.include( point );
            }
            int padding = 20;
            bounds = builder.build();
            final CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.setOnMapLoadedCallback( new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.animateCamera(cu);
                }
            } );        }
        }
}