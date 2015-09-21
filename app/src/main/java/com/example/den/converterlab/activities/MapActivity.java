package com.example.den.converterlab.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.den.converterlab.R;
import com.example.den.converterlab.utils.MapLoader;
import com.example.den.converterlab.utils.NetworkUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.ExecutionException;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {

    private String mTitle;
    private String mCity;
    private String mAddress;
    private GoogleMap mGoogleMap;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();

        mTitle = intent.getStringExtra("title");
        mCity = intent.getStringExtra("city");
        mAddress = intent.getStringExtra("address");

        NetworkUtils networkUtils = new NetworkUtils();

        if(networkUtils.haveNetworkConnection(this)){
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMap_AP);
            mapFragment.getMapAsync(this);
        }else{
            Toast.makeText(this, "not Internet connection", Toast.LENGTH_LONG).show();
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(mTitle);
        mToolbar.setSubtitle(mCity);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapLoader mapLoader = new MapLoader();
        mapLoader.execute(mCity, mAddress);
        mGoogleMap = googleMap;
        if (mGoogleMap == null) return;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        try {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapLoader.get(), mapLoader.getZoom()));
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(mapLoader.get())
                    .title(mTitle)
                    .snippet(mAddress));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
