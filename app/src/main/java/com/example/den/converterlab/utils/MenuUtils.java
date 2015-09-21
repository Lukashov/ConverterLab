package com.example.den.converterlab.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.den.converterlab.activities.DetailActivity;
import com.example.den.converterlab.activities.MapActivity;
import com.example.den.converterlab.models.Organizations;

/**
 * Created by Den on 21.09.15.
 */
public class MenuUtils {

    public void callToNumber(Context context, Organizations organizations) {
        Intent dialIntent = new Intent(Intent.ACTION_CALL);
        dialIntent.setData(Uri.parse("tel:" + organizations.getPhone()));
        dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(dialIntent);
    }

    public void moveToLink(Context context, Organizations organizations) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(organizations.getLink()));
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(browserIntent);
    }


    public void moveToNext(Context context, Organizations organizations) {
        Intent activityIntent = new Intent(context, DetailActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activityIntent.putExtra("idOrg", organizations.getId());
        context.startActivity(activityIntent);
    }

    public void moveToMap(Context context, Organizations organizations) {
        Intent mapIntent = new Intent(context, MapActivity.class);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mapIntent.putExtra("title",organizations.getTitle());
        mapIntent.putExtra("city", organizations.getCityId());
        mapIntent.putExtra("address", organizations.getAddress());
        context.startActivity(mapIntent);
    }
}
