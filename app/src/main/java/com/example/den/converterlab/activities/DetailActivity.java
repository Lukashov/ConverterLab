package com.example.den.converterlab.activities;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.den.converterlab.R;
import com.example.den.converterlab.customViews.ShareDialog;
import com.example.den.converterlab.db.DataBaseHelper;
import com.example.den.converterlab.db.UseDataBaseController;
import com.example.den.converterlab.models.Currenci;
import com.example.den.converterlab.customViews.CourseModel;
import com.example.den.converterlab.models.Organizations;
import com.example.den.converterlab.services.LoadJsonService;
import com.example.den.converterlab.utils.MenuUtils;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private CardView mCardHeader;
    private Toolbar mToolbar;

    private TextView mTxtTitle;
    private TextView mTxtLink;
    private TextView mTxtRegionId;
    private TextView mTxtCityId;
    private TextView mTxtPhone;
    private TextView mTxtAddress;

    private UseDataBaseController mUseDataBaseController;
    private FrameLayout mFrameLayout;

    private Organizations organizations;
    private Intent mIntent;

    private DialogFragment mDialog;

    private List<Currenci> listCurrenci;

    private MenuUtils mMenuUtils;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mExpanded;

    private LinearLayout mLinearLayout;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        findViews();

        mUseDataBaseController = new UseDataBaseController(new DataBaseHelper(this));

        mIntent = getIntent();
        organizations = new Organizations();
        organizations = mUseDataBaseController.getOrganizationFromDB(mIntent.getStringExtra("idOrg"));

        setViews();

        createListCourses();

        poolDownToRefresh();

        if (savedInstanceState != null)
            mExpanded = savedInstanceState.getBoolean("mExpanded");

        setFabMenu();

        setToolbar();

    }

    private void setViews() {
        mTxtTitle.setText(organizations.getTitle());
        mTxtLink.setText(organizations.getLink());
        mTxtRegionId.setText(organizations.getRegionId());
        mTxtCityId.setText(organizations.getCityId());
        mTxtAddress.setText(organizations.getAddress());
        mTxtPhone.setText(organizations.getPhone());
    }

    private void findViews() {
        mCardHeader = (CardView) findViewById(R.id.cardHeader);
        mTxtTitle = (TextView) findViewById(R.id.txtTitle_AD);
        mTxtLink = (TextView) findViewById(R.id.txtLink_AD);
        mTxtRegionId = (TextView) findViewById(R.id.txtRegionId_AD);
        mTxtCityId = (TextView) findViewById(R.id.txtCityId_AD);
        mTxtAddress = (TextView) findViewById(R.id.txtAddress_AD);
        mTxtPhone = (TextView) findViewById(R.id.txtPhone_AD);
    }

    private void createListCourses() {
        mLinearLayout = (LinearLayout) findViewById(R.id.lLayout);

        listCurrenci = new ArrayList<>();
        listCurrenci = organizations.getCurrencies().getCurrencyList();

        for (int i = 0; i < listCurrenci.size(); i++) {
            CourseModel courseModel = new CourseModel(this);
            courseModel.setConteiner(listCurrenci.get(i));
            mLinearLayout.addView(courseModel);
        }
    }

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow);
        getSupportActionBar().setTitle(organizations.getTitle());
        mToolbar.setSubtitle(organizations.getCityId());
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setFabMenu() {
        mFrameLayout = (FrameLayout) findViewById(R.id.flVisibility);

        FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);

        FloatingActionButton actionMap = (FloatingActionButton) findViewById(R.id.action_a);
        FloatingActionButton actionLink = (FloatingActionButton) findViewById(R.id.action_b);
        FloatingActionButton actionPhone = (FloatingActionButton) findViewById(R.id.action_c);

        if (mExpanded)
            mFrameLayout.setVisibility(View.VISIBLE);

        menuMultipleActions.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mExpanded = true;
                mFrameLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                mExpanded = false;
                mFrameLayout.setVisibility(View.INVISIBLE);
            }
        });

        actionMap.setOnClickListener(this);
        actionLink.setOnClickListener(this);
        actionPhone.setOnClickListener(this);

    }

    private void poolDownToRefresh() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light);
    }

    private void startServiceLoadJson() {
        mIntent = new Intent(DetailActivity.this, LoadJsonService.class);
        startService(mIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        mDialog = new ShareDialog();
        Bundle bundle = new Bundle();
        if (id == R.id.action_share){
            bundle.putString("idOrg", mIntent.getStringExtra("idOrg"));
            mDialog.setArguments(bundle);
            mDialog.show(getFragmentManager(),"dialog");
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        mMenuUtils = new MenuUtils();
        switch (v.getId()){
            case R.id.action_a:
                mMenuUtils.moveToMap(this,organizations);
                break;
            case R.id.action_b:
                mMenuUtils.moveToLink(this,organizations);
                break;
            case R.id.action_c:
                mMenuUtils.callToNumber(this,organizations);
                break;
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("mExpanded", mExpanded);
    }
}