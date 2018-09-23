package com.morandror.scclient.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.morandror.scclient.R;
import com.morandror.scclient.activities.ClosetInfoActivity;
import com.morandror.scclient.models.Closet;

import java.util.ArrayList;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.MyViewHolder> {

    private ArrayList<Closet> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Closet mItem;

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.textViewVersion = itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = itemView.findViewById(R.id.imageView);
        }

        public void setItem(Closet item) {
            mItem = item;
        }

        @Override
        public void onClick(View view) {
            Intent closetInfo = new Intent(view.getContext(), ClosetInfoActivity.class);
            closetInfo.putExtra(view.getContext().getString(R.string.closet), mItem);
            view.getContext().startActivity(closetInfo);
        }

    }

    public ClosetAdapter(ArrayList<Closet> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        System.out.println("List position: " + listPosition);

        holder.setItem(dataSet.get(listPosition));

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());
        textViewVersion.setText(dataSet.get(listPosition).getDescription());
        imageView.setImageResource(R.drawable.sclogowhite);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
