package lyon1.iut.weatherapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by valentin on 30/03/17.
 */

public class Forecast {
    private static HashMap<String,ArrayList<Weather>> forecastCity = new HashMap<>();

    public static void addForecast(String ville, ArrayList<Weather> weatherData)
    {
        forecastCity.put(ville,weatherData);
    }

    public static HashMap<String,ArrayList<Weather>> getForecast(){
        return forecastCity;
    }

    public static ArrayList<Weather> getWeatherList(String ville)
    {
        return forecastCity.get(ville);
    }

}
