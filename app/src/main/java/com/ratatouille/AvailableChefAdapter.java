package com.ratatouille;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratatouille.models.Chef;
import java.util.List;

public class AvailableChefAdapter extends BaseAdapter {

    private Context mContext;
    private List<Chef> mChef;

    public AvailableChefAdapter(Context mContext, List<Chef> mChef) {
        this.mContext = mContext;
        this.mChef = mChef;
    }

    @Override
    public int getCount() {
        return mChef.size();
    }

    @Override
    public Object getItem(int position) {
        return mChef.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(mContext, R.layout.item_available_chefs,null);

        TextView textViewChef = (TextView) v.findViewById(R.id.textViewAvailableChef);
        textViewChef.setText(mChef.get(position).getNombre());

        TextView textViewMoreInfo = (TextView) v.findViewById(R.id.textViewMoreInfo);
        textViewMoreInfo.setText(mChef.get(position).getCalificacion()+"");

        return v;
    }

}
