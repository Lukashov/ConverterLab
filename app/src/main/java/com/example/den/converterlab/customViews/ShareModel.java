package com.example.den.converterlab.customViews;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.den.converterlab.R;
import com.example.den.converterlab.models.Currenci;

import java.math.BigDecimal;

/**
 * Created by Den on 21.09.15.
 */
public class ShareModel extends RelativeLayout {

    private TextView mTxtIdCurrency;
    private TextView mTxtAskBid;

    public ShareModel(Context context) {
        super(context);
        initComponent();
    }

    public ShareModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public ShareModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShareModel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initComponent();
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.share_model, this);
        findViews();
    }

    private void findViews() {

        mTxtIdCurrency = (TextView) findViewById(R.id.txtIDCurrency_SM);
        mTxtAskBid = (TextView) findViewById(R.id.txtAskBid_SM);
    }

    public void setConteiner(Currenci currenci){

        BigDecimal ask = new BigDecimal(currenci.getAsk());
        BigDecimal bid = new BigDecimal(currenci.getAsk());

        mTxtIdCurrency.setText(currenci.getIdCurrency()+"      ");
        mTxtIdCurrency.setTextColor(getResources().getColor(R.color.currencyColor));

        mTxtAskBid.setText("          "+ask.setScale(2, BigDecimal.ROUND_HALF_EVEN) + "/" +
                bid.setScale(2, BigDecimal.ROUND_HALF_EVEN)+"");
    }
}
