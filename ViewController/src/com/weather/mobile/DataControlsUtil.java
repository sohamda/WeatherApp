package com.weather.mobile;

import java.util.ArrayList;

import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.exception.AdfInvocationException;

public class DataControlsUtil {
    public DataControlsUtil() {
        super();
    }
    
    public static final Object invokeOnDataControl(String methodName, ArrayList<String> paramNames,  ArrayList<Object> paramValues, ArrayList<Class> paramTypes){
              
         Object outcome  = null;    
         
         try {            
             outcome = AdfmfJavaUtilities.invokeDataControlMethod("WeatherAppDC", null,methodName,paramNames, paramValues, paramTypes);
             return outcome;          
             
         } catch (AdfInvocationException e) {
             e.printStackTrace();
             return outcome;
         }
     }
}
