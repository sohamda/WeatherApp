package com.weather.application.response.getweather;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class GetWeatherResponse {
    
    private String GetWeatherResult;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void setGetWeatherResult(String GetWeatherResult) {
        String oldGetWeatherResult = this.GetWeatherResult;
        this.GetWeatherResult = GetWeatherResult;
        propertyChangeSupport.firePropertyChange("GetWeatherResult", oldGetWeatherResult, GetWeatherResult);
    }

    public String getGetWeatherResult() {
        return GetWeatherResult;
    }

    public GetWeatherResponse() {
        super();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
