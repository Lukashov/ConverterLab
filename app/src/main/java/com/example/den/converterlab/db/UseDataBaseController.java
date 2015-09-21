package com.example.den.converterlab.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.den.converterlab.models.Currenci;
import com.example.den.converterlab.models.Currencies;
import com.example.den.converterlab.models.Organizations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Den on 15.09.15.
 */
public class UseDataBaseController implements DataBaseController {

    private DataBaseHelper mDataBaseHelper;

    private static final String SQL_QUERY = "SELECT tab1.title," +
                                           " tab1.phone," +
                                           " tab1.id," +
                                           " tab1.address," +
                                           " tab1.link," +
                                           " tab2.newValue," +
                                           " tab3.newValue," +
                                           " tab4.newValue" +
                                           " FROM organizations tab1" +
                                           " INNER JOIN" +
                                           " regions tab2 ON tab1.regionId = tab2._id" +
                                           " INNER JOIN" +
                                           " cities tab3 ON tab1.cityId = tab3._id" +
                                           " INNER JOIN" +
                                           " orgTypes tab4 ON tab1.orgType = tab4._id";


    public UseDataBaseController(DataBaseHelper mDataBaseHelper) {
        this.mDataBaseHelper = mDataBaseHelper;
    }

    @Override
    public void addOrganizations(List<Organizations> organizationsList) {

        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_ORGANIZATIONS,null,null);

        ContentValues values = new ContentValues();

