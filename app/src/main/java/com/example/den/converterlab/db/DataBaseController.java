package com.example.den.converterlab.db;

import com.example.den.converterlab.models.Currenci;
import com.example.den.converterlab.models.DataRoot;
import com.example.den.converterlab.models.Organizations;

import java.util.List;
import java.util.Map;

/**
 * Created by Den on 15.09.15.
 */
public interface DataBaseController {
    void addOrganizations(List<Organizations> organizationsList);
    void addCurrencies(Map<String, String> currenciesMap);
    void addCourse(List<Organizations> organizationsList);
    void addOrgTypes(Map<String, String> orgTypesMap);
    void addRegions(Map<String, String> regionsMap);
    void addCities(Map<String, String> citiesMap);
    void addDate(String date);
//    TODO: Getters for structure
    List<Organizations>  getOrganizationsListFromDB();
    Organizations getOrganizationFromDB(String key);
    List<Currenci> getCurrencyListFromDB(String key);
    String getDate();
}
