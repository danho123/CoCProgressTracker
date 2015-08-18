package com.hodanny.cocprogresstracker.fragments.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.models.Building;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by dan on 8/13/2015.
 */
public class ViewPagerRecyclerAdapter extends RecyclerView.Adapter<ViewPagerRecyclerAdapter.ViewHolder>
{
    List<Building> buildings;

    public ViewPagerRecyclerAdapter(List<Building> buildings)
    {
        this.buildings = buildings;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upgrades_card, parent, false);
        ViewHolder bvh = new ViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mBuildingName.setText(buildings.get(position).getName());

        holder.mDowngrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildings.get(position).downgrade();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView mBuildingName;
        TextView mBuildingLevel;
        Button mUpgrade;
        Button mDowngrade;
        public ViewHolder(View view)
        {
            super(view);
            mBuildingName = (TextView)view.findViewById(R.id.upgrade_card_name);
            mUpgrade = (Button)view.findViewById(R.id.button_upgrade);
            mDowngrade = (Button)view.findViewById(R.id.button_downgrade);
            mBuildingLevel = (TextView)view.findViewById(R.id.upgrades_card_level);
        }
    }
}

