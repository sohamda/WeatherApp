package com.weather.application.response.getweather;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class CurrentWeather {
    
    private String location;
    private String time;
    private String wind;
    private String visibility;
    private String skyConditions;
    private String temperature;
    private String dewPoint;
    private String relativeHumidity;
    private String pressure;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public CurrentWeather() {
        super();
    }

    public void setLocation(String location) {
        String oldLocation = this.location;
        this.location = location;
        propertyChangeSupport.firePropertyChange("location", oldLocation, location);
    }

    public String getLocation() {
        return location;
    }

    public void setTime(String time) {
        String oldTime = this.time;
        this.time = time;
        propertyChangeSupport.firePropertyChange("time", oldTime, time);
    }

    public String getTime() {
        return time;
    }

    public void setWind(String wind) {
        String oldWind = this.wind;
        this.wind = wind;
        propertyChangeSupport.firePropertyChange("wind", oldWind, wind);
    }

    public String getWind() {
        return wind;
    }

    public void setVisibility(String visibility) {
        String oldVisibility = this.visibility;
        this.visibility = visibility;
        propertyChangeSupport.firePropertyChange("visibility", oldVisibility, visibility);
    }

    public String getVisibility() {
        return visibility;
    }

    public void setSkyConditions(String skyConditions) {
        String oldSkyConditions = this.skyConditions;
        this.skyConditions = skyConditions;
        propertyChangeSupport.firePropertyChange("skyConditions", oldSkyConditions, skyConditions);
    }

    public String getSkyConditions() {
        return skyConditions;
    }

    public void setTemperature(String temperature) {
        String oldTemperature = this.temperature;
        this.temperature = temperature;
        propertyChangeSupport.firePropertyChange("temperature", oldTemperature, temperature);
    }

    public String getTemperature() {
        return temperature;
    }

    public void setDewPoint(String dewPoint) {
        String oldDewPoint = this.dewPoint;
        this.dewPoint = dewPoint;
        propertyChangeSupport.firePropertyChange("dewPoint", oldDewPoint, dewPoint);
    }

    public String getDewPoint() {
        return dewPoint;
    }

    public void setRelativeHumidity(String relativeHumidity) {
        String oldRelativeHumidity = this.relativeHumidity;
        this.relativeHumidity = relativeHumidity;
        propertyChangeSupport.firePropertyChange("relativeHumidity", oldRelativeHumidity, relativeHumidity);
    }

    public String getRelativeHumidity() {
        return relativeHumidity;
    }

    public void setPressure(String pressure) {
        String oldPressure = this.pressure;
        this.pressure = pressure;
        propertyChangeSupport.firePropertyChange("pressure", oldPressure, pressure);
    }

    public String getPressure() {
        return pressure;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
