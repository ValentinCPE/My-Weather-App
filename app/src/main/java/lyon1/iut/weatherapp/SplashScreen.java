package lyon1.iut.weatherapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import layout.WeatherFragment;

/**
 * Created by valentin on 29/03/17.
 */

public class SplashScreen extends Activity implements LocationListener {
    VideoView videoElem;

    private Location location;
    private LocationManager locationManager;
    private String provider;
    private final int PERMISSION_ACCESS_COARSE_LOCATION = 2;

    private boolean enabled;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);


        try {
            videoElem = (VideoView) findViewById(R.id.myvideo);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.iut_intro);
            videoElem.setVideoURI(video);
            videoElem.setZOrderOnTop(true);
            videoElem.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    testConnection();
                }
            });
            videoElem.start();

        } catch (Exception ex) {
            start();
        }

        localisationManagement();

    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 600, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void testConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            Toast.makeText(SplashScreen.this,
                    "No connection",
                    Toast.LENGTH_LONG).show();
            enabled = true;
        }
        start();
    }

    private void start() {
        if (enabled && isFinishing())
            return;
        Log.d("testgps", new CitySaved(SplashScreen.this).getCity());
        startActivity(new Intent(this, WeatherActivity.class).putExtra("city", new CitySaved(SplashScreen.this).getCity()));
        finish();

    }

    public void majMeteo(final String city) {
        new AsyncTask().execute(city, SplashScreen.this);
    }

    private void localisationManagement(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        enabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            videoElem.pause();
            AlertDialog.Builder gpsTest = new AlertDialog.Builder(SplashScreen.this);
            gpsTest.setTitle("GPS Connection");
            gpsTest.setMessage("We can use your current position or by default \"Villeurbanne\" ");
            gpsTest.setNegativeButton("Without Position", null);
            gpsTest.setPositiveButton("Activate my GPS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(SplashScreen.this, "Thank you, restart :)", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            });
            gpsTest.show();
        }

        Criteria criteria = new Criteria();
        if(locationManager.getBestProvider(criteria, true) != null) {
            provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);

            try {
                if (location != null) {
                    Geocoder loc = new Geocoder(this, Locale.FRANCE);
                    List<Address> addresses = loc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses.size() > 0) {
                        new CitySaved(SplashScreen.this).setCity(addresses.get(0).getLocality().toLowerCase() + ", " + addresses.get(0).getCountryCode().toLowerCase());
                        majMeteo(new CitySaved(SplashScreen.this).getCity());
                        enabled = true;
                        //   start();
                    }

                } else {
                    Toast.makeText(SplashScreen.this, "No Location", Toast.LENGTH_LONG).show();
                    new CitySaved(SplashScreen.this).setCity("villeurbanne, fr");
                    majMeteo(new CitySaved(SplashScreen.this).getCity());
                    enabled = true;
                }
            } catch (Exception e) {
                Log.d("gps", "problem locality");
            }


        }
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                        PERMISSION_ACCESS_COARSE_LOCATION);
            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thank you, restart :)", Toast.LENGTH_SHORT).show();
                    start();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }




}
