package com.weather.application.datacontrol;

import com.oracle.maf.sample.mcs.apis.custom.CustomAPI;
import com.oracle.maf.sample.mcs.shared.authorization.auth.Authorization;
import com.oracle.maf.sample.mcs.shared.exceptions.ServiceProxyException;
import com.oracle.maf.sample.mcs.shared.log.MBELogger;
import com.oracle.maf.sample.mcs.shared.mafrest.MCSRequest;
import com.oracle.maf.sample.mcs.shared.mafrest.MCSResponse;
import com.oracle.maf.sample.mcs.shared.mbe.MBE;
import com.oracle.maf.sample.mcs.shared.mbe.MBEConfiguration;
import com.oracle.maf.sample.mcs.shared.mbe.MBEManager;

import com.oracle.maf.sample.mcs.shared.mbe.error.OracleMobileError;
import com.oracle.maf.sample.mcs.shared.mbe.error.OracleMobileErrorHelper;

import com.weather.application.response.getcities.GetCitiesResponse;

import com.weather.application.response.getcities.GetCitiesSAXHandler;

import com.weather.application.response.getweather.CurrentWeather;

import com.weather.application.response.getweather.CurrentWeatherResponse;

import com.weather.application.response.getweather.GetWeatherSAXHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import oracle.adf.model.datacontrols.device.DeviceManagerFactory;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.api.JSONBeanSerializationHelper;
import oracle.adfmf.java.beans.PropertyChangeListener;
import oracle.adfmf.java.beans.PropertyChangeSupport;
import oracle.adfmf.java.beans.ProviderChangeListener;
import oracle.adfmf.java.beans.ProviderChangeSupport;
import oracle.adfmf.json.JSONException;

import org.xml.sax.SAXException;

public class WeatherAppDC {

    private MBE mobileBackend;
    private MBELogger logger = null;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private ProviderChangeSupport providerChangeSupport = new ProviderChangeSupport(this);

    private List<String> citiesForCountry = new ArrayList<>();
    private CurrentWeather currentWeather;

    public WeatherAppDC() {
        super();
    }

    public void invokeGetWeatherAPI(String countryName, String cityName) {

        this.resetMessages();
        Runnable mcsJob = new Runnable() {

            public void run() {
                try {

                    //get access to the service proxy for custom API calls
                    CustomAPI customApi = mobileBackend.getServiceProxyCustomApi();

                    //MAF MCS Utility provides to configuration objects, MCSRequest and MCSResponse, that guide you
                    //through setting up the MCS request and to read the MCS response. First, a MCSRequest object is
                    //created
                    MCSRequest request = new MCSRequest(mobileBackend.getMbeConfiguration());

                    //the MSF REST connection as defined in Application Resources --> Connections --> REST. In this
                    //public sample the REST connection name is "MCSUTILRESTCONN"
                    request.setConnectionName("MCSRESTCONN");

                    //save the custom MCS API URI
                    request.setRequestURI("/mobile/custom/WeatherAPI/getweather?country=" + countryName + "&city=" +
                                          cityName);

                    //set the request method
                    request.setHttpMethod("GET");
                    //ensure null is translated to "" as we need a string
                    request.setPayload("");
                    request.setRetryLimit(0);

                    //define the headers that need to be send with the custom API request
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    request.setHttpHeaders(headers);

                    //this call returns a String response. For binary responses (binary[]) you use
                    //customApi.sendReceiveBytes(request) instead.
                    MCSResponse response = customApi.sendForStringResponse(request);
                    logger.logFine((String) response.getMessage(), this.getClass().getSimpleName(), "jsonResponse");
                    String jsonResponse = (String) response.getMessage();
                    setWeather(jsonResponse);

                } catch (ServiceProxyException e) {
                    logger.logError(e.getMessage(), this.getClass().getSimpleName(), "josnResponseFailure");
                    if (e.isApplicationError()) {
                        //if this is a well formatted Oracle Mobile error, we can display a user friendly error message
                        try {
                            OracleMobileError mobileError =
                                OracleMobileErrorHelper.getMobileErrorObject(e.getMessage());
                            //print short description of error
                            logDcErrorForUserInterfaceDisplay(mobileError.getTitle());
                        } catch (JSONException f) {
                            logDcErrorForUserInterfaceDisplay(e.getMessage());
                        }
                    } else {
                        logDcErrorForUserInterfaceDisplay(e.getMessage());
                    }
                }
                providerChangeSupport.fireProviderRefresh("currentWeather");
                //ensure main thread is synchronized with result
                AdfmfJavaUtilities.flushDataChangeEvent();
            }
        };


        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(mcsJob);
        executor.shutdown();
    }

