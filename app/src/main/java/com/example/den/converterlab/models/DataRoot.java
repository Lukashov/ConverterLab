package com.example.den.converterlab.models;

import java.util.List;

/**
 * Created by Den on 13.09.15.
 */
public class DataRoot {
    private List<Organizations> organizationsList;
    private Maps map;
    private String date;

    public List<Organizations> getOrganizationsList() {
        return organizationsList;
    }

    public void setOrganizationsList(List<Organizations> organizationsList) {
        this.organizationsList = organizationsList;
    }

    public Maps getMap() {
        return map;
    }

    public void setMap(Maps map) {
        this.map = map;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
