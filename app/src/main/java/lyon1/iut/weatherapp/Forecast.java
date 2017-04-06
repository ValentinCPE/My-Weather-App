package lyon1.iut.weatherapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by valentin on 30/03/17.
 */

public class Forecast {
    private static HashMap<String,ArrayList<Weather>> forecastCity = new HashMap<>();
    private static ArrayList<String> cityHistory = new ArrayList<>();

    public static void addForecast(String ville, ArrayList<Weather> weatherData)
    {
        ville = ville.toLowerCase();
        forecastCity.put(ville,weatherData);

        boolean exist = false;

        if(cityHistory.size() > 0) {
            for (int i = 0; i < cityHistory.size() && !exist; i++) {
                if (cityHistory.get(i).equals(ville)) {
                    exist = true;
                }
            }
        }

        if(!exist){
            cityHistory.add(ville);
        }


    }

    public static HashMap<String,ArrayList<Weather>> getForecast(){
        return forecastCity;
    }

    public static ArrayList<Weather> getWeatherList(String ville)
    {
        return forecastCity.get(ville);
    }

    public static ArrayList<String> getCityHistory(){
        return cityHistory;
    }

}
