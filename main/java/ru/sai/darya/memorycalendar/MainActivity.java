package ru.sai.darya.memorycalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final int REQUEST_CODE_ADD = 12345;
    private static final int REQUEST_CODE_LIST = 12346;
    private static final int REQUEST_CODE_LOCATION = 123;

    private GoogleMap mMap;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.buttonAdd)
    Button btnAdd;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.buttonToList)
    Button btnToList;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.buttonLocation)
    Button btnLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        ButterKnife.bind(this);
        btnAdd.setOnClickListener(s-> {
            Intent intent = new Intent(this, MemoryActivity.class);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

        btnToList.setOnClickListener(s-> {
            Intent intent = new Intent(this, ListActivity.class);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_LIST);
        });

        btnLocation.setOnClickListener(s->{
            Intent intent = new Intent(this, LocationActivity.class);
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng ekat = new LatLng(56.50, 60.35);
        mMap.addMarker(new MarkerOptions()
                .position(ekat)
                .title("Marker in Ekat"));
        LatLng stpeter = new LatLng(59.57, 30.19);
        mMap.addMarker(new MarkerOptions()
                .position(stpeter)
                .title("Marker in StPeter"));
        LatLng moscow = new LatLng(56, 38);
        mMap.addMarker(new MarkerOptions()
                .position(moscow)
                .title("Marker in Moscow"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(moscow));
    }
}

