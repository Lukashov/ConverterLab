package com.example.den.converterlab.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;

import com.example.den.converterlab.R;
import com.example.den.converterlab.adapters.OrganizationsAdapter;
import com.example.den.converterlab.db.DataBaseHelper;
import com.example.den.converterlab.db.UseDataBaseController;
import com.example.den.converterlab.models.Organizations;
import com.example.den.converterlab.receivers.AlarmReceiver;
import com.example.den.converterlab.services.LoadJsonService;
import com.example.den.converterlab.utils.CheckAllForStart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private Intent mIntent;

    private BroadcastReceiver broadcastReceiver;

    private IntentFilter mIntentFilter;

    private UseDataBaseController mUseDataBaseController;
    private DataBaseHelper mDataBaseHelper;

    private List<Organizations> mListOrganizations;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OrganizationsAdapter mOrganizationsAdapter;

    private Toolbar mToolbar;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        poolDownToRefresh();

        mDataBaseHelper = new DataBaseHelper(this);
        mUseDataBaseController = new UseDataBaseController(mDataBaseHelper);

        mListOrganizations = new ArrayList<>();

        mRecyclerView = (RecyclerView)findViewById(R.id.rvList);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        startServiceLoadJson();

        startAlarmReceiver();

        startListenerCompleteLogicReceiver();

    }

    private void startServiceLoadJson() {
        mIntent = new Intent(MainActivity.this, LoadJsonService.class);
        startService(mIntent);
    }

    private void poolDownToRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchView search =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setOnQueryTextListener(this);

        return true;

    }

//    TODO: перенести метод в утилсы
    private void startAlarmReceiver(){
        Intent alarm = new Intent(this, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), 1800000, pendingIntent);
        }
    }

    private void startListenerCompleteLogicReceiver(){

        broadcastReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                switch (action) {
                    case CheckAllForStart.ACTION_COMPLETE:

                        mListOrganizations = mUseDataBaseController.getOrganizationsListFromDB();

                        mAdapter = new OrganizationsAdapter(getApplicationContext(),
                                mListOrganizations);

                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.notifyDataSetChanged();

                        break;
                }
            }
        };
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(CheckAllForStart.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, mIntentFilter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if ( TextUtils.isEmpty(newText) ) {
            mOrganizationsAdapter = new OrganizationsAdapter(getApplicationContext(),mListOrganizations);
            mRecyclerView.setAdapter(mOrganizationsAdapter);
        } else {
            mOrganizationsAdapter.getFilter().filter(newText.toString());
        }

        return true;    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                startServiceLoadJson();
            }
        }, 3000);

    }
}
