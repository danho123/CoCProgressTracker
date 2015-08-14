package com.hodanny.cocprogresstracker.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hodanny.cocprogresstracker.R;
import com.hodanny.cocprogresstracker.activities.adapters.MainRecyclerAdapter;
import com.hodanny.cocprogresstracker.fragments.adapters.UpgradeListViewAdapter;
import com.hodanny.cocprogresstracker.models.Building;

import java.util.ArrayList;

public class BuildingEditFragment extends DialogFragment
{
    Button mButton;
    RecyclerView mRecyclerView;
    private UpgradeListViewAdapter mAdapter;

    ArrayList<Building> buildings;
//    onSubmitListener mListener;
    String text = "";
//    interface onSubmitListener {
//        void setOnSubmitListener(String arg);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        buildings = bundle.getParcelableArrayList("buildings");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.fragment_building_edit);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        mButton = (Button) dialog.findViewById(R.id.button_submit);
        mRecyclerView = (RecyclerView) dialog.findViewById(R.id.upgrade_fragment_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new UpgradeListViewAdapter(buildings, getActivity());

        return dialog;
    }
}
