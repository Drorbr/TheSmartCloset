package com.morandror.scclient.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.morandror.scclient.R;
import com.morandror.scclient.activities.ClosetInfoActivity;
import com.morandror.scclient.activities.MainActivity;
import com.morandror.scclient.activities.home_page_activity2;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import java.util.ArrayList;

import static com.morandror.scclient.utils.SharedStrings.ASSIGN_CLOSET_TO_USER_URL;
import static com.morandror.scclient.utils.SharedStrings.DELETE_CLOSET_URL;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.MyViewHolder> {

    private ArrayList<Closet> dataSet;
    private deleteClosetListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        Closet mItem;

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        ImageButton trashBtn;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.textViewVersion = itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = itemView.findViewById(R.id.imageView);
            this.trashBtn = itemView.findViewById(R.id.imgButton_del_closet);
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
        final Closet currentCloset = dataSet.get(listPosition);
        holder.setItem(currentCloset);

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(currentCloset.getName());
        textViewVersion.setText(currentCloset.getDescription());
        imageView.setImageResource(R.drawable.sclogowhite);

        ImageButton trashBtn = holder.trashBtn;

        trashBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("delete closet with id " + String.valueOf(currentCloset.getId()));
                final View finalView = view;
                StringRequest request = new StringRequest(String.format(DELETE_CLOSET_URL, currentCloset.getId()),
                        new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response){
                                System.out.println("Delete closet succeeded");
                                Toast.makeText(finalView.getContext(), "Closet deleted successfully", Toast.LENGTH_LONG).show();
                                notifyItemRemoved(listPosition);
                                dataSet.remove(listPosition);
                                listener.onClosetDeleted(currentCloset);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String msg = "Failed to delete closet";
                        System.out.println(msg);
                        Toast.makeText(finalView.getContext(), msg, Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueueSingleton.getInstance(view.getContext()).getRequestQueue().add(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDeleteClosetListener(deleteClosetListener listener){
        this.listener = listener;
    }
}
