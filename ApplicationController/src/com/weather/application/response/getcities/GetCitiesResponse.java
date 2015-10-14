package com.weather.application.response.getcities;

import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;

public class GetCitiesResponse {
    
    private Body Body;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void setBody(Body Body) {
        Body oldBody = this.Body;
        this.Body = Body;
        propertyChangeSupport.firePropertyChange("Body", oldBody, Body);
    }

    public Body getBody() {
        return Body;
    }

    public GetCitiesResponse() {
        super();
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
}
