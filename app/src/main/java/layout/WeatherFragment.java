package layout;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import lyon1.iut.weatherapp.AsyncTask;
import lyon1.iut.weatherapp.CitySaved;
import lyon1.iut.weatherapp.Forecast;
import lyon1.iut.weatherapp.R;
import lyon1.iut.weatherapp.Weather;


public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    static TextView champVille;
    static TextView champMAJ;
    static TextView champDetail;
    static TextView champDetailWeather;
    static TextView champTemp;
    static TextView IconeMeteo;

    private static int screenPosition;

    private static FragmentActivity activity;
    private static String city;

    public WeatherFragment(){};

    public WeatherFragment(int listValue,String town){
        screenPosition = listValue;
        city = town;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        champVille = (TextView)rootView.findViewById(R.id.city_field);
        champMAJ = (TextView)rootView.findViewById(R.id.updated_field);
        champDetail = (TextView)rootView.findViewById(R.id.details_field);
        champDetailWeather = (TextView)rootView.findViewById(R.id.detailWeather);
        champTemp = (TextView)rootView.findViewById(R.id.current_temperature_field);
        IconeMeteo = (TextView)rootView.findViewById(R.id.weather_icon);

        IconeMeteo.setTypeface(weatherFont);
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (FragmentActivity) this.getContext();
        weatherFont = Typeface.createFromAsset(activity.getAssets(), "fonts/weatherIcon.ttf");
        new CitySaved(activity).setCity(city);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        majChamps();
    }


   /* public void changeCity(String city){
        majMeteo(city);
    } */


    public static void majChamps() {
        Log.d("async", new CitySaved(activity).getCity());
        if (Forecast.getWeatherList(new CitySaved(activity).getCity()) != null) {
            if (Forecast.getWeatherList(new CitySaved(activity).getCity()).size() > screenPosition) {
                //using of the class Forecast with the only useful date
                ArrayList<Weather> forecastsReader = Forecast.getWeatherList(new CitySaved(activity).getCity());
                Log.d("testForecast", String.valueOf(screenPosition));
                champVille.setText(new CitySaved(activity).getCity());
                champMAJ.setText(forecastsReader.get(screenPosition).getUpdateTime());
                champDetailWeather.setText(forecastsReader.get(screenPosition).getTimeDescription());
                champDetail.setText("Humidity : " + forecastsReader.get(screenPosition).getHumidity()
                        + "%\nPressure : " + forecastsReader.get(screenPosition).getPressure() + " Pa");
                champTemp.setText(String.valueOf(forecastsReader.get(screenPosition).getTemperature()) + " °C");
                IconeMeteo.setText(forecastsReader.get(screenPosition).getIcone());
            }
        }else{
            Toast.makeText(activity,"Connection problem, sorry", Toast.LENGTH_LONG).show();
        }
    }
}