    public void invokeGetCitiesAPI(String countryName) {

        this.resetMessages();
        anonymousLogin();
        Runnable mcsJob = new Runnable() {

            public void run() {
                try {
                    CustomAPI customApi = mobileBackend.getServiceProxyCustomApi();

                    MCSRequest request = new MCSRequest(mobileBackend.getMbeConfiguration());

                    request.setConnectionName("MCSRESTCONN");

                    request.setRequestURI("/mobile/custom/WeatherAPI/getCities?country=" + countryName);

                    request.setHttpMethod("GET");
                    request.setPayload("");
                    request.setRetryLimit(0);

                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    request.setHttpHeaders(headers);

                    MCSResponse response = customApi.sendForStringResponse(request);
                    logger.logFine((String) response.getMessage(), this.getClass().getSimpleName(), "jsonResponse");
                    String jsonResponse = (String) response.getMessage();
                    setCities(jsonResponse);

                } catch (ServiceProxyException e) {
                    logger.logError(e.getMessage(), this.getClass().getSimpleName(), "josnResponseFailure");
                    if (e.isApplicationError()) {
                        //if this is a well formatted Oracle Mobile error, we can display a user friendly error message
                        try {
                            OracleMobileError mobileError =
                                OracleMobileErrorHelper.getMobileErrorObject(e.getMessage());
                            //print short description of error
                            logDcErrorForUserInterfaceDisplay(mobileError.getTitle());
                        } catch (JSONException f) {
                            logDcErrorForUserInterfaceDisplay(e.getMessage());
                        }
                    } else {
                        logDcErrorForUserInterfaceDisplay(e.getMessage());
                    }
                }
                providerChangeSupport.fireProviderRefresh("citiesForCountry");
                //ensure main thread is synchronized with result
                AdfmfJavaUtilities.flushDataChangeEvent();
            }
        };


        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.execute(mcsJob);
        executor.shutdown();
    }

    private void setCities(String citiesForCountry) {

        JSONBeanSerializationHelper jbsh = new JSONBeanSerializationHelper();
        GetCitiesResponse getCitiesResponse = null;

        try {
            getCitiesResponse = (GetCitiesResponse) jbsh.fromJSON(GetCitiesResponse.class, citiesForCountry);
        } catch (Exception e) {
            logger.logError(e.getMessage(), this.getClass().getSimpleName(), "josnToClassFailure");
            e.printStackTrace();
        }
        List<String> getCities = null;
        if (getCitiesResponse != null) {
            try {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                GetCitiesSAXHandler getCitiesSAXHandler = new GetCitiesSAXHandler();

                logger.logFine(getCitiesResponse.getBody().getGetCitiesByCountryResponse().getGetCitiesByCountryResult(),
                               this.getClass().getSimpleName(), "josnToClassFailure");
                InputStream is =
                    new ByteArrayInputStream(getCitiesResponse.getBody().getGetCitiesByCountryResponse().getGetCitiesByCountryResult().getBytes());
                saxParser.parse(is, getCitiesSAXHandler);
                getCities = getCitiesSAXHandler.getCities();
            } catch (SAXException | ParserConfigurationException | IOException exp) {
                logger.logError(exp.getMessage(), this.getClass().getSimpleName(), "SAXParsingFailure");
                exp.printStackTrace();
            }

            setCitiesForCountry(getCities);

        }

    }

    private void setWeather(String weatherResponse) {

        JSONBeanSerializationHelper jbsh = new JSONBeanSerializationHelper();
        CurrentWeatherResponse currentWeatherResponse = null;

        try {
            currentWeatherResponse =
                (CurrentWeatherResponse) jbsh.fromJSON(CurrentWeatherResponse.class, weatherResponse);
        } catch (Exception e) {
            logger.logError(e.getMessage(), this.getClass().getSimpleName(), "josnToClassFailure");
            e.printStackTrace();
        }

        if (currentWeatherResponse != null) {
            try {
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                GetWeatherSAXHandler getWeatherSAXHandler = new GetWeatherSAXHandler();
                String currentWeatherXML =
                    currentWeatherResponse.getBody().getGetWeatherResponse().getGetWeatherResult();
                currentWeatherXML =
                    currentWeatherXML.substring(currentWeatherXML.indexOf("<CurrentWeather>"),
                                                currentWeatherXML.length());
                logger.logFine(currentWeatherXML, this.getClass().getSimpleName(), "josnToClassFailure");
                InputStream is = new ByteArrayInputStream(currentWeatherXML.getBytes());
                saxParser.parse(is, getWeatherSAXHandler);

                setCurrentWeather(getWeatherSAXHandler.getCurrentWeather());
            } catch (SAXException | ParserConfigurationException | IOException exp) {
                logger.logError(exp.getMessage(), this.getClass().getSimpleName(), "SAXParsingFailure");
                exp.printStackTrace();
            }
        }
    }

