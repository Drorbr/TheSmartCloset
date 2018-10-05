package com.morandror.scclient.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.morandror.scclient.R;
import com.morandror.scclient.activities.ClosetInfoActivity;
import com.morandror.scclient.activities.StatsActivity;
import com.morandror.scclient.models.Closet;
import com.morandror.scclient.models.Statistics;
import com.morandror.scclient.utils.JsonHandler;
import com.morandror.scclient.utils.http.RequestQueueSingleton;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static com.morandror.scclient.utils.SharedStrings.DELETE_CLOSET_URL;
import static com.morandror.scclient.utils.SharedStrings.GET_CLOSET_STATS_URL;
import static com.morandror.scclient.utils.SharedStrings.GET_CLOSET_URL;
import static com.morandror.scclient.utils.SharedStrings.REQUEST_TIMEOUT;

public class ClosetAdapter extends RecyclerView.Adapter<ClosetAdapter.MyViewHolder> {

    private ArrayList<Closet> dataSet;
    private deleteClosetListener listener;

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Closet mItem;

        TextView textViewName;
        TextView textViewDescription;
        ImageView imageViewIcon;
        ImageButton trashBtn;
        ImageButton statsBtn;
        TextView closetId;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.textViewName = itemView.findViewById(R.id.textViewName);
            this.textViewDescription = itemView.findViewById(R.id.textViewVersion);
            this.imageViewIcon = itemView.findViewById(R.id.imageView);
            this.trashBtn = itemView.findViewById(R.id.imgButton_del_closet);
            this.statsBtn = itemView.findViewById(R.id.imgButton_stats_closet);
            this.closetId = itemView.findViewById(R.id.closet_id_card);
        }

        public void setItem(Closet item) {
            mItem = item;
        }

        @Override
        public void onClick(View view) {
            final View view1 = view;
            JsonObjectRequest request = new JsonObjectRequest(String.format(GET_CLOSET_URL, mItem.getId()), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Got closet from server");
                            Intent closetInfo = new Intent(view1.getContext(), ClosetInfoActivity.class);
                            closetInfo.putExtra(view1.getContext().getString(R.string.closet), (Closet) JsonHandler.getInstance().fromString(response.toString(), Closet.class));
                            view1.getContext().startActivity(closetInfo);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Failed to get closet info from server");
                    Toast.makeText(view1.getContext(), "Something went wrong, Failed to get closet data", Toast.LENGTH_LONG).show();
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueueSingleton.getInstance(view.getContext()).getRequestQueue().add(request);
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
        TextView textViewDescription = holder.textViewDescription;
        ImageView imageView = holder.imageViewIcon;
        final TextView closetId = holder.closetId;

        textViewName.setText(currentCloset.getName());
        textViewDescription.setText(currentCloset.getDescription());
        closetId.setText(String.valueOf(currentCloset.getId()));

        if (currentCloset.getImage() != null && !currentCloset.getImage().isEmpty()) {
            Bitmap bMap = BitmapFactory.decodeByteArray(currentCloset.getImage().getBytes(), 0, currentCloset.getImage().getBytes().length);
            imageView.setImageBitmap(bMap);

            ByteBuffer bb = ByteBuffer.wrap(currentCloset.getImage().getBytes());
            ImageDecoder.Source source = ImageDecoder.createSource(bb);
            try {
                Bitmap bMap2 = ImageDecoder.decodeBitmap(source);
                imageView.setImageBitmap(bMap2);
            } catch (IOException e) {
                System.out.println("Failed to init image");
                imageView.setImageResource(R.drawable.sclogowhite);
            }
        } else {
            imageView.setImageResource(R.drawable.sclogowhite);
        }

        ImageButton trashBtn = holder.trashBtn;
        ImageButton statsBtn = holder.statsBtn;

        trashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("delete closet with id " + String.valueOf(currentCloset.getId()));
                final View finalView = view;
                StringRequest request = new StringRequest(String.format(DELETE_CLOSET_URL, currentCloset.getId()),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
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

                request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueueSingleton.getInstance(view.getContext()).getRequestQueue().add(request);
            }
        });

        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View view1 = view;
                JsonObjectRequest request = new JsonObjectRequest
                        (String.format(GET_CLOSET_STATS_URL, currentCloset.getId()), null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Intent statsIntent = new Intent(view1.getContext(), StatsActivity.class);
                                statsIntent.putExtra(view1.getContext().getString(R.string.stats), (Statistics) JsonHandler.getInstance().fromString(response.toString(), Statistics.class));
                                view1.getContext().startActivity(statsIntent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("Failed to get statistics, error: " + error.toString());
                                Toast.makeText(view1.getContext(), "Failed to get closet statistics", Toast.LENGTH_LONG).show();
                            }
                        });

                request.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                RequestQueueSingleton.getInstance(view1.getContext()).getRequestQueue().add(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setDeleteClosetListener(deleteClosetListener listener) {
        this.listener = listener;
    }
}
