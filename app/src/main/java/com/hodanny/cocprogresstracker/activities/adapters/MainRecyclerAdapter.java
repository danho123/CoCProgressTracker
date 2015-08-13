package com.hodanny.cocprogresstracker.activities.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.models.Building;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by danho on 8/11/2015.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.BuildingViewHolder>
{
    TreeMap<String, TreeMap<Building,Integer>> buildingsMap;

    Context mContext;

    public MainRecyclerAdapter(TreeMap<String,TreeMap<Building,Integer>> building, Context context)
    {
        this.buildingsMap = building;
        mContext = context;
    }

    @Override
    public BuildingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.building_card, parent, false);
        BuildingViewHolder bvh = new BuildingViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(BuildingViewHolder holder, int position) {
        String key = buildingsMap.keySet().toArray()[position].toString();

        holder.mBuildingName.setText(key + " " + buildingsMap.get(key).size());
        holder.mLinearLayout.removeAllViews();

        for (Map.Entry<Building, Integer> entry : buildingsMap.get(key).entrySet()) {
            View line = LayoutInflater.from(mContext).inflate(R.layout.building_item, null);
            TextView textView = (TextView)line.findViewById(R.id.building_item_name);
            textView.setText("Level " + entry.getKey().getLevel() + " - " + "x" + entry.getValue());
            holder.mLinearLayout.addView(line);
        }

        holder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return buildingsMap.size();
    }

    public static class BuildingViewHolder extends RecyclerView.ViewHolder
    {
        CardView mCardView;
        TextView mBuildingName;
        TextView mEdit;
        LinearLayout mLinearLayout;
        LayoutInflater mLayoutInflater;
        public BuildingViewHolder(View view)
        {
            super(view);
            mCardView = (CardView)view.findViewById(R.id.building_cardview);
            mEdit = (TextView)view.findViewById(R.id.card_edit);
            mBuildingName = (TextView)view.findViewById(R.id.building_name);
            mLinearLayout = (LinearLayout)view.findViewById(R.id.building_linearlayout);
        }
    }
}
