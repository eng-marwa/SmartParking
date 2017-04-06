package com.example.marwatalaat.smartparking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marwatalaat.smartparking.adapters.MyRecyclerViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
@BindView(R.id.my_recycler_view)
RecyclerView mRecyclerView;
    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

}
