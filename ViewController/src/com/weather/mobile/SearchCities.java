package com.weather.mobile;

import java.util.ArrayList;

import oracle.adfmf.amx.event.ValueChangeEvent;

public class SearchCities {
    private String countryName;

    public SearchCities() {
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public String searchForCities() {
        
        ArrayList<String>   paramNames = new ArrayList<String>();        
        paramNames.add("countryName");        
        
        ArrayList<Object>   paramValues = new ArrayList<Object>();        
        paramValues.add(getCountryName().trim()); 
        
        ArrayList<Class> paramTypes = new ArrayList<Class>();
        paramTypes.add(String.class);
        
        DataControlsUtil.invokeOnDataControl("invokeGetCitiesAPI", paramNames, paramValues, paramTypes); 
        
        return null;
    }

    public void countryNameChanged(ValueChangeEvent valueChangeEvent) {
        setCountryName(((String) valueChangeEvent.getNewValue()));
    }
}
