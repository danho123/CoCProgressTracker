package com.hodanny.cocprogresstracker.fragments.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.models.Building;

import java.util.List;

import butterknife.OnClick;

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_card, parent, false);
        EntityViewHolder bvh = new EntityViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(EntityViewHolder holder, final int position) {
        holder.mBuildingName.setText(buildings.get(position).getName());
        holder.mBuildingLevel.setText(buildings.get(position).getLevel() + "");
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
        public EntityViewHolder(View view)
        {
            super(view);
            mBuildingName = (TextView)view.findViewById(R.id.upgrade_card_name);
            mUpgrade = (Button)view.findViewById(R.id.button_upgrade);
            mUpgrade.setTag("upgrade");


            mDowngrade = (Button)view.findViewById(R.id.button_downgrade);
            mDowngrade.setTag("downgrade");

            mBuildingLevel = (TextView)view.findViewById(R.id.upgrades_card_level);

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