    public void setCitiesForCountry(List<String> citiesForCountry) {

        List<String> oldCitiesForCountry = this.citiesForCountry;
        this.citiesForCountry = citiesForCountry;
        propertyChangeSupport.firePropertyChange("citiesForCountry", oldCitiesForCountry, citiesForCountry);
    }

    public List<String> getCitiesForCountry() {
        return citiesForCountry;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        CurrentWeather oldCurrentWeather = this.currentWeather;
        this.currentWeather = currentWeather;
        propertyChangeSupport.firePropertyChange("currentWeather", oldCurrentWeather, currentWeather);
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    public void addProviderChangeListener(ProviderChangeListener l) {
        providerChangeSupport.addProviderChangeListener(l);
    }

    public void removeProviderChangeListener(ProviderChangeListener l) {
        providerChangeSupport.removeProviderChangeListener(l);
    }

    public MBE getMobileBackend() {

        if (mobileBackend == null) {
            String mobileBackendUrl =
                (String) AdfmfJavaUtilities.getELValue("#{preferenceScope.application.mcs.mobileBackendURL}");
            //clear
            AdfmfJavaUtilities.clearSecurityConfigOverrides("MCSRESTCONN");
            //override
            AdfmfJavaUtilities.overrideConnectionProperty("MCSRESTCONN", "restconnection", "url", mobileBackendUrl);


            String mobileBackendId =
                (String) AdfmfJavaUtilities.getELValue("#{preferenceScope.application.mcs.mobileBackendId}");
            String mbeAnonymousKey =
                (String) AdfmfJavaUtilities.getELValue("#{preferenceScope.application.mcs.mbeAnonymousKey}");
            String appKeyAndroid =
                (String) AdfmfJavaUtilities.getELValue("#{preferenceScope.application.mcs.mobileBackendApplicationKeyAndroid}");


            MBEConfiguration mbeConfiguration =
                new MBEConfiguration("MCSRESTCONN", mobileBackendId, mbeAnonymousKey, appKeyAndroid,
                                     MBEConfiguration.AuthenticationType.BASIC_AUTH);

            //enable analytics for MBE
            mbeConfiguration.setEnableAnalytics(true);

            mbeConfiguration.setLoggingEnabled(true);

            mbeConfiguration.setMobileDeviceId(DeviceManagerFactory.getDeviceManager().getName());
            this.mobileBackend = MBEManager.getManager().createOrRenewMobileBackend(mobileBackendId, mbeConfiguration);
            this.logger = mobileBackend.getMbeConfiguration().getLogger();
        }

        return mobileBackend;
    }

    public Boolean anonymousLogin() {
        Boolean authenticationSuccess = Boolean.FALSE;
        //reset existing DC messages in UI
        resetMessages();

        if (getMobileBackend() != null) {
            mobileBackend.getMbeConfiguration().getLogger().logFine("Anonymous authentication invoked from DC",
                                                                    this.getClass().getSimpleName(), "anonymousLogin");
            Authorization authorization = this.mobileBackend.getAuthorizationProvider();
            try {
                authorization.authenticateAsAnonymous();
                AdfmfJavaUtilities.setELValue("#{applicationScope.mafmcsutilauthenticateduser}", "anonymous");
                authenticationSuccess = Boolean.TRUE;
            } catch (ServiceProxyException e) {
                authenticationSuccess = Boolean.FALSE;

                //is exception dur to an MCS response error ?
                if (e.isApplicationError()) {
                    //if this is a well formatted Oracle Mobile error, we can display a user friendly error message
                    try {
                        OracleMobileError mobileError = OracleMobileErrorHelper.getMobileErrorObject(e.getMessage());
                        //print short description of error
                        logDcErrorForUserInterfaceDisplay(mobileError.getTitle());
                    } catch (JSONException f) {
                        logDcErrorForUserInterfaceDisplay(e.getMessage());
                    }
                } else {
                    logDcErrorForUserInterfaceDisplay(e.getMessage());
                }
            }
        }
        return authenticationSuccess;
    }

    private void resetMessages() {
        AdfmfJavaUtilities.setELValue("#{applicationScope.dataControlError}", "");
    }

    private void logDcErrorForUserInterfaceDisplay(String message) {
        AdfmfJavaUtilities.setELValue("#{applicationScope.dataControlError}", message);
    }
}
