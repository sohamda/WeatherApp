package com.weather.application.response.getcities;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class GetCitiesByCountryResponse {
    
    private String GetCitiesByCountryResult;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void setGetCitiesByCountryResult(String GetCitiesByCountryResult) {
        String oldGetCitiesByCountryResult = this.GetCitiesByCountryResult;
        this.GetCitiesByCountryResult = GetCitiesByCountryResult;
        propertyChangeSupport.firePropertyChange("GetCitiesByCountryResult", oldGetCitiesByCountryResult,
                                                 GetCitiesByCountryResult);
    }

    public String getGetCitiesByCountryResult() {
        return GetCitiesByCountryResult;
    }


    public GetCitiesByCountryResponse() {
        super();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
