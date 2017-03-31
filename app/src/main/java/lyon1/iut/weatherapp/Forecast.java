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
        String previousDate = "";
        int hoursToTest;
        String dateToTest;
        for(int i = 0; i < weatherData.size(); i++)
        {
            dateToTest = weatherData.get(i).getUpdateTimeToString().substring(0,2);
            hoursToTest = Integer.valueOf(weatherData.get(i).getUpdateTimeToString().substring(12,14));
            Log.d("testHour", "i="+i+ " " + weatherData.get(i).getUpdateTimeToString() + " dateAct:"+dateToTest+" datePrec:"+previousDate+ " hour:"+hoursToTest);
            if(dateToTest.equals(previousDate) || hoursToTest < 8 || hoursToTest > 20) //one data each day between 8am and 20pm
            {
                Log.d("testHour", "suppr : " + weatherData.get(i).getUpdateTimeToString() + " dateAct:"+dateToTest+" datePrec:"+previousDate+ " hour:"+hoursToTest);
                weatherData.remove(i);
            }else {
                previousDate = dateToTest;
            }
            }

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
