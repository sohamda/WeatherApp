package com.weather.application.response.getcities;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GetCitiesSAXHandler extends DefaultHandler {
    
    private List<String> cities = new ArrayList<>();
    private Boolean citiesTag = Boolean.FALSE;
    
    public GetCitiesSAXHandler() {
        super();
    }
    
    public List<String> getCities() {
        return cities;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        if(qName.equals("City")) {
            citiesTag = Boolean.TRUE;
        } else {
            citiesTag = Boolean.FALSE;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        if(citiesTag) {
            String city = new String(ch, start, length);
            if(city != "" && city.trim().length() > 0) {
                cities.add(city);
            }
        }
    }
}
