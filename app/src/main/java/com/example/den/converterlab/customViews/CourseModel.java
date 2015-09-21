package com.example.den.converterlab.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.den.converterlab.R;
import com.example.den.converterlab.models.Currenci;

/**
 * Created by Den on 18.09.15.
 */
public class CourseModel extends RelativeLayout {

    private TextView mTxtNameCurrency;
    private TextView mTxtAsk;
    private TextView mTxtBid;
    private ImageView mArrAsk;
    private ImageView mArrBid;

    public CourseModel(Context context) {
        super(context);
        initComponent();
    }

    public CourseModel(Context context, AttributeSet attrs) {
        super(context, attrs);
        initComponent();
    }

    public CourseModel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initComponent();
    }

    private void initComponent() {
        inflate(getContext(), R.layout.course_model, this);

        mTxtNameCurrency = (TextView) findViewById(R.id.txtNameCurrency_CM);
        mTxtAsk = (TextView) findViewById(R.id.txtAsk_CM);
        mTxtBid = (TextView) findViewById(R.id.txtBid_CV);
        mArrAsk = (ImageView) findViewById(R.id.imgAsk_CM);
        mArrBid = (ImageView) findViewById(R.id.imgBid_CM);    }

    public void setConteiner(Currenci currenci){
        mTxtNameCurrency.setText(currenci.getNameCurrency());
        mTxtAsk.setText(currenci.getAsk());
        mTxtBid.setText(currenci.getBid());

        if(currenci.getAskFluc()==1){
            mArrAsk.setImageResource(R.drawable.ic_green_arrow_up);
            mTxtAsk.setTextColor(getResources().getColor(R.color.courseColorGreen));
        }else{
            mArrAsk.setImageResource(R.drawable.ic_red_arrow_down);
            mTxtAsk.setTextColor(getResources().getColor(R.color.currencyColor));
        }

        if(currenci.getBidFluc()==1){
            mArrBid.setImageResource(R.drawable.ic_green_arrow_up);
            mTxtBid.setTextColor(getResources().getColor(R.color.courseColorGreen));
        }else{
            mArrBid.setImageResource(R.drawable.ic_red_arrow_down);
            mTxtBid.setTextColor(getResources().getColor(R.color.currencyColor));
        }

    }
}
