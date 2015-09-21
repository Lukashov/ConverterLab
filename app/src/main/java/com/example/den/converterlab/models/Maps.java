package com.example.den.converterlab.models;

import java.util.Map;

/**
 * Created by Den on 14.09.15.
 */
public class Maps {
    private Map<String,String> mapCurrencies;
    private Map<String,String> mapOrgTypes;
    private Map<String,String> mapRegions;
    private Map<String,String> mapCities;

    public Map<String, String> getMapCurrencies() {
        return mapCurrencies;
    }

    public void setMapCurrencies(Map<String, String> mapCurrencies) {
        this.mapCurrencies = mapCurrencies;
    }

    public Map<String, String> getMapOrgTypes() {
        return mapOrgTypes;
    }

    public void setMapOrgTypes(Map<String, String> mapOrgTypes) {
        this.mapOrgTypes = mapOrgTypes;
    }

    public Map<String, String> getMapRegions() {
        return mapRegions;
    }

    public void setMapRegions(Map<String, String> mapRegions) {
        this.mapRegions = mapRegions;
    }

    public Map<String, String> getMapCities() {
        return mapCities;
    }

    public void setMapCities(Map<String, String> mapCities) {
        this.mapCities = mapCities;
    }
}
