package edu.uiuc.cs427app;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import edu.uiuc.cs427app.databinding.ActivityMapsBinding;

// uses Google Map fragment to display a city map
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String cityname;
    double latitude = 40.11642; //default value is Champaign
    double longitude = -88.24338;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //apply use theme to this activity
        Account account = getIntent().getParcelableExtra("account");
        ThemeUtils.applyTheme(account, this);

        //bind the map to the view
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //process the city information in the intent package
        Intent intent = getIntent();
        cityname = intent.getStringExtra("city");
        latitude = intent.getDoubleExtra("lat",-34);
        longitude = intent.getDoubleExtra("lon",151);

        //display city information on header of map
        String msg = cityname + "\n" +
                "Latitude: " +String.format("%.5f", latitude)
                + "\n" +
                "Longitude: "+String.format("%.5f", longitude);
        TextView cityInfo = findViewById(R.id.cityNameLatLon);
        cityInfo.setText(msg);
        TextView cityName = findViewById(R.id.cityName);
        cityName.setText(cityname);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used. We move the map
     * view camera to the city's latitude and longitude coords. Map preferences are
     * saved in activity_maps.xml
     * @param googleMap  the map to be drawn
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set the map lat/lon, and move the camera to the city
        LatLng citylocation = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(citylocation));
    }

}