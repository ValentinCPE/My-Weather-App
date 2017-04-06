package lyon1.iut.weatherapp;

import android.app.Activity;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by valentin on 29/03/17.
 */

public class Weather {
    private String updateTime;
    private String icone;
    private String humidity;
    private String pressure;
    private Double temperature;
    private String timeDescription;


    public Weather(){
        this.updateTime = null;
        this.icone = null;
        this.humidity = null;
        this.pressure = null;
        this.temperature = null;
    }

    public Weather(String update, String icon, String humidity, String pressure, Double tmp, String timeDes)
    {
        this.updateTime = update;
        this.icone = icon;
        this.humidity = humidity;
        this.pressure = pressure;
        this.temperature = tmp;
        this.timeDescription = timeDes;
    }


    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(int actualId, Activity iconAsset) {
        this.setWeatherIcon(actualId, iconAsset);
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getTimeDescription() {
        return timeDescription;
    }

    public void setTimeDescription(String timeDescription) {
        this.timeDescription = timeDescription;
    }

    private void setWeatherIcon(int actualId, Activity iconAsset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
                icon = iconAsset.getString(R.string.weather_sunny);
        } else {
            switch(id) {
                case 2 : icon = iconAsset.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = iconAsset.getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = iconAsset.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = iconAsset.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = iconAsset.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = iconAsset.getString(R.string.weather_rainy);
                    break;
            }
        }
        this.icone = icon;
    }
}
