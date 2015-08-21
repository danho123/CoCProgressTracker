package com.hodanny.cocprogresstracker.fragments.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.activities.HomeActivity;
import com.hodanny.cocprogresstracker.models.Building;
import com.hodanny.cocprogresstracker.utils.TimeConverter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dan on 8/13/2015.
 */
public class ViewPagerRecyclerAdapter extends RecyclerView.Adapter<ViewPagerRecyclerAdapter.EntityViewHolder>
{
    List<Building> buildings;
    ClickListener clickListener;

    public ViewPagerRecyclerAdapter(List<Building> buildings)
    {
        this.buildings = buildings;
    }

    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_card_level, parent, false);
        EntityViewHolder bvh = new EntityViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(EntityViewHolder holder, final int position) {

        Building building = buildings.get(position);
        holder.mBuildingName.setText(building.getName());

        if(building.getLevel() != 0)
        {
            holder.mBuildingLevel.setText("Level " + building.getLevel());
            holder.mBuildingLevel.setTextColor(Color.BLACK);
        }
        else
        {
            holder.mBuildingLevel.setText("NOT BUILT");
            holder.mBuildingLevel.setTextColor(Color.RED);
        }

        if(building.getUpgradeCost() != 0) {
            holder.mNextUpgrade.setText(String.format("%d, %s", building.getUpgradeCost(), TimeConverter.formatSeconds(building.getUpgradeTime())));
        }
        else
        {
            holder.mNextUpgrade.setText("MAXED");
        }

        float progress =  ((float)building.getLevel()/HomeActivity.mMaxMap.get(building.getName()))*100;
        holder.mProgressBar.setProgress(Math.round(progress));

    }

    public void setClickListener(ClickListener clickListener)
    {
        this.clickListener = clickListener;
    }


    @Override
    public int getItemCount() {
        return buildings.size();
    }

    public class EntityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView mBuildingName;
        TextView mBuildingLevel;
        Button mUpgrade;
        Button mDowngrade;
        TextView mNextUpgrade;

        @Bind(R.id.entity_progressbar) ProgressBar mProgressBar;

        public EntityViewHolder(View view)
        {
            super(view);

            ButterKnife.bind(this, view);

            mBuildingName = (TextView) view.findViewById(R.id.upgrade_card_name);
            mBuildingLevel = (TextView) view.findViewById(R.id.upgrade_card_level);
            mUpgrade = (Button) view.findViewById(R.id.button_upgrade);
            mDowngrade = (Button) view.findViewById(R.id.button_downgrade);
            mNextUpgrade = (TextView) view.findViewById(R.id.upgrade_card_next);

            mUpgrade.setTag("upgrade");
            mDowngrade.setTag("downgrade");
            mUpgrade.setOnClickListener(this);
            mDowngrade.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener !=null)
            {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View v, int pos);
    }
}

