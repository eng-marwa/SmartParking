package com.example.marwatalaat.smartparking.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by MarwaTalaat on 11/29/2016.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder>{
    public MyRecyclerViewAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


}
class MyViewHolder extends RecyclerView.ViewHolder{

    public MyViewHolder(View itemView) {
        super(itemView);
    }
}