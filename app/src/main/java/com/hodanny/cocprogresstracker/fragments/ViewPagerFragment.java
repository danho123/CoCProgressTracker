package com.hodanny.cocprogresstracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.fragments.adapters.ViewPagerRecyclerAdapter;
import com.hodanny.cocprogresstracker.models.Building;
import com.hodanny.cocprogresstracker.utils.Log;

import java.util.ArrayList;

public class ViewPagerFragment extends Fragment implements ViewPagerRecyclerAdapter.ClickListener {
    private static final String ARG_POSITION = "position";

    private int position;

    private RecyclerView mRecyclerView;
    private ViewPagerRecyclerAdapter mAdapter;

    public ArrayList<Building> buildings;

    public static ViewPagerFragment newInstance(ArrayList<Building> pBuildings) {
        ViewPagerFragment f = new ViewPagerFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList("buildings", pBuildings);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildings = getArguments().getParcelableArrayList("buildings");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view_pager, container, false);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.fragment_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ViewPagerRecyclerAdapter(buildings);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);


        return v;
    }

    @Override
    public void itemClicked(View v, int pos) {
        Building building = buildings.get(pos);
        if(v.getTag().equals("upgrade"))
        {
            building.upgrade();
        }
        if(v.getTag().equals("downgrade"))
        {
            building.downgrade();
        }
        mAdapter.notifyDataSetChanged();
    }
}