package com.example.den.converterlab.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.den.converterlab.db.DataBaseHelper;
import com.example.den.converterlab.db.UseDataBaseController;
import com.example.den.converterlab.models.DataRoot;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Den on 16.09.15.
 */
public class CheckAllForStart {
    private NetworkUtils mNetworkUtils;
    private LoadDataToModelsFromJson mLoadDataToModelsFromJson;
    private DataBaseHelper mDataBaseHelper;
    private UseDataBaseController mUseDataBaseController;
    private Context mContext;

    public static final String ACTION_COMPLETE = "com.example.den.converterlab.action.COMPLETE";

    public CheckAllForStart(Context context) {
        this.mContext = context;
    }

    public void checkAll(){

        DataRoot dataRoot = new DataRoot();
        mNetworkUtils = new NetworkUtils();
        mLoadDataToModelsFromJson = new LoadDataToModelsFromJson();
        mDataBaseHelper = new DataBaseHelper(mContext);
        mUseDataBaseController = new UseDataBaseController(mDataBaseHelper);

        if(mNetworkUtils.haveNetworkConnection(mContext)){
            Log.d("CHECK:", "network true, load json");
            try {
                dataRoot = mLoadDataToModelsFromJson.getFillDataRoot(mContext);
                Log.d("CHECK:", "json download");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(mDataBaseHelper.checkDataBase(mContext)){
                Log.d("CHECK:", "database exist true");
                if(!mDataBaseHelper.compareDate(mUseDataBaseController.getDate(),dataRoot.getDate())){
                    Log.d("CHECK:", "date change true");
                    jsonToDataBase(dataRoot);
                    Log.d("CHECK:", "json download to DB complete, load UI from new DB");
                }else{
                    Log.d("CHECK:", "date change false, load UI from old DB");
                }
            }else{
                Log.d("CHECK:", "database not exist");
                jsonToDataBase(dataRoot);
                Log.d("CHECK:", "json to DB complete, load UI from new DB");
            }
        }else{
            Log.d("CHECK:", "Don't have connection to internet");
            if(mDataBaseHelper.checkDataBase(mContext)){
                Log.d("CHECK:", "database exist true, load UI from old DB");
            }else{
                Log.d("CHECK:", "database exist false");
            }
        }
        showUI();
    }

    private void jsonToDataBase(DataRoot dataRoot) {
        mUseDataBaseController.addOrganizations(dataRoot.getOrganizationsList());
        mUseDataBaseController.addOrgTypes(dataRoot.getMap().getMapOrgTypes());
        mUseDataBaseController.addRegions(dataRoot.getMap().getMapRegions());
        mUseDataBaseController.addCities(dataRoot.getMap().getMapCities());
        mUseDataBaseController.addCurrencies(dataRoot.getMap().getMapCurrencies());
        mUseDataBaseController.addCourse(dataRoot.getOrganizationsList());
        mUseDataBaseController.addDate(dataRoot.getDate());
    }

    private void showUI(){
        Intent intent = new Intent(ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        Log.d("CHECK:", "START showUI");
    }

}
