package com.example.den.converterlab.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.den.converterlab.db.DataBaseHelper;
import com.example.den.converterlab.models.Currenci;
import com.example.den.converterlab.models.Currencies;
import com.example.den.converterlab.models.Organizations;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Den on 16.09.15.
 */
public class CompareUtils {

    public boolean compareDate(String dateFromBD, String dateFromJson){
        if(dateFromBD.equals(dateFromJson)){
            return true;
        }
        return false;
    }

}
