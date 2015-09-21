package com.example.den.converterlab.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.den.converterlab.activities.MainActivity;
import com.example.den.converterlab.models.Currenci;
import com.example.den.converterlab.models.Currencies;
import com.example.den.converterlab.models.DataRoot;
import com.example.den.converterlab.models.Maps;
import com.example.den.converterlab.models.Organizations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Den on 15.09.15.
 */
public class LoadDataToModelsFromJson {

    private static final String CURRENCIES = "currencies";
    private static final String CITIES = "cities";
    private static final String REGIONS = "regions";
    private static final String ORG_TYPE = "orgTypes";
    private static final String ORGANIZATIONS = "organizations";
    private static final String URL = "http://resources.finance.ua/ru/public/currency-cash.json";

//    TODO:отрефакторить этот метод
    public DataRoot getFillDataRoot(Context context) throws JSONException,IOException {

        String jsonString           = null;
        try {
            jsonString = getJsonFromUrl(URL,context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataRoot dataRoot           = new DataRoot();

        GsonBuilder builder         = new GsonBuilder();
        Gson gson                   = builder.create();

        JSONObject jsonObject       = new JSONObject(jsonString);

        JSONArray jsonOrganizations = jsonObject.getJSONArray(ORGANIZATIONS);

        Maps map                    = new Maps();
        map                         = getAllMaps(gson, jsonObject);

        dataRoot.setMap(map);

        String data = jsonObject.getString("date");
        dataRoot.setDate(data);

        List<Organizations> organizationList=new ArrayList<>();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        for (int i = 0; i < jsonOrganizations.length(); i++){

            Organizations organizations = gson.fromJson(String.valueOf(jsonOrganizations
                    .getJSONObject(i)), Organizations.class);

            organizations.setCurrencies(getFillCurrencies(gson, jsonObject, map, i));
            organizationList.add(organizations);

            initNotification(context, jsonOrganizations, notificationManager, i);

        }
        notificationManager.cancelAll();

        dataRoot.setOrganizationsList(organizationList);
        return dataRoot;
    }

    private void initNotification(Context context, JSONArray jsonOrganizations, NotificationManagerCompat notificationManager, int i) {
        int percentage = 100 * i / jsonOrganizations.length();

        String notificationText = String.valueOf((int) (percentage))
                + " %";
        NotificationCompat.Builder builderNot = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentText(notificationText);
        builderNot.setProgress(100, percentage, false);
        builderNot.setOngoing(true);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Notification.FLAG_ONGOING_EVENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        builderNot.setContentIntent(pendingIntent);

        Notification notification = builderNot.build();
        notificationManager.notify(1, notification);
    }

    private Currencies getFillCurrencies( Gson gson, JSONObject jsonObject, Maps map, int indx) throws JSONException {

        Currencies currencies = new Currencies();
        currencies.setCurrencyList(getCurrenciList(gson, jsonObject, map, indx));
        return currencies;
    }

    private List<Currenci> getCurrenciList(Gson gson, JSONObject jsonObject, Maps map, int indx) throws JSONException {

        JSONObject jsonCurrency = jsonObject
                .getJSONArray(ORGANIZATIONS)
                .getJSONObject(indx)
                .getJSONObject(CURRENCIES);

        List<Currenci> list = new ArrayList<>();

        Map<String,String> mapCurrencies = new HashMap<>();
        mapCurrencies = getMap(jsonObject, gson, CURRENCIES);

        int j = 0;
        for(String key : mapCurrencies.keySet()) {
            if(jsonCurrency.has(key)){

                Currenci currenci = gson.fromJson(String.valueOf(jsonCurrency
                        .getJSONObject(key)), Currenci.class);

                list.add(currenci);
                list.get(j).setIdCurrency(key);
                list.get(j).setNameCurrency(mapCurrencies.get(key));
                j++;
            }
        }
        return list;
    }

    private String getJsonFromUrl(String _url, Context context) throws IOException {
        URL url = new URL(_url);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(10000);
        conn.setRequestMethod("GET");

        int respCode = conn.getResponseCode();
        if (respCode == 200) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
        return null;
    }


    private Maps getAllMaps(Gson gson, JSONObject jsonObject) throws JSONException {
        Maps maps = new Maps();
        maps.setMapCities(getMap(jsonObject, gson, CITIES));
        maps.setMapCurrencies(getMap(jsonObject, gson, CURRENCIES));
        maps.setMapRegions(getMap(jsonObject, gson, REGIONS));
        maps.setMapOrgTypes(getMap(jsonObject, gson, ORG_TYPE));

        return maps;
    }

    private Map<String, String> getMap(JSONObject jsonObject, Gson gson, String key) throws JSONException {

        Map<String, String> map;
        Type mapType = new TypeToken<Map>() {}.getType();

        map = gson.fromJson((jsonObject.getJSONObject(key)).toString(), mapType);

        return map;
    }
}
