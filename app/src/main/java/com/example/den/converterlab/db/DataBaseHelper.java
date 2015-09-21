package com.example.den.converterlab.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.File;

/**
 * Created by Den on 15.09.15.
 */
public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "mydatabase.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_ORGANIZATIONS = "organizations";
    public static final String TABLE_CURRENCIES = "currencies";
    public static final String TABLE_COURSES = "courses";
    public static final String TABLE_REGIONS = "regions";
    public static final String TABLE_CITIES = "cities";
    public static final String TABLE_ORG_TYPES = "orgTypes";
    public static final String TABLE_DATE = "date";

    public static final String KEY_ID = "_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_OLD_ID = "oldId";
    public static final String COLUMN_ORG_TYPE = "orgType";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_REGION_ID = "regionId";
    public static final String COLUMN_CITY_ID = "cityId";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LINK = "link";
    public static final String COLUMN_VALUE = "newValue";
    public static final String COLUMN_ID_CURRENCY = "idCurrency";
    public static final String COLUMN_NAME_CURRENCY = "nameCurrency";
    public static final String COLUMN_ASK_CURRENCY = "ask";
    public static final String COLUMN_BID_CURRENCY = "bid";
    public static final String COLUMN_ID_ORGANIZATIONS = "idOrganization";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ASK_FLUC = "askFluc";
    public static final String COLUMN_BID_FLUC = "bidFluc";

    private static final String CREATE_ORGANIZATIONS_TABLE = "create table "
            + TABLE_ORGANIZATIONS + " (" + KEY_ID + " integer primary key autoincrement, "
            + COLUMN_ID + " text, "
            + COLUMN_OLD_ID + " text, "
            + COLUMN_ORG_TYPE + " text, "
            + COLUMN_TITLE + " text, "
            + COLUMN_REGION_ID + " text, "
            + COLUMN_CITY_ID + " text, "
            + COLUMN_PHONE + " text, "
            + COLUMN_ADDRESS + " text, "
            + COLUMN_LINK + " text, "
            + COLUMN_DATE + " text, "
            + "FOREIGN KEY (" + COLUMN_ORG_TYPE + ")REFERENCES "+ TABLE_ORG_TYPES + "("+ KEY_ID +")"
            + "FOREIGN KEY (" + COLUMN_REGION_ID + ")REFERENCES "+ TABLE_REGIONS + "("+ KEY_ID +")"
            + "FOREIGN KEY (" + COLUMN_ID + ")REFERENCES "+ TABLE_COURSES + "("+ COLUMN_ID_ORGANIZATIONS +")"
            + "FOREIGN KEY (" + COLUMN_CITY_ID + ")REFERENCES "+ TABLE_CITIES + "("+ KEY_ID +")" + ");";

    private static final String CREATE_ORG_TYPES_TABLE = "create table "
            + TABLE_ORG_TYPES + " (" + KEY_ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE, "
            + COLUMN_VALUE + " text);";

    private static final String CREATE_CURRENCIES_TABLE = "create table "
            + TABLE_CURRENCIES + " (" + KEY_ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE, "
            + COLUMN_VALUE + " text);";

    private static final String CREATE_COURSE_TABLE = "create table "
            + TABLE_COURSES + " (" + KEY_ID + " integer primary key autoincrement, "
            + COLUMN_ID_ORGANIZATIONS + " text, "
            + COLUMN_ID_CURRENCY + " text, "
            + COLUMN_NAME_CURRENCY + " text, "
            + COLUMN_ASK_CURRENCY + " text, "
            + COLUMN_ASK_FLUC + " text, "
            + COLUMN_BID_CURRENCY + " text, "
            + COLUMN_BID_FLUC + " text, "
            + "FOREIGN KEY (" + COLUMN_ID_CURRENCY + ")REFERENCES "+ TABLE_CURRENCIES + "("+ KEY_ID +")" + ");";

    private static final String CREATE_REGIONS_TABLE = "create table "
            + TABLE_REGIONS + " (" + KEY_ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE, "
            + COLUMN_VALUE + " text);";

    private static final String CREATE_CITIES_TABLE = "create table "
            + TABLE_CITIES + " (" + KEY_ID + " TEXT PRIMARY KEY ON CONFLICT REPLACE, "
            + COLUMN_VALUE + " text);";

    private static final String CREATE_DATE_TABLE = "create table "
            + TABLE_DATE + " (" + KEY_ID + " integer primary key autoincrement, "
            + COLUMN_DATE + " text);";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ORGANIZATIONS_TABLE);
        db.execSQL(CREATE_ORG_TYPES_TABLE);
        db.execSQL(CREATE_CURRENCIES_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_REGIONS_TABLE);
        db.execSQL(CREATE_CITIES_TABLE);
        db.execSQL(CREATE_DATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_ORGANIZATIONS);
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_ORG_TYPES);
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_CURRENCIES);
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_REGIONS);
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_CITIES);
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF IT EXISTS " + TABLE_DATE);

        onCreate(db);
    }

    public boolean checkDataBase(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    public boolean compareDate(String dateFromBD, String dateFromJson){
        if(dateFromBD.equals(dateFromJson)){
            return true;
        }
        return false;
    }
}