        for (int i = 0; i < organizationsList.size(); i++) {
            values.put(DataBaseHelper.COLUMN_ID, organizationsList.get(i).getId());
            values.put(DataBaseHelper.COLUMN_OLD_ID, organizationsList.get(i).getOldId());
            values.put(DataBaseHelper.COLUMN_ORG_TYPE, organizationsList.get(i).getOrgType());
            values.put(DataBaseHelper.COLUMN_TITLE, organizationsList.get(i).getTitle());
            values.put(DataBaseHelper.COLUMN_REGION_ID, organizationsList.get(i).getRegionId());
            values.put(DataBaseHelper.COLUMN_CITY_ID, organizationsList.get(i).getCityId());
            values.put(DataBaseHelper.COLUMN_PHONE, organizationsList.get(i).getPhone());
            values.put(DataBaseHelper.COLUMN_ADDRESS, organizationsList.get(i).getAddress());
            values.put(DataBaseHelper.COLUMN_LINK, organizationsList.get(i).getLink());
            db.insert(DataBaseHelper.TABLE_ORGANIZATIONS, null, values);
        }
        db.close();
    }

    @Override
    public void addCurrencies(Map<String, String> currenciesMap) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_CURRENCIES,null,null);

        ContentValues values = new ContentValues();

        for(Map.Entry<String, String> map : currenciesMap.entrySet()) {
            values.put(DataBaseHelper.KEY_ID, map.getKey());
            values.put(DataBaseHelper.COLUMN_VALUE, map.getValue());
            db.insert(DataBaseHelper.TABLE_CURRENCIES, null, values);
        }
        db.close();
    }

    @Override
    public void addCourse(List<Organizations> organizationsList) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_COURSES,null,null);

        for (Organizations org : organizationsList) {
            for (Currenci currenci : org.getCurrencies().getCurrencyList()) {

                Cursor cursor = db.query(mDataBaseHelper.TABLE_COURSES,
                        new String[]{mDataBaseHelper.COLUMN_ASK_CURRENCY,
                                mDataBaseHelper.COLUMN_BID_CURRENCY},
                        mDataBaseHelper.COLUMN_ID_CURRENCY + "=? AND " +
                                mDataBaseHelper.COLUMN_ID_ORGANIZATIONS + "=?", new String[]
                                {currenci.getIdCurrency(), org.getId()}, null, null, null);
                try {
                    cursor.moveToFirst();

                    BigDecimal askDB = new BigDecimal(cursor.getString(0));
                    BigDecimal bidDB = new BigDecimal(cursor.getString(1));
                    BigDecimal askJS = new BigDecimal(currenci.getAsk());
                    BigDecimal bidJS = new BigDecimal(currenci.getBid());
                    if (askJS.compareTo(askDB) >= 0 || cursor.getString(0).isEmpty()) {
                        currenci.setAskFluc(1);
                    } else {
                        currenci.setAskFluc(0);
                    }
                    if (bidJS.compareTo(bidDB) >= 0 || cursor.getString(1).isEmpty()) {
                        currenci.setBidFluc(1);
                    } else {
                        currenci.setBidFluc(0);
                    }

                } catch (Exception e) {
                    currenci.setAskFluc(1);
                    currenci.setBidFluc(1);
                }
                finally {
                    cursor.close();
                }
            }
        }

        ContentValues values = new ContentValues();

        for (int i = 0; i < organizationsList.size(); i++) {
            List<Currenci> currenciList = organizationsList.get(i).getCurrencies().getCurrencyList();
            for (int j = 0; j < currenciList.size(); j++) {
                values.put(DataBaseHelper.COLUMN_ID_ORGANIZATIONS, organizationsList.get(i).getId());
                values.put(DataBaseHelper.COLUMN_ID_CURRENCY, currenciList.get(j).getIdCurrency());
                values.put(DataBaseHelper.COLUMN_NAME_CURRENCY, currenciList.get(j).getNameCurrency());
                values.put(DataBaseHelper.COLUMN_ASK_CURRENCY, currenciList.get(j).getAsk());
                values.put(DataBaseHelper.COLUMN_BID_CURRENCY, currenciList.get(j).getBid());
                values.put(DataBaseHelper.COLUMN_ASK_FLUC, currenciList.get(j).getAskFluc());
                values.put(DataBaseHelper.COLUMN_BID_FLUC, currenciList.get(j).getBidFluc());

                db.insert(DataBaseHelper.TABLE_COURSES, null, values);
            }

        }
        db.close();
    }

    @Override
    public void addOrgTypes(Map<String, String> orgTypesMap) {

        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_ORG_TYPES,null,null);

        ContentValues values = new ContentValues();

        for(Map.Entry<String, String> map : orgTypesMap.entrySet()) {
            values.put(DataBaseHelper.KEY_ID, map.getKey());
            values.put(DataBaseHelper.COLUMN_VALUE, map.getValue());
            db.insert(DataBaseHelper.TABLE_ORG_TYPES, null, values);
        }
        db.close();
    }

    @Override
    public void addRegions(Map<String, String> regionsMap) {

        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_REGIONS,null,null);

        ContentValues values = new ContentValues();

        for(Map.Entry<String, String> map : regionsMap.entrySet()) {
            values.put(DataBaseHelper.KEY_ID, map.getKey());
            values.put(DataBaseHelper.COLUMN_VALUE, map.getValue());
            db.insert(DataBaseHelper.TABLE_REGIONS, null, values);
        }
        db.close();
    }

    @Override
    public void addCities(Map<String, String> citiesMap) {

        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        db.delete(DataBaseHelper.TABLE_CITIES,null,null);

        ContentValues values = new ContentValues();

        for(Map.Entry<String, String> map : citiesMap.entrySet()) {
            values.put(DataBaseHelper.KEY_ID, map.getKey());
            values.put(DataBaseHelper.COLUMN_VALUE, map.getValue());
            db.insert(DataBaseHelper.TABLE_CITIES, null, values);
        }
        db.close();
    }

    @Override
    public void addDate(String date) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put(DataBaseHelper.COLUMN_DATE, date);
            db.insert(DataBaseHelper.TABLE_DATE, null, values);
        db.close();
    }

    @Override
    public List<Organizations> getOrganizationsListFromDB() {
        List<Organizations> organizationsList = new ArrayList<>();
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(SQL_QUERY,null);

        if (cursor.moveToFirst()) {
            do {
                Organizations organizations = new Organizations();
                organizations.setTitle(cursor.getString(0));
                organizations.setPhone(cursor.getString(1));
                organizations.setId(cursor.getString(2));
                organizations.setAddress(cursor.getString(3));
                organizations.setLink(cursor.getString(4));
                organizations.setRegionId(cursor.getString(5));
                organizations.setCityId(cursor.getString(6));
                organizations.setOrgType(cursor.getString(7));

                Currencies currencies = new Currencies();
                currencies.setCurrencyList(getCurrencyListFromDB(cursor.getString(2)));

                organizations.setCurrencies(currencies);
                organizationsList.add(organizations);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return organizationsList;
    }

    @Override
    public Organizations getOrganizationFromDB(String key) {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        Organizations organizations = new Organizations();

        Cursor cursor = db.rawQuery(SQL_QUERY + " WHERE tab1.id = " + "'" + key + "'" , null);

        cursor.moveToFirst();

                organizations.setTitle(cursor.getString(0));
                organizations.setPhone(cursor.getString(1));
                organizations.setId(cursor.getString(2));
                organizations.setAddress(cursor.getString(3));
                organizations.setLink(cursor.getString(4));
                organizations.setRegionId(cursor.getString(5));
                organizations.setCityId(cursor.getString(6));
                organizations.setOrgType(cursor.getString(7));

                Currencies currencies = new Currencies();
                currencies.setCurrencyList(getCurrencyListFromDB(key));
                organizations.setCurrencies(currencies);

        cursor.close();
        db.close();
        return organizations;
    }

    @Override
    public List<Currenci> getCurrencyListFromDB(String key) {
        List<Currenci> currencyList = new ArrayList<>();
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DataBaseHelper.TABLE_COURSES, new String[]{DataBaseHelper.COLUMN_ID_CURRENCY, DataBaseHelper.COLUMN_NAME_CURRENCY,
                        DataBaseHelper.COLUMN_ASK_CURRENCY, DataBaseHelper.COLUMN_BID_CURRENCY, DataBaseHelper.COLUMN_ASK_FLUC, DataBaseHelper.COLUMN_BID_FLUC}, DataBaseHelper.COLUMN_ID_ORGANIZATIONS + "=?",
                new String[]{String.valueOf(key)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Currenci currenci = new Currenci();

                currenci.setIdCurrency(cursor.getString(0));
                currenci.setNameCurrency(cursor.getString(1));
                currenci.setAsk(cursor.getString(2));
                currenci.setBid(cursor.getString(3));
                currenci.setAskFluc(cursor.getInt(4));
                currenci.setBidFluc(cursor.getInt(5));

                currencyList.add(currenci);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return currencyList;
    }

    @Override
    public String getDate() {
        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();
        String date = "";
        Cursor cursor = db.query(DataBaseHelper.TABLE_DATE, new String[]{DataBaseHelper.COLUMN_DATE,}, null
                , null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
            date = cursor.getString(0);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("Date: ", "" + date);
        return date;
    }


}
