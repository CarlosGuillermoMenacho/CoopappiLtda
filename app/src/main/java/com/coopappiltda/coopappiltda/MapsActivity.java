package com.coopappiltda.coopappiltda;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String respuesta;
    private String response;
    private String view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        respuesta = getIntent().getStringExtra("respuesta");
        response = getIntent().getStringExtra("response");
        view = getIntent().getStringExtra("vista");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            JSONArray jsonObject = new JSONArray(respuesta);
            String lugar;
            LatLng coordenada = new LatLng(0,0);
            for (int i = 0 ; i < jsonObject.length() ; i++){
                JSONArray fila = jsonObject.getJSONArray(i);
                double latitud = fila.getDouble(0);
                double longitud = fila.getDouble(1);
                lugar = fila.getString(2);
                coordenada = new LatLng(latitud,longitud);
                googleMap.addMarker(new MarkerOptions().position(coordenada).title(lugar));
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenada,11));
        } catch (JSONException e) {
            startActivity(new Intent(MapsActivity.this,MainActivity.class));
            overridePendingTransition(R.anim.right_in,R.anim.right_out);
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (view.equals("pc")) {
            startActivity(new Intent(MapsActivity.this, MainActivity.class));
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
        if (view.equals("ue")){
            Intent intent =new Intent(MapsActivity.this, UnidadesdeEmergencia.class);
            intent.putExtra("respuesta",response);
            startActivity(intent);
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
    }
}