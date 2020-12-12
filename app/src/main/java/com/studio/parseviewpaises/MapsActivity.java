package com.studio.parseviewpaises;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {

    private GoogleMap mMap;
    private Double lat, log;
    private String nome;

    private SensorManager sensorManager;
    private Sensor acelerometro;
    Bitmap imageBitmap = null;

    private int cont = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(acelerometro == null)
            Toast.makeText(this, "Dispositivo não contém acelerador",Toast.LENGTH_LONG).show();


        if(getIntent().getExtras() != null){
            lat = Double.parseDouble(getIntent().getExtras().getString("Lat"));
            log = Double.parseDouble(getIntent().getExtras().getString("Lon"));
            nome = getIntent().getExtras().getString("Nome");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        sensorManager.registerListener(this,acelerometro,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        super.onStop();

        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mMap != null){
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                   ChamarIntent(false);//Chama a PhotoActivity sem a camera iniciada

                    return true;
                }
            });
        }
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng ponto = new LatLng(lat, log);
        mMap.addMarker(new MarkerOptions().position(ponto).title(nome));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ponto));

        onResume();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];


        if(x > 20.000 || x < -15.000 || y > 20.000 || y < -15.000){
            ChamarIntent(true);//Chama a PhotoActivity com a camera iniciada
        }
        
        onStop();

    }


    public void ChamarIntent(boolean AtivarCamera){

        Intent intent = new Intent(MapsActivity.this, PhotoActivity.class);
        intent.putExtra("Nome",nome);
        intent.putExtra("Camera",AtivarCamera);
        startActivity(intent);
    }
}

