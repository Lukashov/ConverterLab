package com.example.den.converterlab.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.den.converterlab.activities.DetailActivity;
import com.example.den.converterlab.activities.MapActivity;
import com.example.den.converterlab.R;
import com.example.den.converterlab.models.Organizations;
import com.example.den.converterlab.utils.MenuUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Den on 18.09.15.
 */
public class OrganizationsAdapter extends RecyclerView.Adapter<OrganizationsAdapter.MyViewHolder> implements Filterable {
        private final Context mContext;
        private List<Organizations> mOrganizationsList;
        private MyViewHolder myViewHolder;
        private Organizations mOrganizations;
        private List<Organizations> orig;
        private MenuUtils mMenuUtils = new MenuUtils();


    public OrganizationsAdapter(Context context, List<Organizations> contactList) {
            this.mContext = context;
            this.mOrganizationsList = contactList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_list, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder viewHolder, int i) {

            mOrganizations = mOrganizationsList.get(i);

            viewHolder.mTxtTitle.setText(mOrganizations.getTitle());
            viewHolder.mTxtRegion.setText(mOrganizations.getRegionId());
            viewHolder.mTxtCity.setText(mOrganizations.getCityId());
            viewHolder.mTxtPhone.setText("Тел.: " + mOrganizations.getPhone());
            viewHolder.mTxtAddress.setText("Адрес : " + mOrganizations.getAddress());

        }

        @Override
        public int getItemCount() {
            return mOrganizationsList == null ? 0 : mOrganizationsList.size();    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Organizations> results = new ArrayList<Organizations>();
                if (orig == null){
                    orig = mOrganizationsList;}
                if (constraint != null){
                    if(orig !=null & orig.size()>0 ){
                        for ( Organizations g :orig) {
                            if (g.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())
                                    || g.getCityId().toLowerCase().contains(constraint.toString().toLowerCase())
                                    || g.getRegionId().toLowerCase().contains(constraint.toString().toLowerCase())){
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mOrganizationsList = (ArrayList<Organizations>)results.values;
                notifyDataSetChanged();

            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements TabLayout.OnTabSelectedListener {

            private CardView mCard;

            private TextView mTxtTitle;
            private TextView mTxtRegion;
            private TextView mTxtCity;
            private TextView mTxtPhone;
            private TextView mTxtAddress;

            private TabLayout mTabLayout;

            public MyViewHolder(View itemView) {
                super(itemView);

                mCard = (CardView) itemView.findViewById(R.id.mCard);

                mTxtTitle = (TextView) itemView.findViewById(R.id.txtOrgTitle_IL);
                mTxtRegion = (TextView) itemView.findViewById(R.id.txtRegion_IL);
                mTxtCity = (TextView) itemView.findViewById(R.id.txtCity_IL);
                mTxtPhone = (TextView) itemView.findViewById(R.id.txtPhone_IL);
                mTxtAddress = (TextView) itemView.findViewById(R.id.txtAddress_IL);
                mTabLayout = (TabLayout) itemView.findViewById(R.id.tabsLayout_IL);

                mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_link));
                mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_map));
                mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_phone));
                mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.ic_next));

                mTabLayout.setOnTabSelectedListener(this);
            }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            checkTabSelected(tab,mOrganizationsList.get(getPosition()));
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            checkTabSelected(tab,mOrganizationsList.get(getPosition()));
        }

        private void checkTabSelected(TabLayout.Tab tab, Organizations organizations) {
            switch (tab.getPosition()) {
                case 0:
                    mMenuUtils.moveToLink(mContext, organizations);
                    break;
                case 1:
                    mMenuUtils.moveToMap(mContext, organizations);
                    break;
                case 2:
                    mMenuUtils.callToNumber(mContext, organizations);
                    break;
                case 3:
                    mMenuUtils.moveToNext(mContext, organizations);
                    break;
            }
        }
    }
}

