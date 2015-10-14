package com.weather.application.response.getcities;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class Body {
    
    private GetCitiesByCountryResponse GetCitiesByCountryResponse;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void setGetCitiesByCountryResponse(GetCitiesByCountryResponse GetCitiesByCountryResponse) {
        GetCitiesByCountryResponse oldGetCitiesByCountryResponse = this.GetCitiesByCountryResponse;
        this.GetCitiesByCountryResponse = GetCitiesByCountryResponse;
        propertyChangeSupport.firePropertyChange("GetCitiesByCountryResponse", oldGetCitiesByCountryResponse,
                                                 GetCitiesByCountryResponse);
    }

    public GetCitiesByCountryResponse getGetCitiesByCountryResponse() {
        return GetCitiesByCountryResponse;
    }

    public Body() {
        super();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
