package com.hodanny.cocprogresstracker.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.models.Building;

import java.util.List;

/**
 * Created by dan on 8/13/2015.
 */
public class UpgradeListViewAdapter  extends RecyclerView.Adapter<UpgradeListViewAdapter.ViewHolder>
{
    List<Building> buildings;
    Context mContext;

    public UpgradeListViewAdapter(List<Building> buildings, Context context)
    {
        this.buildings = buildings;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upgrades_card, parent, false);
        ViewHolder bvh = new ViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBuildingName.setText(buildings.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mBuildingName;
        public ViewHolder(View view)
        {
            super(view);
            mBuildingName = (TextView)view.findViewById(R.id.upgrade_card_name);
        }
    }
}

