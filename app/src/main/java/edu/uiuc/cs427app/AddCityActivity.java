package edu.uiuc.cs427app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.accounts.Account;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import java.util.Arrays;

public class AddCityActivity extends AppCompatActivity implements View.OnClickListener {

    private String username;
    private Account account;
    protected SharedPreferences sharedPreferences;
    private static final String TAG = "AddCityActivity";
    private String cityname;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = getIntent().getParcelableExtra("account");
        username = account.name;

        // Apply the theme based on the user's preference
        ThemeUtils.applyTheme(account, this);

        setContentView(R.layout.activity_add_city);

        //set up button and listener to save the city once selected
        Button buttonSaveCity = findViewById(R.id.buttonSaveCity);
        buttonSaveCity.setOnClickListener(this);

        // Initialize the AutocompleteSupportFragment to search for the city name
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Only return name, lat/lon, and address data
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        // Restrict the Places to "Cities"
        autocompleteFragment.setTypesFilter(Arrays.asList("(cities)"));

        // Set up a PlaceSelectionListener to handle the response and store the info
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            /**
             * Called whenever city is selected from the search autocomplete list.
             * @param place the city returned from the autocomplete list
             */
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //get the data from the place object
                cityname = place.getAddress();
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;

                String msg = "Place: "
                        + place.getAddress() + "\n"
                        + "Latitude: "
                        + place.getLatLng().latitude + "\n"
                        + "Longitude: "
                        + place.getLatLng().longitude;
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                Log.d(TAG, msg);
            }

            /**
             * Called whenever there is an error in choosing a city from the autocomplete list.
             * @param status the error returned
             */
            @Override
            public void onError(@NonNull Status status) {
                Log.d(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onClick(View view) {
        //save the city data to the db
        if (view.getId()==R.id.buttonSaveCity) {
            //setup up the ContentValues object with city data to send to the db
            ContentValues values = new ContentValues();
            values.put(DataStore.CityEntry.COL_USERNAME, username);
            values.put(DataStore.CityEntry.COL_CITY, cityname);
            values.put(DataStore.CityEntry.COL_LATITUDE, latitude);
            values.put(DataStore.CityEntry.COL_LONGITUDE, longitude);

            //query the db to see if the city is already there for the given user
            String selection = DataStore.CityEntry.COL_USERNAME + " = '" + username+"' AND "
                              +DataStore.CityEntry.COL_CITY+" = '"+cityname+"'";
            Cursor cursor = getContentResolver().query(DataStore.CityEntry.CONTENT_URI, null, selection, null, null);

            //only insert the city if not already in the users citylist, i.e. the query result is empty
            if (cursor.getCount()==0){
                // inserting into database through content URI
                getContentResolver().insert(DataStore.CityEntry.CONTENT_URI, values);

                // displaying a toast message
                Toast.makeText(getBaseContext(), cityname+" Saved", Toast.LENGTH_LONG).show();

                //jump back to the Main Activity
                cursor.close();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("account", account);
                finish();
                startActivity(intent);

            } else {
                //pop a message about the duplicate city
                Toast.makeText(getBaseContext(), cityname+" is a duplicate. Choose a new city.", Toast.LENGTH_LONG).show();
            }

        }
    }
}