package com.example.localisationdab;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class ListDabAdapter extends BaseAdapter implements Filterable {


    Context mContext;
    List<Dab> mList;
    List<Dab> filteredData;

    public ListDabAdapter( Context mContext, List<Dab> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }



    @Override
    public Filter getFilter() {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list

                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = mList;
                    results.count = mList.size();
                }
                else
                {
                    List<Dab> filterResultsData=mList;

                    for(Dab data : mList)
                    {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if(data.getNomInstitut().equals(charSequence))
                        {
                            filterResultsData.add(data);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredData = (List<Dab>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=View .inflate(mContext,R.layout.item_list_view,null);

        TextView tvName =v.findViewById(R.id.tv_name);
        TextView tvAdress =v.findViewById(R.id.tv_adresse);
        TextView tvEtat =v.findViewById(R.id.tv_etat);
        tvName.setText(mList.get(position).getNomInstitut());
        tvAdress.setText(String.valueOf(mList.get(position).getAdresse()));
        tvEtat.setText(mList.get(position).getEtat());
        v.setTag( mList.get(position).getId());
        getFilter();


        return v;
    }
}
