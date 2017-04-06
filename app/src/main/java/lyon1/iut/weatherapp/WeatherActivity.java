package lyon1.iut.weatherapp;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import layout.WeatherFragment;

public class WeatherActivity extends FragmentActivity {


    private static int NUM_PAGES;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private Location location;
    private LocationManager locationManager;
    private String provider;

    private TextView seLocaliser;
    private TextView changer;
    private TextView map;
    private TextView history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Intent rec = getIntent();
        String city = rec.getStringExtra("city");
        Log.d("testgps", city);
        new CitySaved(WeatherActivity.this).setCity(city);
        if(Forecast.getWeatherList(new CitySaved(WeatherActivity.this).getCity()) != null) {
            NUM_PAGES = Forecast.getWeatherList(new CitySaved(WeatherActivity.this).getCity()).size();
        }else{
            NUM_PAGES = 10;
        }
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        map = (TextView) findViewById(R.id.textViewMap);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MapActivity.class));
            }
        });

        changer = (TextView) findViewById(R.id.textViewChercher);
        changer.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                                showInputDialog();
                                           }
                                       });

        seLocaliser = (TextView) findViewById(R.id.textViewLocaliser);
        seLocaliser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                provider = locationManager.getBestProvider(criteria, true);
                location = locationManager.getLastKnownLocation(provider);
                Geocoder loc = new Geocoder(v.getContext(), Locale.FRANCE);
                List<Address> addresses = null;
                if(location != null) {
                    try {
                        addresses = loc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            new CitySaved(WeatherActivity.this).setCity(addresses.get(0).getLocality() + ", " + addresses.get(0).getCountryCode());
                            majMeteo(new CitySaved(WeatherActivity.this).getCity());
                        }
                    } catch (IOException e) {
                        Toast.makeText(WeatherActivity.this,"Connection problem", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(WeatherActivity.this,"Localisation problem, sorry", Toast.LENGTH_LONG).show();
                }
            }
        });

        history = (TextView) findViewById(R.id.textViewFavori);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(WeatherActivity.this);
                builderSingle.setTitle("Select One City:");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WeatherActivity.this, android.R.layout.select_dialog_singlechoice);

                for(String city : Forecast.getCityHistory())
                    arrayAdapter.add(city);

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cityChoose = arrayAdapter.getItem(which);
                        new CitySaved(WeatherActivity.this).setCity(cityChoose);
                        majMeteo(new CitySaved(WeatherActivity.this).getCity());
                    }
                });
                builderSingle.show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new WeatherFragment(position,new CitySaved(WeatherActivity.this).getCity());
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void majMeteo(final String city) {
        new AsyncTask().execute(city, WeatherActivity.this);
    }

    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString().toLowerCase());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        new CitySaved(this).setCity(city);
        majMeteo(new CitySaved(WeatherActivity.this).getCity());
    }

}
