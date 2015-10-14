package com.weather.application.response.getweather;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class Body {
    
    private GetWeatherResponse GetWeatherResponse;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Body() {
        super();
    }

    public void setGetWeatherResponse(GetWeatherResponse GetWeatherResponse) {
        GetWeatherResponse oldGetWeatherResponse = this.GetWeatherResponse;
        this.GetWeatherResponse = GetWeatherResponse;
        propertyChangeSupport.firePropertyChange("GetWeatherResponse", oldGetWeatherResponse, GetWeatherResponse);
    }

    public GetWeatherResponse getGetWeatherResponse() {
        return GetWeatherResponse;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
