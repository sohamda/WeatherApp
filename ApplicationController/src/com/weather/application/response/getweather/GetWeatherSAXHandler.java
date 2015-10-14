package com.weather.application.response.getweather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GetWeatherSAXHandler extends DefaultHandler {
    
    private CurrentWeather currentWeather;
    private boolean locationTag = false;
    private boolean timeTag = false;
    private boolean windTag = false;
    private boolean visibilityTag = false;
    private boolean skyConditionTag = false;
    private boolean temperatureTag = false;
    private boolean dewPointTag = false;
    private boolean humidityTag = false;
    private boolean pressureTag = false;
    
    public GetWeatherSAXHandler() {
        super();
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        
        if(qName.equals("CurrentWeather")) {
            this.currentWeather = new CurrentWeather();
        }        
        
        if(qName.equals("Location")) {
            locationTag = true;
        } else {
            if(qName.equals("Time")) {
                timeTag = true;
            } else {
                if(qName.equals("Wind")) {
                    windTag = true;
                } else {
                    if(qName.equals("Visibility")) {
                        visibilityTag = true;
                    } else {
                        if(qName.equals("SkyConditions")) {
                            skyConditionTag = true;
                        } else {
                            if(qName.equals("Temperature")) {
                                temperatureTag = true;
                            } else {
                                if(qName.equals("DewPoint")) {
                                    dewPointTag = true;
                                } else {
                                    if(qName.equals("RelativeHumidity")) {
                                        humidityTag = true;
                                    } else {
                                        if(qName.equals("Pressure")) {
                                            pressureTag = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        if(locationTag) {
            String location = new String(ch, start, length);
            if(location != "" && location.trim().length() > 0) {
                currentWeather.setLocation(location);
            }
            locationTag = false;
        }
        
        if(timeTag) {
            String time = new String(ch, start, length);
            if(time != "" && time.trim().length() > 0) {
                currentWeather.setTime(time);
            }
            timeTag = false;
        }
        
        if(windTag) {
            String wind = new String(ch, start, length);
            if(wind != "" && wind.trim().length() > 0) {
                currentWeather.setWind(wind);
            }
            windTag = false;
        }
        
        if(visibilityTag) {
            String visibility = new String(ch, start, length);
            if(visibility != "" && visibility.trim().length() > 0) {
                currentWeather.setVisibility(visibility);
            }
            visibilityTag = false;
        }
        
        if(skyConditionTag) {
            String skyCondition = new String(ch, start, length);
            if(skyCondition != "" && skyCondition.trim().length() > 0) {
                currentWeather.setSkyConditions(skyCondition);
            }
            skyConditionTag = false;
        }
        
        if(temperatureTag) {
            String temperature = new String(ch, start, length);
            if(temperature != "" && temperature.trim().length() > 0) {
                currentWeather.setTemperature(temperature);
            }
            temperatureTag = false;
        }
        
        if(dewPointTag) {
            String dewPoint = new String(ch, start, length);
            if(dewPoint != "" && dewPoint.trim().length() > 0) {
                currentWeather.setDewPoint(dewPoint);
            }
            dewPointTag = false;
        }
        
        if(humidityTag) {
            String humidity = new String(ch, start, length);
            if(humidity != "" && humidity.trim().length() > 0) {
                currentWeather.setRelativeHumidity(humidity);
            }
            humidityTag = false;
        }
        
        if(pressureTag) {
            String pressure = new String(ch, start, length);
            if(pressure != "" && pressure.trim().length() > 0) {
                currentWeather.setPressure(pressure);
            }
            pressureTag = false;
        }
    }
}
