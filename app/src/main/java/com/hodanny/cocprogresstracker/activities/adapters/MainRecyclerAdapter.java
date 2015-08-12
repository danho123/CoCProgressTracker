package com.hodanny.cocprogresstracker.activities.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.models.Building;

import java.util.List;

/**
 * Created by danho on 8/11/2015.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.BuildingViewHolder>
{
    List<Building> buildings;
    public MainRecyclerAdapter(List<Building> building)
    {
        this.buildings = building;
    }
    @Override
    public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.building_card, parent, false);
        BuildingViewHolder bvh = new BuildingViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BuildingViewHolder holder, int position) {
        Building building = buildings.get(position);
//        holder.mBuildingLevel.setText("Level " + building.getLevel());
        holder.mBuildingName.setText((building.getName()));
//        holder.mImage.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public static class BuildingViewHolder extends RecyclerView.ViewHolder
    {
        CardView mCardView;
        TextView mBuildingName;
        TextView mBuildingLevel;
        ImageView mImage;

        public BuildingViewHolder(View view)
        {
            super(view);
            mCardView = (CardView)view.findViewById(R.id.building_cardview);
            //mBuildingLevel = (TextView)view.findViewById(R.id.building_level);
            mBuildingName = (TextView)view.findViewById(R.id.building_name);
        }
    }
}
