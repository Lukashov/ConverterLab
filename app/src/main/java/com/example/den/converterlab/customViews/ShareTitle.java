package com.example.den.converterlab.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.den.converterlab.R;
import com.example.den.converterlab.models.Organizations;

/**
 * Created by Den on 21.09.15.
 */
public class ShareTitle extends RelativeLayout {

    private TextView mTxtTitleName;
    private TextView mTxtRegion;
    private TextView mTxtCity;

    public ShareTitle(Context context) {
        super(context);
        initComponent();
    }

    public ShareTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public ShareTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShareTitle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.share_title, this);
        findViews();
    }

    private void findViews() {

        mTxtTitleName = (TextView) findViewById(R.id.txtTitle_ST);
        mTxtRegion = (TextView) findViewById(R.id.txtRegion_ST);
        mTxtCity = (TextView) findViewById(R.id.txtCity_ST);

    }

    public void setConteiner(Organizations organizations){
        mTxtTitleName.setText(organizations.getTitle());
        mTxtRegion.setText(organizations.getRegionId());
        mTxtCity.setText(organizations.getCityId());
    }

}
